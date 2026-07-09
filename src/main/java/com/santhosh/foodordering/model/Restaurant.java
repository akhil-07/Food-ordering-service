package com.santhosh.foodordering.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "restaurant")
@Getter
@Setter
@NoArgsConstructor
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rstid;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(length = 255)
    private String address;

    /** Whether the restaurant is currently open / accepting orders. */
    @Column(nullable = false)
    private boolean active;

    /** The RESTAURANT_OWNER that owns this restaurant. */
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private Users owner;
}