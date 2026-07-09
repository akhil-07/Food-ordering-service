package com.santhosh.foodordering.repo;

import com.santhosh.foodordering.model.FoodItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {

    @EntityGraph(attributePaths = "restaurant")
    Optional<FoodItem> findById(Long id);

    @EntityGraph(attributePaths = "restaurant")
    Page<FoodItem> findByRestaurant_Rstid(Long restaurantId, Pageable pageable);

    @EntityGraph(attributePaths = "restaurant")
    Page<FoodItem> findByRestaurant_RstidAndAvailableTrue(Long restaurantId,
                                                          Pageable pageable);
}