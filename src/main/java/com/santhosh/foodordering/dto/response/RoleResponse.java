package com.santhosh.foodordering.dto.response;

import com.santhosh.foodordering.model.Role;

public record RoleResponse(
        Long roleId,
        String roleName,
        String roleDescription
) {
    public static RoleResponse from(Role r) {
        return new RoleResponse(r.getRoleId(), r.getRoleName(), r.getRoleDescription());
    }
}
