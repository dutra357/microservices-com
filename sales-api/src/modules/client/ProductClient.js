import axios from "axios";

import { PRODUCT_API_URL } from "../../config/constants/secrets.js";

class ProductClient {
    async checkProductStock(products, token) {
        try {
            const headers = {
                Authorization: `Bearer ${token}`,
            }
            console.info(`Sending request to Product API. Data: ${JSON.stringify(products)}`);
            axios.post(`${PRODUCT_API_URL}/check-stock`, { headers }, products)
                .then(res => {
                    return true;
                }).catch(error => {
                    console.error(`Error sending request to Product API. Error: ${error.message}`);
                    return false;
            })
        } catch (error) {
            return false;
        }
    }
}

export default new ProductClient();