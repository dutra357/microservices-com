import amqp from "amqplib/callback_api.js";
import { listenToSalesConfirmationQueue } from "../../modules/sales/rabbitMq/salesConfirmationListener.js";

import {
    PRODUCT_TOPIC, SALES_CONFIRMATION_ROUTING_QUEUE, PRODUCT_STOCK_UPDATE_QUEUE,
    PRODUCT_STOCK_UPDATE_ROUTING_KEY, SALES_CONFIRMATION_QUEUE
} from "./queue.js"

import { RABBIT_MQ_URL } from "../constants/secrets.js";


const TIME = 1000;

export async function connectRabbitMq() {
    connectionCreateQueue();
}

    async function connectionCreateQueue() {
        amqp.connect(RABBIT_MQ_URL, { timeout: 180000 },(error, connection) => {
            console.info("Starting RabbitMQ.");

            if (error) {
                throw error;
            }
            createQueue(connection, PRODUCT_STOCK_UPDATE_QUEUE, PRODUCT_STOCK_UPDATE_ROUTING_KEY,
                PRODUCT_TOPIC);

            createQueue(connection, SALES_CONFIRMATION_QUEUE, SALES_CONFIRMATION_ROUTING_QUEUE,
                PRODUCT_TOPIC);

            console.info("Queues and Topics were defined.")

            setTimeout(function () {
                connection.close();
            }, TIME);
        });
        setTimeout(function () {
            listenToSalesConfirmationQueue();
        }, TIME);
    }

    function createQueue(connection, queue, routingKey, topic) {
        connection.createChannel((error, channel) => {
            if (error) {
                throw error;
            }
            channel.assertExchange(topic, 'topic', {durable: true});
            channel.assertQueue(queue, {durable: true});

            channel.bindQueue(queue, topic, routingKey)
        });
    }
