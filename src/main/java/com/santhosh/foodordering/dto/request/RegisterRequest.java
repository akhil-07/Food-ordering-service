package com.santhosh.foodordering.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Public self-registration. {@code role} is restricted to CUSTOMER or RESTAURANT_OWNER
 * (an ADMIN can only be created/promoted by another ADMIN). If blank, defaults to CUSTOMER.
 */
public record RegisterRequest(
        @NotBlank @Size(min = 3, max = 50) String username,
        @NotBlank @Size(min = 6, max = 100) String password,
        @NotBlank @Email String email,
        @Pattern(regexp = "CUSTOMER|RESTAURANT_OWNER", message = "role must be CUSTOMER or RESTAURANT_OWNER")
        String role
) {
}
