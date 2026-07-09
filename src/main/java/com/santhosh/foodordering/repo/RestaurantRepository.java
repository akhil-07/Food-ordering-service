package com.santhosh.foodordering.repo;

import com.santhosh.foodordering.model.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    Page<Restaurant> findByActiveTrue(Pageable pageable);

    Page<Restaurant> findByOwner_Id(Long ownerId, Pageable pageable);
}
