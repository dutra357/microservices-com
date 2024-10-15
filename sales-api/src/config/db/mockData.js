import Order from "../../modules/sales/model/Order.js";
import { v4 as uuidv4 } from "uuid";

export async function startInitialData(params) {
    try {
        let existingData = await Order.find();
        if (existingData && existingData.length > 0) {
            console.info("Remove existing data..")
            await Order.collection.drop();
        }

        await Order.create({
            products: [
                {
                    productId: 1,
                    quantity: 2
                },
                {
                    productId: 2,
                    quantity: 2
                },
                {
                    productId: 3,
                    quantity: 1
                }
            ],

            user: {
                id: 1,
                name: 'User Test',
                email: "usertest@gmail.com"
            },
            status: "APPROVED",
            createdAt: new Date(),
            updatedAt: new Date(),
            transactionid: uuidv4(),
            serviceid: uuidv4()
        });

        await Order.create({
            products: [
                {
                    productId: 1,
                    quantity: 2
                },
                {
                    productId: 2,
                    quantity: 2
                },
                {
                    productId: 3,
                    quantity: 1
                }
            ],
            user: {
                id: 2,
                name: 'User Test Second',
                email: "usertest_second@gmail.com"
            },
            status: "REJECTED",
            createdAt: new Date(),
            updatedAt: new Date(),
            transactionid: uuidv4(),
            serviceid: uuidv4()
        });

        let mockData = await Order.find();
        console.info(`Mock data was created at: ${JSON.stringify(mockData, undefined, 4)}`);

    } catch (error) {
        console.log(error);
    }
}