package com.order_service.common.exception;

public class PaymentProcessingException extends RuntimeException{

    public PaymentProcessingException(String message){
        super(message);
    }

}
