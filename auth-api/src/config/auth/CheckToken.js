import jwt from "jsonwebtoken";
import { promisify } from "util";
import AuthException from "./AuthException.js";

import * as secrets from "../constants/Secret.js";
import * as HttpStatus from "../constants/HttpStatus.js";



const bearer = "bearer ";


export default async (request, response, next) => {
    try {
        const { authorization } = request.headers;
        let accessToken = authorization;

        if (!authorization) {
            throw new AuthException(HttpStatus.UNAUTHORIZED, "Access token was not informed.");
        }

        if (accessToken.toLowerCase().includes(bearer)) {
            accessToken = accessToken.replace(bearer, "");
        }

        const decoded = await promisify(jwt.verify)(accessToken, secrets.API_SECRET);

        request.authUser = decoded.authUser;

        return next();

    } catch (error) {
        return {
            status: error.status ? error.status : HttpStatus.INTERNAL_SERVER_ERROR,
            message: error.message
        };
    }
}