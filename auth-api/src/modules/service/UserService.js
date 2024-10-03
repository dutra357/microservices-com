import UserRepository from "../repository/UserRepository.js"
import * as HttpStatus from "../../config/constants/HttpStatus.js"

import UserException from "../user/UserException/UserException.js";

class UserService {

    async findByEmail(request) {
        try {
            const { email } = request.params;
            this.validateEmail(email);

            let user = UserRepository.findByEmail(email);
            
            if (!user) {
                throw new UserException(HttpStatus.BAD_REQUEST, "User not found.");
            }

            return {
                status: HttpStatus.SUCCESS,
                user: {
                    id: user.id,
                    nome: user.nome,
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
            throw new Error("User email was not informed.");
        }
    
    }

}

export default new UserService();