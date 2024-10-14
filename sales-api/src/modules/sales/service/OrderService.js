import OrderRepository from "../repository/OrderRepository.js";
import { sendMessageProductStockUpdateQueue } from "../../product/rabbitMq/productStockUpdateSender.js";
import * as httpStatus from "../../../config/constants/httpStatus.js";
import {ACCEPTED, PENDING, REJECTED} from "../status/OrderStatus.js";
import OrderException from "../exception/OrderException.js";
import {BAD_REQUEST, INTERNAL_SERVER_ERROR, SUCCESS} from "../../../config/constants/httpStatus.js";
import ProductClient from "../../client/ProductClient.js";

class OrderService {
    async createOrder(req) {
        try {
            let orderData = req.body;

            this.validateOrderData(orderData);
            const {authUser} = req;
            const { authorization } = req.headers;

            let order = this.createInitialOrderData(orderData, authUser);
            await this.validateStock(order, authorization);

            let createdOrder = await OrderRepository.save(order);
            this.sendMessage(createdOrder);
            return {
                status: httpStatus.SUCCESS,
                order,
            }
        } catch (error) {
            return {
                status: error.status ? error.status : httpStatus.INTERNAL_SERVER_ERROR,
                message: error.message
            }
        }
    }

    async updateOrder(message) {
        try {
            const order = JSON.parse(message);

            if (order.salesId && order.status) {
                let existingOrder = await OrderRepository.findById(order.salesId);

                if (existingOrder && order.status !== existingOrder.status) {
                    existingOrder.status = order.status;
                    existingOrder.updatedAt = new Date();
                    await OrderRepository.save(existingOrder);
                }
            } else {
                console.warn("The order message was not complete.")
            }
        } catch (error) {
            console.error("Could not parse order message from queue.")
            console.error(error.message);
        }
    }

    async validateStock(order, token) {
        let stockLimit = await ProductClient.checkProductStock(order.products, token);
        if (stockLimit) {
            throw new OrderException(BAD_REQUEST, "The stock is out of products.")
        }
    }

    validateOrderData(data) {
        if (!data || !data.products) {
            throw new OrderException(BAD_REQUEST, "The products must be informed.");
        }
    }

    createInitialOrderData(orderData, authUser) {
        return {
            status: PENDING,
            user: authUser,
            createdAt: new Date(),
            updatedAt: new Date(),
            products: orderData,
        };
    }

    sendMessage(createdOrder) {
        const message = {
            salesId: createdOrder.id,
            products: createdOrder.products
        }
        sendMessageProductStockUpdateQueue(createdOrder.products);
    }

    async findById(req) {
        const { id } = req.params;
        this.validateId(id);
        const existingOrder = await OrderRepository.findById(id);
        if (!existingOrder) {
            throw new OrderException(BAD_REQUEST, "The order was not found.")
        }

        try {
            return {
                status: SUCCESS,
                existingOrder,
            }
        } catch (error) {
            return {
                status: error.status ? error.status : INTERNAL_SERVER_ERROR,
                message: error.message
            }
        }
    }

    validateId(id) {
        if (!id) {
            throw new OrderException(BAD_REQUEST, "The order ID must be informed.")
        }
    }
}
export default new OrderService();