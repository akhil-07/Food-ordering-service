package com.example.demo.controller;

import com.example.demo.service.UserService;
import com.example.demo.model.Users;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping
    public Users save(@RequestBody Users users) {
        return userService.save(users);
    }
    @GetMapping("/{id}")
    public Users findById(@PathVariable("id") Long id){
        return userService.findById(id).orElseThrow(()-> new RuntimeException("User not found"));
    }
    @GetMapping
    public List<Users> getUsers(){
        return userService.getUsers();
    }
}
