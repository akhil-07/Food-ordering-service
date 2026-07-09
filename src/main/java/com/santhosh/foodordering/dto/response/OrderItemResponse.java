package com.santhosh.foodordering.dto.response;

import com.santhosh.foodordering.model.OrderItem;

public record OrderItemResponse(
        Long orditemid,
        Long foodItemId,
        String foodItemName,
        int quantity,
        double price,
        double lineTotal
) {
    public static OrderItemResponse from(OrderItem oi) {
        return new OrderItemResponse(
                oi.getOrditemid(),
                oi.getFoodItem().getFoodid(),
                oi.getFoodItem().getName(),
                oi.getQuantity(),
                oi.getPrice(),
                oi.getPrice() * oi.getQuantity()
        );
    }
}
