package com.santhosh.foodordering.controller;

import com.santhosh.foodordering.dto.request.FoodItemRequest;
import com.santhosh.foodordering.dto.response.FoodItemResponse;
import com.santhosh.foodordering.dto.response.PageResponse;
import com.santhosh.foodordering.service.FoodItemService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Food Item Management.
 *
 * Public endpoints:
 * - GET /api/restaurants/{restaurantId}/food-items (browse menu, no auth required)
 *
 * Protected endpoints:
 * - POST   (RESTAURANT_OWNER or ADMIN)
 * - PUT    (RESTAURANT_OWNER or ADMIN)
 * - DELETE (RESTAURANT_OWNER or ADMIN)
 */
@RestController
@RequestMapping("/api/restaurants/{restaurantId}/food-items")
public class FoodItemController {

    private final FoodItemService foodItemService;

    public FoodItemController(FoodItemService foodItemService) {
        this.foodItemService = foodItemService;
    }

    /**
     * Browse available food items in a restaurant.
     * Public endpoint.
     */
    @GetMapping
    public PageResponse<FoodItemResponse> browseMenu(
            @PathVariable Long restaurantId,
            @RequestParam(defaultValue = "false") boolean availableOnly,
            @PageableDefault(size = 20, sort = "foodid") Pageable pageable) {
        return PageResponse.of(
                foodItemService.findByRestaurant(restaurantId, availableOnly, pageable),
                FoodItemResponse::from
        );
    }

    /**
     * Get a single food item.
     * Public endpoint.
     */
    @GetMapping("/{foodItemId}")
    public FoodItemResponse getById(@PathVariable Long restaurantId, @PathVariable Long foodItemId) {
        return FoodItemResponse.from(foodItemService.findByRestaurantAndId(restaurantId, foodItemId));
    }

    /**
     * Add a new food item to a restaurant.
     * RESTAURANT_OWNER or ADMIN only.
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('RESTAURANT_OWNER', 'ADMIN')")
    public ResponseEntity<FoodItemResponse> create(
            @PathVariable Long restaurantId,
            @Valid @RequestBody FoodItemRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(FoodItemResponse.from(foodItemService.create(restaurantId, request)));
    }

    /**
     * Update a food item.
     * RESTAURANT_OWNER or ADMIN only.
     */
    @PutMapping("/{foodItemId}")
    @PreAuthorize("hasAnyRole('RESTAURANT_OWNER', 'ADMIN')")
    public FoodItemResponse update(
            @PathVariable Long restaurantId,
            @PathVariable Long foodItemId,
            @Valid @RequestBody FoodItemRequest request) {
        return FoodItemResponse.from(foodItemService.update(restaurantId, foodItemId, request));
    }

    /**
     * Delete a food item from a restaurant.
     * RESTAURANT_OWNER or ADMIN only.
     */
    @DeleteMapping("/{foodItemId}")
    @PreAuthorize("hasAnyRole('RESTAURANT_OWNER', 'ADMIN')")
    public ResponseEntity<Void> delete(
            @PathVariable Long restaurantId,
            @PathVariable Long foodItemId) {
        foodItemService.delete(restaurantId, foodItemId);
        return ResponseEntity.noContent().build();
    }
}

