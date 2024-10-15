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

            const { transactionid, sarviceid } = req.headers;
            console.info(`Request to POST new order with data ${JSON.stringify(orderData)} |
             [transactionId: ${transactionid}] |
              serviceId: ${sarviceid}`);

            this.validateOrderData(orderData);
            const {authUser} = req;
            const { authorization } = req.headers;

            let order = this.createInitialOrderData(orderData, authUser, transactionid, sarviceid);
            await this.validateStock(order, authorization, transactionid);

            let createdOrder = await OrderRepository.save(order);
            this.sendMessage(createdOrder, transactionid);
            let response = {
                status: httpStatus.SUCCESS,
                order,
            }

            console.info(`Request to POST new order with data ${JSON.stringify(response)} |
             [transactionId: ${transactionid}] |
              serviceId: ${sarviceid}`);

            return response;
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
                console.warn(`The order message was not complete [transactionId: ${message.transactionId}].`)
            }
        } catch (error) {
            console.error("Could not parse order message from queue.")
            console.error(error.message);
        }
    }

    async validateStock(order, token, transactionid) {
        let stockLimit = await ProductClient.checkProductStock(order, token, transactionid);
        if (!stockLimit) {
            throw new OrderException(BAD_REQUEST, "The stock is out of products.")
        }
    }

    validateOrderData(data) {
        if (!data || !data.products) {
            throw new OrderException(BAD_REQUEST, "The products must be informed.");
        }
    }

    createInitialOrderData(orderData, authUser, transactionid, sarviceid) {
        return {
            status: PENDING,
            user: authUser,
            createdAt: new Date(),
            updatedAt: new Date(),
            transactionid,
            sarviceid,
            products: orderData.products,
        };
    }

    sendMessage(createdOrder, transactionid) {
        const message = {
            salesId: createdOrder.id,
            products: createdOrder.products,
            transactionid,
        }
        sendMessageProductStockUpdateQueue(createdOrder.products);
    }

    async findById(req) {
        try {
            const { id } = req.params;

            const { transactionid, sarviceid } = req.headers;
            console.info(`Request to GET order by ID[${id}] |
             [transactionId: ${transactionid}] | serviceId: ${sarviceid}`);

            this.validateId(id);
            const existingOrder = await OrderRepository.findById(id);
            if (!existingOrder) {
                throw new OrderException(BAD_REQUEST, "The order was not found.")
            }
            let response = {
                status: SUCCESS,
                existingOrder,
            }

            console.info(`Response to GET order by ID[${id}]: ${JSON.stringify(response)} |
             [transactionId: ${transactionid}] | serviceId: ${sarviceid}`);

            return  response;
        } catch (error) {
            return {
                status: error.status ? error.status : INTERNAL_SERVER_ERROR,
                message: error.message
            }
        }
    }

    async findAll(req) {
        try {
            const orders = await OrderRepository.findAll();

            const { transactionid, sarviceid } = req.headers;
            console.info(`Request to GET all |
             [transactionId: ${transactionid}] | serviceId: ${sarviceid}`);

            if (!orders) {
                throw new OrderException(BAD_REQUEST, "No orders were found.")
            }

            let response = {
                status: SUCCESS,
                orders,
            }

            console.info(`Response to GET orders: ${JSON.stringify(response)} |
             [transactionId: ${transactionid}] | serviceId: ${sarviceid}`);

            return  response;
        } catch (error) {
            return {
                status: error.status ? error.status : INTERNAL_SERVER_ERROR,
                message: error.message
            }
        }
    }

    async findByProductId(req) {
        try {
            const { productId } = req.params;
            this.validateId(productId);

            const { transactionid, sarviceid } = req.headers;
            console.info(`Request to GET order by Product ID[${productId}] |
             [transactionId: ${transactionid}] | serviceId: ${sarviceid}`);

            const orders = await OrderRepository.findByProductId(productId);
            if (!orders) {
                throw new OrderException(BAD_REQUEST, "No orders were found.")
            }

            let response = {
                status: SUCCESS,
                orders,
            }

            console.info(`Response to GET order by Product ID[${productId}] |
             [transactionId: ${transactionid}] | serviceId: ${sarviceid}`);

            return  response;
        } catch (error) {
            return {
                status: error.status ? error.status : INTERNAL_SERVER_ERROR,
                message: error.message
            }
        }
    }

    validateId(id) {
        if (!id) {
            throw new OrderException(BAD_REQUEST, "The ID must be informed.")
        }
    }
}
export default new OrderService();