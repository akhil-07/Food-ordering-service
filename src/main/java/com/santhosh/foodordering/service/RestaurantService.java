package com.santhosh.foodordering.service;

import com.santhosh.foodordering.model.Restaurant;
import com.santhosh.foodordering.model.Users;
import com.santhosh.foodordering.repo.RestaurantRepository;
import com.santhosh.foodordering.repo.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    public RestaurantService(RestaurantRepository restaurantRepository, UserRepository userRepository) {
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    public Restaurant addRest(Restaurant restaurant){
        Long id=restaurant.getUsers().getId();
        Users users=userRepository.findById(id).orElseThrow(()->new RuntimeException("User not found"));
        restaurant.setUsers(users);
        restaurant.setActive(true);
        return restaurantRepository.save(restaurant);
    }

    public List<Restaurant> getRestaurants() {
        return restaurantRepository.findAll();
    }


    public Restaurant updateStatus(Long id, boolean active) {
        Restaurant restaurant=restaurantRepository.getById(id);
        restaurant.setActive(active);
        return restaurantRepository.save(restaurant);
    }

    public List<Restaurant> getActiveRestaurants() {
        List<Restaurant> result=new ArrayList<>();
        List<Restaurant> rest=restaurantRepository.findAll();
        for(Restaurant r: rest){
            if(r.isActive()){
                result.add(r);
            }
        }
        return result;
    }

    public Restaurant openRestaurants(Long id) {
        Restaurant r=restaurantRepository.getById(id);
        r.setActive(true);
        return restaurantRepository.save(r);
    }

    public Restaurant closeRestaurants(Long id) {
        Restaurant r=restaurantRepository.getById(id);
        r.setActive(false);
        return restaurantRepository.save(r);
    }
}
