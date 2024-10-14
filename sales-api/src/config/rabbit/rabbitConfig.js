import amqp from "amqplib/callback_api.js";
import { listenToSalesConfirmationListener } from "../../modules/sales/rabbitMq/salesConfirmationListener.js";

import { PRODUCT_TOPIC, SALES_CONFIRMATION_QUEUE,
    SALES_CONFIRMATION_ROUTING_QUEUE, PRODUCT_STOCK_UPDATE_QUEUE,
    PRODUCT_STOCK_UPDATE_ROUTING_KEY } from "./queue.js"

import { RABBIT_MQ_URL } from "../constants/secrets.js";

const TIME = 2000;
const HALF_MINUTES = 30000;
const CONTAINER_ENV = "container";

export async function connectRabbitMq() {
    const env = process.env.NODE_ENV;

       if (CONTAINER_ENV === env) {
           console.info("Waiting for RabbitMQ starts.");

           setInterval(async () => {
               await connectionCreateQueue();
           }, HALF_MINUTES);
       } else {
           await connectionCreateQueue();
       }
}

    async function connectionCreateQueue() {
        amqp.connect(RABBIT_MQ_URL, (error, connection) => {
            console.info("Starting RabbitMQ.");

            if (error) {
                throw error;
            }
            createQueue(connection, PRODUCT_STOCK_UPDATE_QUEUE, PRODUCT_STOCK_UPDATE_ROUTING_KEY,
                PRODUCT_TOPIC);
            createQueue(connection, SALES_CONFIRMATION_ROUTING_QUEUE, SALES_CONFIRMATION_ROUTING_QUEUE,
                PRODUCT_TOPIC);

            setTimeout(function () {
                connection.close();
            }, TIME);
        });
        listenToSalesConfirmationListener();
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
