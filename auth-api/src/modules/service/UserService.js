import bcrypt from "bcrypt";
import jwt from "jsonwebtoken";

import UserRepository from "../repository/UserRepository.js";
import * as HttpStatus from "../../config/constants/HttpStatus.js";
import * as secrets from "../../config/constants/Secret.js";

import UserException from "../user/UserException/UserException.js";

class UserService {

    async getAccessToken(request) {
        try {
            const { email, password } = request.body;
            
            this.validateAccessTokenData(email, password);

            let user = await UserRepository.findByEmail(email);
            
            this.validateUser(user);

            await this.validatePassword(password, user.password);

            let authUser = {id: user.id, name:user.name, email:user.email}    
            
            const ACCESS_TOKEN = jwt.sign({ authUser }, secrets.API_SECRET, { expiresIn: '1d' });
            return {
                status: HttpStatus.SUCCESS,
                ACCESS_TOKEN
            }

        } catch (error) {
            return {
                status: error.status ? error.status : HttpStatus.INTERNAL_SERVER_ERROR,
                message: error.message
            }
        }
    }


    async validatePassword(password, hashedPassword) {
        if (!await bcrypt.compare(password, hashedPassword)) {
            throw new UserException(HttpStatus.UNAUTHORIZED, "Password doesn't match.");
        }
    }

    validateAccessTokenData(email, password) {
        if (!email || !password) {
            throw new UserException(HttpStatus.UNAUTHORIZED, "Email and password must be informed.")
        }
    }

    async findByEmail(request) {
        try {
            const { email } = request.params;
            const { authUser } = request

            this.validateEmail(email);

            let user = await UserRepository.findByEmail(email);

            this.validateAuthenticatedUser(user, authUser);
            this.validateUser(user);

            return {
                status: HttpStatus.SUCCESS,
                user: {
                    id: user.id,
                    name: user.name,
                    email: user.email
                }
            }

        } catch (error) {
            return {
                status: error.status ? error.status : HttpStatus.INTERNAL_SERVER_ERROR,
                message: error.message
            }
        }
    }


    validateAuthenticatedUser(user, authUser) {
        if (!authUser.id || (user.id !== authUser.id)) {
            throw new UserException(HttpStatus.FORBIDDEN, "User cannot see others users data.")
        }
    }


    validateEmail(email) {
        if (!email) {
            throw new UserException(HttpStatus.BAD_REQUEST, "User email was not informed.");
        }
    
    }

    validateUser(user) {
        if (!user) {
            throw new UserException(HttpStatus.BAD_REQUEST, "User not found.");
        }
    }

}

export default new UserService();