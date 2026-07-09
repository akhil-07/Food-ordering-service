package com.santhosh.foodordering.service;

import com.santhosh.foodordering.dto.request.RoleRequest;
import com.santhosh.foodordering.exception.DuplicateResourceException;
import com.santhosh.foodordering.exception.ResourceNotFoundException;
import com.santhosh.foodordering.model.Role;
import com.santhosh.foodordering.repo.RoleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Transactional(readOnly = true)
    public Page<Role> findAll(Pageable pageable) {
        return roleRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Role findById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role", id));
    }

    public Role create(RoleRequest request) {
        if (roleRepository.existsByRoleName(request.roleName())) {
            throw new DuplicateResourceException("Role already exists: " + request.roleName());
        }
        return roleRepository.save(new Role(null, request.roleName(), request.roleDescription()));
    }

    public Role update(Long id, RoleRequest request) {
        Role role = findById(id);
        if (!role.getRoleName().equals(request.roleName()) && roleRepository.existsByRoleName(request.roleName())) {
            throw new DuplicateResourceException("Role already exists: " + request.roleName());
        }
        role.setRoleName(request.roleName());
        role.setRoleDescription(request.roleDescription());
        return roleRepository.save(role);
    }

    public void delete(Long id) {
        Role role = findById(id);
        roleRepository.delete(role);
    }
}
