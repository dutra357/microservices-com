class OrderException extends Error {
    constructor(status, message) {
        super(message);
        this.status = status;
        this.message = message;
        this.name = name;
        Error.captureStackTrace(this, this.constructor);
    }
}

export default OrderException;