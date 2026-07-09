package com.santhosh.foodordering.service;

import com.santhosh.foodordering.dto.request.LoginRequest;
import com.santhosh.foodordering.dto.request.RegisterRequest;
import com.santhosh.foodordering.dto.response.AuthResponse;
import com.santhosh.foodordering.exception.DuplicateResourceException;
import com.santhosh.foodordering.exception.ResourceNotFoundException;
import com.santhosh.foodordering.model.Role;
import com.santhosh.foodordering.model.Users;
import com.santhosh.foodordering.repo.RoleRepository;
import com.santhosh.foodordering.repo.UserRepository;
import com.santhosh.foodordering.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder, JwtService jwtService,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new DuplicateResourceException("Username already taken: " + request.username());
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Email already registered: " + request.email());
        }

        String roleName = StringUtils.hasText(request.role()) ? request.role() : "CUSTOMER";
        Role role = roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleName));

        Users user = new Users();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(role);
        userRepository.save(user);

        String token = jwtService.generateToken(user.getUsername(), role.getRoleName());
        return AuthResponse.bearer(token, user.getUsername(), role.getRoleName(), jwtService.getExpirationMs());
    }

    public AuthResponse login(LoginRequest request) {
        // Throws BadCredentialsException on failure (handled globally -> 401).
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password()));

        Users user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.username()));

        String token = jwtService.generateToken(user.getUsername(), user.getRole().getRoleName());
        return AuthResponse.bearer(token, user.getUsername(), user.getRole().getRoleName(),
                jwtService.getExpirationMs());
    }
}
