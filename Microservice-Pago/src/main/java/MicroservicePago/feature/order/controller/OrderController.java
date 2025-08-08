package MicroservicePago.feature.order.controller;


import MicroservicePago.common.exception.UnauthorizedAccessException;
import MicroservicePago.feature.order.dto.OrderRequest;
import MicroservicePago.feature.order.entity.OrderItem;
import MicroservicePago.common.dto.ApiResponse;
import MicroservicePago.feature.order.service.IOrderService;
import com.authcore.security.AuthenticatedUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static MicroservicePago.common.constants.ErrorMessageConstants.UNAUTHORIZED_ACCESS;
import static MicroservicePago.common.constants.GeneralConstants.DEFAULT_PAGE_NUMBER;
import static MicroservicePago.common.constants.GeneralConstants.ID_IN_PATH;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final IOrderService orderService;
    private final AuthenticatedUserService authenticatedUserService;

    @Value("${m2m.apikey}")
    private String m2mApiKey;

    @GetMapping(ID_IN_PATH)
    public List<OrderItem> getOrderById(@PathVariable Long id,
                                        @RequestHeader(value = "X-Internal-Key", required = false) String key)  {
        if (!key.equals(m2mApiKey)) {
            throw new UnauthorizedAccessException(UNAUTHORIZED_ACCESS);
        }
        return orderService.getOrderById(id).getOrderItems();
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createOrder(@RequestBody OrderRequest orderRequest)  {
        return new ResponseEntity<>(ApiResponse.success(orderService.createOrder(orderRequest)), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getOrdersByUser(@RequestParam(defaultValue = DEFAULT_PAGE_NUMBER) int page){
        Long userId = authenticatedUserService.getAuthenticatedUserId();
        return new ResponseEntity<>(ApiResponse.success(orderService.getOrdersByUser(userId, page)), HttpStatus.OK);
    }


}
