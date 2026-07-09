package com.santhosh.foodordering.controller;

import com.santhosh.foodordering.dto.response.PageResponse;
import com.santhosh.foodordering.dto.response.UserResponse;
import com.santhosh.foodordering.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {


    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /** The caller's own profile - any authenticated user. */
    @GetMapping("/me")
    public UserResponse me() {
        return UserResponse.from(userService.getCurrentUser());
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public PageResponse<UserResponse> findAll(@PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return PageResponse.of(userService.findAll(pageable), UserResponse::from);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse findById(@PathVariable Long id) {
        return UserResponse.from(userService.findById(id));
    }

    /** Promote/demote a user, e.g. PUT /api/users/3/role?roleName=ADMIN */
    @PutMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse changeRole(@PathVariable Long id, @RequestParam String roleName) {
        return UserResponse.from(userService.changeRole(id, roleName));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
