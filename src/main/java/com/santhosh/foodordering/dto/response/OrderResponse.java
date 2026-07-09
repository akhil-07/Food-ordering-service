package com.santhosh.foodordering.dto.response;

import com.santhosh.foodordering.model.Order;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long ordid,
        Long customerId,
        String customerUsername,
        Long restaurantId,
        String restaurantName,
        String status,
        double totalAmount,
        LocalDateTime orderDate,
        List<OrderItemResponse> items
) {
    public static OrderResponse from(Order o) {
        return new OrderResponse(
                o.getOrdid(),
                o.getUser().getId(),
                o.getUser().getUsername(),
                o.getRestaurant().getRstid(),
                o.getRestaurant().getName(),
                o.getStatus().name(),
                o.getTotalAmount(),
                o.getOrderDate(),
                o.getItems().stream().map(OrderItemResponse::from).toList()
        );
    }
}
