import express from "express";

import { startInitialData } from "./src/config/db/mockData.js"
import { connectMongoDb } from "./src/config/db/mongoConfig.js";
import Order from "./src/modules/sales/model/Order.js";
import checkToken from "./src/config/auth/checkToken.js";

import {connectRabbitMq} from "./src/config/rabbit/rabbitConfig.js";

import { sendMessageProductStockUpdateQueue } from "./src/modules/product/rabbitMq/productStockUpdateSender.js";
import e from "express";


const app = express();
const env = process.env;
const PORT = env.PORT || 8082

connectMongoDb();
startInitialData();
connectRabbitMq();

//app.use(checkToken);

app.get("/teste", (req, res) => {
    try {
        sendMessageProductStockUpdateQueue([
            {
                "productId" : 1001,
                "quantity": 1
            },
            {
                "productId" : 1002,
                "quantity": 9
            }
        ]);
        return res.status(200).json({status: 200});

    } catch (error) {
        console.log(error);
        return res.status(500).json({error: true});
    }
})


app.get('/api/status', async (req, res) => {
    let teste = await Order.find();
    console.log(teste);

    return res.status(200).json({
        service: "Sales-API",
        status: "UP",
        httpStatus: 200
    })
});


app.listen(PORT, () => {
    console.info(`Server started successfully at port: ${PORT}`)
});