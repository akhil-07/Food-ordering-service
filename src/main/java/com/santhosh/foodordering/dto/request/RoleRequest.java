package com.santhosh.foodordering.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RoleRequest(
        @NotBlank @Size(max = 50) String roleName,
        @Size(max = 200) String roleDescription
) {
}
