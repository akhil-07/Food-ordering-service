package com.santhosh.foodordering.dto.response;

import com.santhosh.foodordering.model.Restaurant;

public record RestaurantResponse(
        Long rstid,
        String name,
        String address,
        boolean active,
        Long ownerId,
        String ownerUsername
) {
    public static RestaurantResponse from(Restaurant r) {
        return new RestaurantResponse(
                r.getRstid(),
                r.getName(),
                r.getAddress(),
                r.isActive(),
                r.getOwner().getId(),
                r.getOwner().getUsername()
        );
    }
}
