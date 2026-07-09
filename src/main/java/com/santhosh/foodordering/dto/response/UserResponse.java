package com.santhosh.foodordering.dto.response;

import com.santhosh.foodordering.model.Users;

/** Public view of a user. Note: the password is intentionally never exposed. */
public record UserResponse(
        Long id,
        String username,
        String email,
        String role
) {
    public static UserResponse from(Users u) {
        return new UserResponse(u.getId(), u.getUsername(), u.getEmail(), u.getRole().getRoleName());
    }
}
