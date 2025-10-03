package com.order_service.common.exception;


import static com.order_service.common.constants.ErrorMessageConstants.EMPTY_CART;

public class EmptyCartException extends RuntimeException{

    public EmptyCartException() {
        super(EMPTY_CART);

    }
}
