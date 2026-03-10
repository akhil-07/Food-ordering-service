package com.example.demo.controller;

import com.example.demo.model.Role;
import com.example.demo.service.RoleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
public class RoleController {

    private final RoleService roleService;
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }
    @GetMapping
    public List<Role> findAll() {
        return roleService.findAll();
    }
    @PostMapping
    public Role save(@RequestBody Role role) {
        return roleService.save(role);
    }
    @GetMapping("/{id}")
    public Role findById(@PathVariable Long id) {
        return roleService.findById(id).orElseThrow(()->new RuntimeException("Role not found"));
    }
}
