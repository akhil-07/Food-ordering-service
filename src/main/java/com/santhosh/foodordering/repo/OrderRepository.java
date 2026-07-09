package com.santhosh.foodordering.repo;

import com.santhosh.foodordering.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Override
    @EntityGraph(attributePaths = {"user", "restaurant", "restaurant.owner", "items", "items.foodItem"})
    Optional<Order> findById(Long id);

    @EntityGraph(attributePaths = {"user", "restaurant", "restaurant.owner", "items", "items.foodItem"})
    Page<Order> findByUser_Id(Long userId, Pageable pageable);

    @EntityGraph(attributePaths = {"user", "restaurant", "restaurant.owner", "items", "items.foodItem"})
    Page<Order> findByRestaurant_Rstid(Long restaurantId, Pageable pageable);
}
