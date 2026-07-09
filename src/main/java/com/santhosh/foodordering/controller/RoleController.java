package com.santhosh.foodordering.controller;

import com.santhosh.foodordering.dto.request.RoleRequest;
import com.santhosh.foodordering.dto.response.PageResponse;
import com.santhosh.foodordering.dto.response.RoleResponse;
import com.santhosh.foodordering.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/** Role administration - ADMIN only. */
@RestController
@RequestMapping("/api/roles")
@PreAuthorize("hasRole('ADMIN')")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public PageResponse<RoleResponse> findAll(@PageableDefault(size = 10, sort = "roleId") Pageable pageable) {
        return PageResponse.of(roleService.findAll(pageable), RoleResponse::from);
    }

    @GetMapping("/{id}")
    public RoleResponse findById(@PathVariable Long id) {
        return RoleResponse.from(roleService.findById(id));
    }

    @PostMapping
    public ResponseEntity<RoleResponse> create(@Valid @RequestBody RoleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(RoleResponse.from(roleService.create(request)));
    }

    @PutMapping("/{id}")
    public RoleResponse update(@PathVariable Long id, @Valid @RequestBody RoleRequest request) {
        return RoleResponse.from(roleService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        roleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
