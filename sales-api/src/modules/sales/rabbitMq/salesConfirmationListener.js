import amqp from "amqplib/callback_api.js";

import { RABBIT_MQ_URL } from "../../../config/constants/secrets.js";
import { SALES_CONFIRMATION_QUEUE } from "../../../config/rabbit/queue.js";

export function listenToSalesConfirmationListener() {
    amqp.connect(RABBIT_MQ_URL, (error, connection) => {
        if (error) {
            throw error;
        }

        console.info("Listening Sales Confirmation Queue...");
        connection.createChannel((error, channel) => {
            if (error) {
                throw error;
            }

            channel.consume(SALES_CONFIRMATION_QUEUE, (message) => {
                console.info(`Recieving message from queue: ${message.content.toString()}`);
            }, {
                noAck: true,
            });
        });
    });
}