package com.santhosh.foodordering.service;

import com.santhosh.foodordering.dto.request.FoodItemRequest;
import com.santhosh.foodordering.dto.request.PaymentRequest;
import com.santhosh.foodordering.dto.request.RoleRequest;
import com.santhosh.foodordering.exception.BadRequestException;
import com.santhosh.foodordering.exception.DuplicateResourceException;
import com.santhosh.foodordering.exception.ResourceNotFoundException;
import com.santhosh.foodordering.model.FoodItem;
import com.santhosh.foodordering.model.Order;
import com.santhosh.foodordering.model.OrderStatus;
import com.santhosh.foodordering.model.Payment;
import com.santhosh.foodordering.model.Restaurant;
import com.santhosh.foodordering.model.Role;
import com.santhosh.foodordering.model.Users;
import com.santhosh.foodordering.repo.FoodItemRepository;
import com.santhosh.foodordering.repo.OrderRepository;
import com.santhosh.foodordering.repo.PaymentRepository;
import com.santhosh.foodordering.repo.RoleRepository;
import com.santhosh.foodordering.security.CurrentUserProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServiceBehaviorTests {

    @Test
    void foodItemLookupRequiresMatchingRestaurant() {
        FoodItemRepository foodItemRepository = mock(FoodItemRepository.class);
        FoodItemService foodItemService = new FoodItemService(
                foodItemRepository,
                mock(RestaurantService.class),
                mock(CurrentUserProvider.class)
        );

        Restaurant restaurant = new Restaurant();
        restaurant.setRstid(10L);

        FoodItem item = new FoodItem();
        item.setFoodid(5L);
        item.setRestaurant(restaurant);

        when(foodItemRepository.findById(5L)).thenReturn(Optional.of(item));

        assertThrows(ResourceNotFoundException.class, () -> foodItemService.findByRestaurantAndId(99L, 5L));
    }

    @Test
    void foodItemUpdateUsesRestaurantScopedLookup() {
        FoodItemRepository foodItemRepository = mock(FoodItemRepository.class);
        CurrentUserProvider currentUser = mock(CurrentUserProvider.class);
        FoodItemService foodItemService = new FoodItemService(
                foodItemRepository,
                mock(RestaurantService.class),
                currentUser
        );

        Users owner = new Users();
        owner.setId(7L);

        Restaurant restaurant = new Restaurant();
        restaurant.setRstid(10L);
        restaurant.setOwner(owner);

        FoodItem item = new FoodItem();
        item.setFoodid(5L);
        item.setRestaurant(restaurant);

        when(foodItemRepository.findById(5L)).thenReturn(Optional.of(item));
        when(currentUser.isAdmin()).thenReturn(false);
        when(currentUser.getId()).thenReturn(7L);
        when(foodItemRepository.save(item)).thenReturn(item);

        foodItemService.update(10L, 5L, new FoodItemRequest("Dosa", 80.0, "Breakfast", true));

        verify(foodItemRepository).save(item);
    }

    @Test
    void orderStatusCannotSkipLifecycleSteps() {
        OrderRepository orderRepository = mock(OrderRepository.class);
        CurrentUserProvider currentUser = mock(CurrentUserProvider.class);
        OrderService orderService = new OrderService(
                orderRepository,
                mock(FoodItemService.class),
                mock(RestaurantService.class),
                currentUser
        );

        Users owner = new Users();
        owner.setId(1L);

        Restaurant restaurant = new Restaurant();
        restaurant.setOwner(owner);

        Order order = new Order();
        order.setOrdid(100L);
        order.setRestaurant(restaurant);
        order.setStatus(OrderStatus.PLACED);

        when(orderRepository.findById(100L)).thenReturn(Optional.of(order));
        when(currentUser.isAdmin()).thenReturn(true);

        assertThrows(BadRequestException.class,
                () -> orderService.updateStatus(100L, OrderStatus.DELIVERED));
    }

    @Test
    void paymentRequiresPlacedOrder() {
        PaymentRepository paymentRepository = mock(PaymentRepository.class);
        CurrentUserProvider currentUser = mock(CurrentUserProvider.class);
        OrderService orderService = mock(OrderService.class);
        PaymentService paymentService = new PaymentService(paymentRepository, orderService, currentUser);

        Users customer = new Users();
        customer.setId(3L);

        Order order = new Order();
        order.setOrdid(20L);
        order.setUser(customer);
        order.setStatus(OrderStatus.PREPARING);

        when(orderService.findEntity(20L)).thenReturn(order);
        when(currentUser.isAdmin()).thenReturn(false);
        when(currentUser.getId()).thenReturn(3L);
        when(paymentRepository.existsByOrder_Ordid(20L)).thenReturn(false);

        assertThrows(BadRequestException.class,
                () -> paymentService.pay(new PaymentRequest(20L, "UPI")));
    }

    @Test
    void roleUpdateRejectsDuplicateName() {
        RoleRepository roleRepository = mock(RoleRepository.class);
        RoleService roleService = new RoleService(roleRepository);

        Role role = new Role(1L, "CUSTOMER", "Customer");

        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        when(roleRepository.existsByRoleName("ADMIN")).thenReturn(true);

        assertThrows(DuplicateResourceException.class,
                () -> roleService.update(1L, new RoleRequest("ADMIN", "Admin")));
    }

    @Test
    void paymentForPlacedOrderIsSavedAndConfirmsOrder() {
        PaymentRepository paymentRepository = mock(PaymentRepository.class);
        CurrentUserProvider currentUser = mock(CurrentUserProvider.class);
        OrderService orderService = mock(OrderService.class);
        PaymentService paymentService = new PaymentService(paymentRepository, orderService, currentUser);

        Users customer = new Users();
        customer.setId(3L);

        Order order = new Order();
        order.setOrdid(20L);
        order.setUser(customer);
        order.setStatus(OrderStatus.PLACED);
        order.setTotalAmount(250.0);

        when(orderService.findEntity(20L)).thenReturn(order);
        when(currentUser.isAdmin()).thenReturn(false);
        when(currentUser.getId()).thenReturn(3L);
        when(paymentRepository.existsByOrder_Ordid(20L)).thenReturn(false);
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        paymentService.pay(new PaymentRequest(20L, "UPI"));

        verify(paymentRepository).save(any(Payment.class));
        org.junit.jupiter.api.Assertions.assertEquals(OrderStatus.CONFIRMED, order.getStatus());
    }
}
