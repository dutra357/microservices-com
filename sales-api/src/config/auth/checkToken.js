import jwt from "jsonwebtoken";
import { promisify } from "util";

import AuthException from "./AuthException.js";

import { API_SECRET } from "../secrets/secrets.js";
import { UNAUTHORIZED, INTERNAL_SERVER_ERROR} from "../httpStatus/httpStatus.js";


const emptySpace = " ";

export default async (request, response, next) => {
    try {
        let { authorization } = request.headers;

        if (!authorization) {
            throw new AuthException(UNAUTHORIZED, "Access token was not informed.");
        }

        let accessToken = authorization;

        if (accessToken.includes(emptySpace)) {
            accessToken = accessToken.split(emptySpace)[1];
        }

        const decoded = await promisify(jwt.verify)(accessToken, API_SECRET);

        request.authUser = decoded.authUser;

        return next();

    } catch (error) {
        const statusCode = error.status ? error.status : INTERNAL_SERVER_ERROR;
        return response.status(statusCode).json({ statusCode, message: error.message });
    }
}