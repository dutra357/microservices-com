import express from "express";

import { startInitialData } from "./src/config/db/mockData.js"
import { connectMongoDb } from "./src/config/db/mongoConfig.js";
import {connectRabbitMq} from "./src/config/rabbit/rabbitConfig.js";

import checkToken from "./src/config/auth/checkToken.js";
import orderRoutes from "./src/modules/sales/routes/OrderRoutes.js";
import tracing from "./src/config/tracing.js";


const app = express();
const env = process.env;
const PORT = env.PORT || 8082
const CONTAINER_ENV = "container";
const TIME = 1800;

startInitialData();
console.info("Starting RabbitMQ and MongoDB..");
connectMongoDb();
connectRabbitMq();

app.use(express.json())

app.get("/api/initial-data", async (req, res) => {
    await startInitialData();
    return res.json({ message: "Initial Data ok.." });
});

app.get("/", async (req, res) => {
    return res.status(200).json(getOkResponse());
});

app.get('/api/status', (req, res) => {
    return res.status(200).json(getOkResponse());
});

function getOkResponse() {
    return {
        service: "Sales-API",
        status: "up",
        httpStatus: 200,
    }
}

app.use(tracing)
app.use(checkToken);
app.use(orderRoutes);

app.listen(PORT, () => {
    console.info(`Server started successfully at port: ${PORT}`)
});