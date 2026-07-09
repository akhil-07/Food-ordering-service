package com.santhosh.foodordering.dto.response;

import com.santhosh.foodordering.model.FoodItem;

import java.math.BigDecimal;

public record FoodItemResponse(
        Long foodid,
        String name,
        double price,
        String category,
        boolean available,
        Long restaurantId,
        String restaurantName
) {
    public static FoodItemResponse from(FoodItem f) {
        return new FoodItemResponse(
                f.getFoodid(),
                f.getName(),
                f.getPrice(),
                f.getCategory(),
                f.isAvailable(),
                f.getRestaurant().getRstid(),
                f.getRestaurant().getName()
        );
    }
}
