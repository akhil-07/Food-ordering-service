package com.santhosh.foodordering.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "order_item")
@Getter
@Setter
@NoArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orditemid;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", referencedColumnName = "ordid", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "food_item_id", referencedColumnName = "foodid", nullable = false)
    private FoodItem foodItem;

    @Column(nullable = false)
    private int quantity;

    /** Unit price captured at order time (so later menu price changes don't rewrite history). */
    @Column(nullable = false)
    private double price;
}