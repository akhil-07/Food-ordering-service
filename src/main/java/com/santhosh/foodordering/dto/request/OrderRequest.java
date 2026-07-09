package com.santhosh.foodordering.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OrderRequest(
        @NotNull Long restaurantId,
        @NotEmpty(message = "an order must contain at least one item")
        @Valid List<OrderItemRequest> items
) {
}
