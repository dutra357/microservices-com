import express from "express";

import { startInitialData } from "./src/config/db/mockData.js"
import { connect } from "./src/config/db/mongoConfig.js";
import Order from "./src/modules/sales/model/Order.js";



const app = express();
const env = process.env;
const PORT = env.PORT || 8082

connect();
startInitialData()

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