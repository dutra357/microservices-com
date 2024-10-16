import mongoose from 'mongoose';

import { MONGO_DB_URL } from '../constants/secrets.js'

export function connectMongoDb() {
    mongoose.connect(MONGO_DB_URL, {
        serverSelectionTimeoutMS: 10000,
    });

    mongoose.connection.on('connected', function() {
        console.info("The application connected to mongoDB successfully!");
    });
    mongoose.connection.on('error', function() {
        console.error("The application cannot connected to mongoDB successfully!");

    });
}