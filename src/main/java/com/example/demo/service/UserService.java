package com.example.demo.service;

import com.example.demo.model.Role;
import com.example.demo.model.Users;
import com.example.demo.repo.RoleRepository;
import com.example.demo.repo.UserRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final  UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public Users save(Users users){
        Long roleId=users.getRole().getRoleId();
        Role role=roleRepository.findById(roleId).orElseThrow(()->new RuntimeException("Role Not Found"));
        users.setRole(role);
        return userRepository.save(users);
    }
    public Optional<Users> findById(Long id){
        return userRepository.findById(id);
    }

    public List<Users> getUsers() {
        return userRepository.findAll();
    }
}
