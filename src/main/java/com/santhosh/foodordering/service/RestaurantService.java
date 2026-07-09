package com.santhosh.foodordering.service;

import com.santhosh.foodordering.dto.request.RestaurantRequest;
import com.santhosh.foodordering.exception.ResourceNotFoundException;
import com.santhosh.foodordering.model.Restaurant;
import com.santhosh.foodordering.repo.RestaurantRepository;
import com.santhosh.foodordering.security.CurrentUserProvider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final CurrentUserProvider currentUser;

    public RestaurantService(RestaurantRepository restaurantRepository, CurrentUserProvider currentUser) {
        this.restaurantRepository = restaurantRepository;
        this.currentUser = currentUser;
    }

    /** Creates a restaurant owned by the currently authenticated RESTAURANT_OWNER. */
    public Restaurant create(RestaurantRequest request) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(request.name());
        restaurant.setAddress(request.address());
        restaurant.setActive(true);
        restaurant.setOwner(currentUser.getUser());
        return restaurantRepository.save(restaurant);
    }

    @Transactional(readOnly = true)
    public Page<Restaurant> findAll(Pageable pageable) {
        return restaurantRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Restaurant> findActive(Pageable pageable) {
        return restaurantRepository.findByActiveTrue(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Restaurant> findMine(Pageable pageable) {
        if (currentUser.isAdmin()) {
            return restaurantRepository.findAll(pageable);
        }
        return restaurantRepository.findByOwner_Id(currentUser.getId(), pageable);
    }

    @Transactional(readOnly = true)
    public Restaurant findById(Long id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", id));
    }

    public Restaurant update(Long id, RestaurantRequest request) {
        Restaurant restaurant = findById(id);
        assertCanManage(restaurant);
        restaurant.setName(request.name());
        restaurant.setAddress(request.address());
        return restaurantRepository.save(restaurant);
    }

    public Restaurant setActive(Long id, boolean active) {
        Restaurant restaurant = findById(id);
        assertCanManage(restaurant);
        restaurant.setActive(active);
        return restaurantRepository.save(restaurant);
    }

    public void delete(Long id) {
        Restaurant restaurant = findById(id);
        assertCanManage(restaurant);
        restaurantRepository.delete(restaurant);
    }

    /** Only the owning RESTAURANT_OWNER or an ADMIN may mutate a restaurant. */
    private void assertCanManage(Restaurant restaurant) {
        if (!currentUser.isAdmin() && !restaurant.getOwner().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You do not own this restaurant");
        }
    }
}
