import { Router } from "express";
import OrderController from "../controller/OrderController.js";

const router = new Router();

router.get("/api/order/:id", OrderController.createOrder);
router.get("/api/orders", OrderController.findAll);
router.get("/api/order/:id", OrderController.findById);
router.get("/api/order/product/:id", OrderController.findByProductId);
router.post("/api/order/create", OrderController.createOrder);

export default router;