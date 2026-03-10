package com.santhosh.foodordering.controller;

import com.santhosh.foodordering.model.Restaurant;
import com.santhosh.foodordering.repo.RestaurantRepository;
import com.santhosh.foodordering.service.RestaurantService;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final RestaurantRepository restaurantRepository;

    public RestaurantController(RestaurantService restaurantService, RestaurantRepository restaurantRepository){
        this.restaurantService = restaurantService;
        this.restaurantRepository = restaurantRepository;
    }

    @PostMapping
    public Restaurant addRestaurant(@RequestBody Restaurant restaurant){
        return restaurantService.addRest(restaurant);
    }
    @GetMapping
    public List<Restaurant> getAllRestaurants(){
        return restaurantService.getRestaurants();
    }
    @GetMapping("/{id}/open")
    public Restaurant open(@PathVariable Long id){
        return restaurantService.openRestaurants(id);
    }
    @GetMapping("/{id}/close")
    public Restaurant close(@PathVariable Long id){
        return restaurantService.closeRestaurants(id);
    }
    @PutMapping("/{id}/status")
    public Restaurant updateStatus(@PathVariable Long id, @RequestParam boolean active){
        return restaurantService.updateStatus(id,active);
    }
    @GetMapping("/active")
    public List<Restaurant> getActiveRestaurants(){
        return restaurantService.getActiveRestaurants();
    }
}
