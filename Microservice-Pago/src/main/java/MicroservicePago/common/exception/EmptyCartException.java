package MicroservicePago.common.exception;


import static MicroservicePago.common.constants.ErrorMessageConstants.EMPTY_CART;

public class EmptyCartException extends RuntimeException{

    public EmptyCartException() {
        super(EMPTY_CART);

    }
}
