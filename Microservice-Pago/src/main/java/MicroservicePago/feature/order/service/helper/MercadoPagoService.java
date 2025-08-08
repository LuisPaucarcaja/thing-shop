package MicroservicePago.feature.order.service.helper;

import MicroservicePago.feature.order.dto.CartItem;
import MicroservicePago.feature.shipping.entity.ShippingAddress;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.*;
import com.mercadopago.resources.preference.Preference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static MicroservicePago.common.constants.MercadoPagoConstants.ORDER_ID_FIELD;
import static MicroservicePago.common.constants.MercadoPagoConstants.PAYMENT_METHOD_PAGOEFECTIVO_ATM;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.resources.payment.Payment;
@Service
public class MercadoPagoService {

    @Value("${mercado_pago.access_token}")
    private String ACCESS_TOKEN;


    public Payment getPaymentDetails(String paymentId) throws Exception {
        MercadoPagoConfig.setAccessToken(ACCESS_TOKEN);
        PaymentClient paymentClient = new PaymentClient();
        return paymentClient.get(Long.valueOf(paymentId));
    }

    public Preference createPaymentPreference(Long orderId,
                                              List<CartItem> cartItems,
                                              ShippingAddress shippingAddress)  {
        try {
            List<PreferenceItemRequest> items = mapCartItemsToPreferenceItems(cartItems);
            PreferenceShipmentsRequest shipments = buildShipments(shippingAddress);
            PreferencePaymentMethodsRequest paymentMethods = buildPaymentMethods();
            PreferenceRequest preferenceRequest = buildPreferenceRequest(orderId, items, shipments, paymentMethods);

            MercadoPagoConfig.setAccessToken(ACCESS_TOKEN);
            return new PreferenceClient().create(preferenceRequest);
        } catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }

    }


    private List<PreferenceItemRequest> mapCartItemsToPreferenceItems(List<CartItem> cartItems) {
        List<PreferenceItemRequest> items = new ArrayList<>();
        cartItems.forEach(cartItem -> items.add(PreferenceItemRequest.builder()
                .id(String.valueOf(cartItem.getVariantId()))
                .quantity(cartItem.getQuantity())
                .currencyId("PEN")
                .unitPrice((cartItem.getPrice()))
                .build()));
        return items;
    }

    private PreferenceShipmentsRequest buildShipments(ShippingAddress shippingAddress) {
        String city = shippingAddress.getShippingDistrict().getShippingCity().getName();
        String district = shippingAddress.getShippingDistrict().getName();

        PreferenceReceiverAddressRequest address = PreferenceReceiverAddressRequest.builder()
                .stateName(city)
                .cityName(district)
                .zipCode(shippingAddress.getPostalCode())
                .build();

        return PreferenceShipmentsRequest.builder()
                .cost(shippingAddress.getShippingDistrict().getShippingPrice())
                .receiverAddress(address)
                .mode("not_specified")
                .build();
    }

    private PreferencePaymentMethodsRequest buildPaymentMethods() {
        List<PreferencePaymentTypeRequest> excludedPaymentTypes = List.of(
                PreferencePaymentTypeRequest.builder().id("ticket").build()
        );

        List<PreferencePaymentMethodRequest> excludedPaymentMethods = List.of(
                PreferencePaymentMethodRequest.builder().id(PAYMENT_METHOD_PAGOEFECTIVO_ATM).build() //Excludes Cash Payment
        );

        return PreferencePaymentMethodsRequest.builder()
                .installments(1) // Cuotas
                .excludedPaymentTypes(excludedPaymentTypes)
                .excludedPaymentMethods(excludedPaymentMethods)
                .build();
    }


    private PreferenceRequest buildPreferenceRequest(Long orderId,
                                                     List<PreferenceItemRequest> items,
                                                     PreferenceShipmentsRequest shipments,
                                                     PreferencePaymentMethodsRequest paymentMethods) {
        return PreferenceRequest.builder()
                .items(items)
                .shipments(shipments)
                .paymentMethods(paymentMethods)
                .metadata(Map.of(ORDER_ID_FIELD, orderId))
                .expires(true)
                .expirationDateTo(OffsetDateTime.now().plusDays(1))
                .build();
    }

}
