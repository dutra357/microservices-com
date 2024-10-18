import axios from "axios";

import { PRODUCT_API_URL } from "../../config/constants/secrets.js";
import {response} from "express";

class ProductClient {
    async checkProductStock(productsData, token, transactionid) {
        try {
            const headers = {
                Authorization: token,
                transactionid,
            }
            console.info(`Sending request to Product API. Data: ${JSON.stringify(productsData)} | 
            [transactionId: ${transactionid}]`);

            let response = false;

            await axios.post(`${PRODUCT_API_URL}/check-stock`, { products: productsData.products }, { headers })
                .then(res => {
                    console.info(`Success response from Product-API. [transactionId: ${transactionid}]`)
                    response = true;
                }).catch(error => {
                    console.error(`Error sending request to Product API - [transactionId: ${transactionid}]. Error: ${error.message}`);
                    response = false;
            })
        } catch (error) {
            console.error(`Error sending request to Product API - [transactionId: ${transactionid}]. Error: ${error.message}`);
            return false;
        }

        return response;
    }
}

export default new ProductClient();