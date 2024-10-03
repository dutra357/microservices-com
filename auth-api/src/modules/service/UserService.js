import UserRepository from "../repository/UserRepository.js"
import * as HttpStatus from "../../config/constants/HttpStatus.js"

import UserException from "../user/UserException/UserException.js";
import User from "../user/model/User.js";

class UserService {

    async findByEmail(request) {
        try {
            const { email } = request.params;
            this.validateEmail(email);

            let user = await UserRepository.findByEmail(email);
            
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