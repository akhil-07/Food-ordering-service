package com.santhosh.foodordering.repo;

import com.santhosh.foodordering.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Restaurant getByName(String name);



    void deleteById(Long id);
}
