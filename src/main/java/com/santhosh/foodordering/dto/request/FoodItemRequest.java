package com.santhosh.foodordering.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record FoodItemRequest(
        @NotBlank @Size(max = 120) String name,
        @Positive(message = "price must be greater than 0") double price,
        @Size(max = 60) String category,
        boolean available
) {
}
