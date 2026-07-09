package com.santhosh.foodordering.controller;

import com.santhosh.foodordering.dto.request.OrderRequest;
import com.santhosh.foodordering.dto.response.OrderResponse;
import com.santhosh.foodordering.dto.response.PageResponse;
import com.santhosh.foodordering.model.OrderStatus;
import com.santhosh.foodordering.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Order Management.
 *
 * CUSTOMER:
 * - POST (place order)
 * - GET /me (view own orders)
 * - GET /{orderId} (view own order details)
 * - PUT /{orderId}/cancel (cancel own order)
 *
 * RESTAURANT_OWNER:
 * - GET /restaurants/{restaurantId} (view orders for their restaurant)
 * - PUT /{orderId}/status (update order status)
 *
 * ADMIN:
 * - Full access to all orders
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Place a new order (CUSTOMER).
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('CUSTOMER', 'RESTAURANT_OWNER', 'ADMIN')")
    public ResponseEntity<OrderResponse> placeOrder(@Valid @RequestBody OrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(OrderResponse.from(orderService.placeOrder(request)));
    }

    /**
     * Get all orders for the currently authenticated user (CUSTOMER).
     * Restaurant owners and admins see all orders.
     */
    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'RESTAURANT_OWNER', 'ADMIN')")
    public PageResponse<OrderResponse> getMyOrders(
            @PageableDefault(size = 10, sort = "ordid") Pageable pageable) {
        return PageResponse.of(orderService.findMine(pageable), OrderResponse::from);
    }

    /**
     * Get an order by its ID.
     */
    @GetMapping("/{orderId}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'RESTAURANT_OWNER', 'ADMIN')")
    public OrderResponse getById(@PathVariable Long orderId) {
        return OrderResponse.from(orderService.getById(orderId));
    }

    /**
     * Get all orders for a specific restaurant (RESTAURANT_OWNER or ADMIN).
     */
    @GetMapping("/restaurants/{restaurantId}")
    @PreAuthorize("hasAnyRole('RESTAURANT_OWNER', 'ADMIN')")
    public PageResponse<OrderResponse> getByRestaurant(
            @PathVariable Long restaurantId,
            @PageableDefault(size = 10, sort = "ordid") Pageable pageable) {
        return PageResponse.of(
                orderService.findByRestaurant(restaurantId, pageable),
                OrderResponse::from
        );
    }

    /**
     * Update order status (RESTAURANT_OWNER or ADMIN).
     * RESTAURANT_OWNER can only update orders for their restaurants.
     */
//    @PutMapping("/{orderId}/status")
//    @PreAuthorize("hasAnyRole('RESTAURANT_OWNER', 'ADMIN')")
//    public OrderResponse updateStatus(
//            @PathVariable Long orderId,
//            @RequestParam OrderStatus status) {
//        return OrderResponse.from(orderService.updateStatus(orderId, status));
//    }

    @PutMapping("/{orderId}/accept")
    @PreAuthorize("hasAnyRole('RESTAURANT_OWNER','ADMIN')")
    public OrderResponse acceptOrder(
            @PathVariable Long orderId) {

        return OrderResponse.from(
                orderService.acceptOrder(orderId));

    }

    @PutMapping("/{orderId}/prepare")
    @PreAuthorize("hasAnyRole('RESTAURANT_OWNER','ADMIN')")
    public OrderResponse startPreparing(
            @PathVariable Long orderId) {

        return OrderResponse.from(
                orderService.startPreparing(orderId));

    }

    @PutMapping("/{orderId}/dispatch")
    @PreAuthorize("hasAnyRole('RESTAURANT_OWNER','ADMIN')")
    public OrderResponse dispatch(
            @PathVariable Long orderId) {

        return OrderResponse.from(
                orderService.markOutForDelivery(orderId));

    }

    @PutMapping("/{orderId}/deliver")
    @PreAuthorize("hasAnyRole('RESTAURANT_OWNER','ADMIN')")
    public OrderResponse deliver(
            @PathVariable Long orderId) {

        return OrderResponse.from(
                orderService.markDelivered(orderId));

    }

    @PutMapping("/{orderId}/cancel")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'RESTAURANT_OWNER', 'ADMIN')")
    public OrderResponse cancelOrder(@PathVariable Long orderId) {
        return OrderResponse.from(orderService.cancel(orderId));
    }
}

