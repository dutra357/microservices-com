import express from "express";
import * as db from "./src/config/initialData.js";
import UserRoutes from "./src/modules/routes/UserRoutes.js";
import tracing from "./src/config/tracing.js";

const app = express();
const env = process.env;
const PORT = env.PORT || 8080;
const CONTAINER_ENV = "container";

app.get('/api/status', (req, res) => {
    return res.status(200).json({
        service: 'Auth-API',
        status: "up",
        httpStatus: 200
    })
});

app.use(express.json());

start();
function start() {
    if (env.NODE_ENV !== CONTAINER_ENV) {}
    db.createInitialData();
}

app.get('/api/initial-data', (req, res) => {
    start();
    return res.json({ message: "Data created." })
});

app.use(tracing);
app.use(UserRoutes);

app.listen(PORT, () => {console.info(`Server started successfully at port: ${PORT}`)})