package MicroservicePago.common.constants;

import lombok.Getter;

public class MercadoPagoConstants {

    public static final String METADATA_FIELD = "metadata";
    public static final String FIELD_PAYMENT_METHOD_ID = "payment_method_id";
    public static final String DATA_FIELD = "data";

    public static final String PAYMENT_METHOD_PAGOEFECTIVO_ATM = "pagoefectivo_atm";

    public static final String ACTION_FIELD = "action";
    public static final String STATUS_FIELD = "status";
    public static final String ORDER_ID_FIELD = "order_id";

    public static final String FIELD_TRANSACTION_AMOUNT = "transaction_amount";



    @Getter
    public enum EventType {
        PAYMENT_CREATED("payment.created"),
        PAYMENT_CANCELED("payment.canceled");

        private final String value;

        EventType(String value) {
            this.value = value;
        }
    }

    @Getter
    public enum PaymentStatus {
        APPROVED("approved"),
        PENDING("pending"),
        CANCELED("cancelled");

        private final String value;

        PaymentStatus(String value) {
            this.value = value;
        }
    }
}
