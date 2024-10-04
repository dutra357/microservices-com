import bcrypt from "bcrypt";
import User from "../modules/user/model/User.js";


export async function createInitialData() {
    try {
        
        await User.sync({ force: true });

        let password1 = await bcrypt.hash("123456", 10);
        let password2 = await bcrypt.hash("123456", 10);


        let firstUser = await User.create({
            name: "User Test-1",
            email: "user1@gmail.com",
            password: password1
        });

        let secondUser = await User.create({
            name: "User Test-2",
            email: "user2@gmail.com",
            password: password2
        });

    } catch (err) {
        console.log(err);
    }
}