package com.santhosh.foodordering.service;

import com.santhosh.foodordering.exception.ResourceNotFoundException;
import com.santhosh.foodordering.model.Role;
import com.santhosh.foodordering.model.Users;
import com.santhosh.foodordering.repo.RoleRepository;
import com.santhosh.foodordering.repo.UserRepository;
import com.santhosh.foodordering.security.CurrentUserProvider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CurrentUserProvider currentUser;

    public UserService(UserRepository userRepository, RoleRepository roleRepository,
                       CurrentUserProvider currentUser) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.currentUser = currentUser;
    }

    @Transactional(readOnly = true)
    public Page<Users> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Users findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
    }

    /** The profile of the currently authenticated user. */
    @Transactional(readOnly = true)
    public Users getCurrentUser() {
        return findById(currentUser.getId());
    }

    /** ADMIN promotes/demotes a user by assigning a different role. */
    public Users changeRole(Long userId, String roleName) {
        Users user = findById(userId);
        Role role = roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleName));
        user.setRole(role);
        return userRepository.save(user);
    }

    public void delete(Long id) {
        Users user = findById(id);
        userRepository.delete(user);
    }
}
