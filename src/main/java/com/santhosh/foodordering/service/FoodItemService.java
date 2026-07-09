package com.santhosh.foodordering.service;

import com.santhosh.foodordering.dto.request.FoodItemRequest;
import com.santhosh.foodordering.exception.ResourceNotFoundException;
import com.santhosh.foodordering.model.FoodItem;
import com.santhosh.foodordering.model.Restaurant;
import com.santhosh.foodordering.repo.FoodItemRepository;
import com.santhosh.foodordering.security.CurrentUserProvider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FoodItemService {

    private final FoodItemRepository foodItemRepository;
    private final RestaurantService restaurantService;
    private final CurrentUserProvider currentUser;

    public FoodItemService(FoodItemRepository foodItemRepository, RestaurantService restaurantService,
                           CurrentUserProvider currentUser) {
        this.foodItemRepository = foodItemRepository;
        this.restaurantService = restaurantService;
        this.currentUser = currentUser;
    }

    /** Adds a menu item to a restaurant. Only the restaurant's owner or an ADMIN may do this. */
    public FoodItem create(Long restaurantId, FoodItemRequest request) {
        Restaurant restaurant = restaurantService.findById(restaurantId);
        assertCanManage(restaurant);

        FoodItem item = new FoodItem();
        item.setName(request.name());
        item.setPrice(request.price());
        item.setCategory(request.category());
        item.setAvailable(request.available());
        item.setRestaurant(restaurant);
        return foodItemRepository.save(item);
    }

    @Transactional(readOnly = true)
    public Page<FoodItem> findByRestaurant(Long restaurantId, boolean availableOnly, Pageable pageable) {
        // Validates the restaurant exists -> clean 404 instead of an empty page for a bad id.
        restaurantService.findById(restaurantId);
        return availableOnly
                ? foodItemRepository.findByRestaurant_RstidAndAvailableTrue(restaurantId, pageable)
                : foodItemRepository.findByRestaurant_Rstid(restaurantId, pageable);
    }

    @Transactional(readOnly = true)
    public FoodItem findById(Long id) {
        return foodItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Food item", id));
    }

    @Transactional(readOnly = true)
    public FoodItem findByRestaurantAndId(Long restaurantId, Long id) {
        FoodItem item = findById(id);
        if (!item.getRestaurant().getRstid().equals(restaurantId)) {
            throw new ResourceNotFoundException("Food item " + id + " not found in restaurant: " + restaurantId);
        }
        return item;
    }

    public FoodItem update(Long restaurantId, Long id, FoodItemRequest request) {
        FoodItem item = findByRestaurantAndId(restaurantId, id);
        assertCanManage(item.getRestaurant());
        item.setName(request.name());
        item.setPrice(request.price());
        item.setCategory(request.category());
        item.setAvailable(request.available());
        return foodItemRepository.save(item);
    }

    public void delete(Long restaurantId, Long id) {
        FoodItem item = findByRestaurantAndId(restaurantId, id);
        assertCanManage(item.getRestaurant());
        foodItemRepository.delete(item);
    }

    private void assertCanManage(Restaurant restaurant) {
        if (!currentUser.isAdmin() && !restaurant.getOwner().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You do not own this restaurant's menu");
        }
    }
}
