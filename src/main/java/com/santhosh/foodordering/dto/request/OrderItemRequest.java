package com.santhosh.foodordering.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderItemRequest(
        @NotNull Long foodItemId,
        @Positive(message = "quantity must be at least 1") int quantity
) {
}
