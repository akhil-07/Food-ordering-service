package com.santhosh.foodordering.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A role grants a set of permissions. We keep a single role per user for clarity
 * (ADMIN, RESTAURANT_OWNER, CUSTOMER). The {@code roleName} is stored WITHOUT the
 * Spring Security "ROLE_" prefix; the prefix is added when building authorities.
 */
@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    @Column(nullable = false, unique = true, length = 50)
    private String roleName;

    @Column(length = 200)
    private String roleDescription;
}
