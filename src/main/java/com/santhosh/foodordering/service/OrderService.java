package com.santhosh.foodordering.service;

import com.santhosh.foodordering.dto.request.OrderItemRequest;
import com.santhosh.foodordering.dto.request.OrderRequest;
import com.santhosh.foodordering.exception.BadRequestException;
import com.santhosh.foodordering.exception.ResourceNotFoundException;
import com.santhosh.foodordering.model.*;
import com.santhosh.foodordering.repo.OrderRepository;
import com.santhosh.foodordering.security.CurrentUserProvider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

@Service
@Transactional
public class OrderService {

    private static final Map<OrderStatus, Set<OrderStatus>> ALLOWED_STATUS_TRANSITIONS =
            new EnumMap<>(OrderStatus.class);

    static {
            ALLOWED_STATUS_TRANSITIONS.put(OrderStatus.PLACED,
                    EnumSet.of(OrderStatus.PAID, OrderStatus.CANCELLED));
            ALLOWED_STATUS_TRANSITIONS.put(OrderStatus.PAID,
                    EnumSet.of(OrderStatus.CONFIRMED, OrderStatus.CANCELLED));
            ALLOWED_STATUS_TRANSITIONS.put(OrderStatus.CONFIRMED,
                    EnumSet.of(OrderStatus.PREPARING));
            ALLOWED_STATUS_TRANSITIONS.put(OrderStatus.PREPARING,
                    EnumSet.of(OrderStatus.OUT_FOR_DELIVERY));
            ALLOWED_STATUS_TRANSITIONS.put(OrderStatus.OUT_FOR_DELIVERY,
                    EnumSet.of(OrderStatus.DELIVERED));
            ALLOWED_STATUS_TRANSITIONS.put(OrderStatus.DELIVERED,
                    EnumSet.noneOf(OrderStatus.class));
            ALLOWED_STATUS_TRANSITIONS.put(OrderStatus.CANCELLED,
                    EnumSet.noneOf(OrderStatus.class));
    }

    private final OrderRepository orderRepository;
    private final FoodItemService foodItemService;
    private final RestaurantService restaurantService;
    private final CurrentUserProvider currentUser;

    public OrderService(OrderRepository orderRepository, FoodItemService foodItemService,
                        RestaurantService restaurantService, CurrentUserProvider currentUser) {
        this.orderRepository = orderRepository;
        this.foodItemService = foodItemService;
        this.restaurantService = restaurantService;
        this.currentUser = currentUser;
    }

    /** A CUSTOMER places an order. Validates the restaurant is open and every item is orderable. */
    public Order placeOrder(OrderRequest request) {
        Restaurant restaurant = restaurantService.findById(request.restaurantId());
        if (!restaurant.isActive()) {
            throw new BadRequestException("Restaurant is currently closed: " + restaurant.getName());
        }

        Order order = new Order();
        order.setUser(currentUser.getUser());
        order.setRestaurant(restaurant);
        order.setStatus(OrderStatus.PLACED);
        order.setOrderDate(LocalDateTime.now());

        double total = 0;
        for (OrderItemRequest line : request.items()) {
            FoodItem food = foodItemService.findById(line.foodItemId());

            if (!food.getRestaurant().getRstid().equals(restaurant.getRstid())) {
                throw new BadRequestException(
                        "Food item " + food.getFoodid() + " does not belong to restaurant " + restaurant.getRstid());
            }
            if (!food.isAvailable()) {
                throw new BadRequestException("Food item not available: " + food.getName());
            }

            OrderItem item = new OrderItem();
            item.setFoodItem(food);
            item.setQuantity(line.quantity());
            item.setPrice(food.getPrice()); // snapshot the price at order time
            order.addItem(item);

            total += food.getPrice() * line.quantity();
        }

        order.setTotalAmount(total);
        return orderRepository.save(order); // cascades the items
    }

    @Transactional(readOnly = true)
    public Page<Order> findMine(Pageable pageable) {
        return orderRepository.findByUser_Id(currentUser.getId(), pageable);
    }

    /** Orders for a restaurant - restricted to that restaurant's owner or an ADMIN. */
    @Transactional(readOnly = true)
    public Page<Order> findByRestaurant(Long restaurantId, Pageable pageable) {
        Restaurant restaurant = restaurantService.findById(restaurantId);
        assertCanManageRestaurant(restaurant);
        return orderRepository.findByRestaurant_Rstid(restaurantId, pageable);
    }

    /** Loads an order or 404 - no access check (internal use, e.g. payment). */
    @Transactional(readOnly = true)
    public Order findEntity(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", id));
    }

    public Order markAsPaid(Order order) {
        return changeStatus(order, OrderStatus.PAID);
    }
    /** Loads an order and enforces that the caller may view it. */
    @Transactional(readOnly = true)
    public Order getById(Long id) {
        Order order = findEntity(id);
        assertCanView(order);
        return order;
    }


    public Order updateStatus(Long id, OrderStatus newStatus) {
        Order order = findEntity(id);
        assertCanManageRestaurant(order.getRestaurant());
        return changeStatus(order, newStatus);
    }
    /**
     * Restaurant accepts a paid order.
     */
    public Order acceptOrder(Long id) {
        return updateStatus(id, OrderStatus.CONFIRMED);
    }

    /**
     * Restaurant starts preparing the order.
     */
    public Order startPreparing(Long id) {
        return updateStatus(id, OrderStatus.PREPARING);
    }

    /**
     * Marks the order as out for delivery.
     */
    public Order markOutForDelivery(Long id) {
        return updateStatus(id, OrderStatus.OUT_FOR_DELIVERY);
    }

    /**
     * Restaurant marks the order as delivered.
     */
    public Order markDelivered(Long id) {
        return updateStatus(id, OrderStatus.DELIVERED);
    }

    /** A customer cancels their own order while it is still cancellable. */
    public Order cancel(Long id) {
        Order order = findEntity(id);
        if (!currentUser.isAdmin() && !order.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You can only cancel your own orders");
        }
        if (order.getStatus() != OrderStatus.PLACED && order.getStatus() != OrderStatus.PAID) {
            throw new BadRequestException("Order can no longer be cancelled (status: " + order.getStatus() + ")");
        }
        return changeStatus(order, OrderStatus.CANCELLED);
    }

    private Order changeStatus(Order order, OrderStatus newStatus) {
        assertValidTransition(order.getStatus(), newStatus);
        order.setStatus(newStatus);
        return orderRepository.save(order);
    }

    private void assertCanView(Order order) {
        if (currentUser.isAdmin()) {
            return;
        }
        Long me = currentUser.getId();
        boolean isCustomer = order.getUser().getId().equals(me);
        boolean isRestaurantOwner = order.getRestaurant().getOwner().getId().equals(me);
        if (!isCustomer && !isRestaurantOwner) {
            throw new AccessDeniedException("You are not allowed to view this order");
        }
    }

    private void assertCanManageRestaurant(Restaurant restaurant) {
        if (!currentUser.isAdmin() && !restaurant.getOwner().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You do not manage this restaurant");
        }
    }

    private void assertValidTransition(OrderStatus currentStatus, OrderStatus newStatus) {
        if (currentStatus == newStatus) {
            return;
        }

        Set<OrderStatus> allowedStatuses = ALLOWED_STATUS_TRANSITIONS.getOrDefault(
                currentStatus,
                EnumSet.noneOf(OrderStatus.class)
        );
        if (!allowedStatuses.contains(newStatus)) {
            throw new BadRequestException(
                    "Cannot change order status from " + currentStatus + " to " + newStatus);
        }
    }
}
