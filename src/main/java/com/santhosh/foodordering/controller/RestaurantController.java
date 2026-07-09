package com.santhosh.foodordering.controller;

import com.santhosh.foodordering.dto.request.RestaurantRequest;
import com.santhosh.foodordering.dto.response.PageResponse;
import com.santhosh.foodordering.dto.response.RestaurantResponse;
import com.santhosh.foodordering.service.RestaurantService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Restaurant Management REST Controller.
 *
 * <p>This controller demonstrates clean REST API design with proper separation of concerns:
 * - Controllers only handle HTTP concerns (routing, validation, response formatting)
 * - All business logic is delegated to services
 * - DTOs separate API contracts from database entities
 * - Authorization is handled via @PreAuthorize annotations
 * - All exceptions are handled globally by GlobalExceptionHandler
 *
 * <p>Public endpoints (no authentication required):
 * - GET /api/restaurants - browse all restaurants with pagination
 * - GET /api/restaurants/active - browse only active restaurants
 * - GET /api/restaurants/{id} - get restaurant details
 *
 * <p>Protected endpoints (authentication required):
 * - For RESTAURANT_OWNER: create own restaurants, update/delete own restaurants, manage menus
 * - For ADMIN: full access to all restaurants
 *
 * @author Food Ordering Service Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    /**
     * Constructor injection for dependency injection (better than field/setter injection).
     * Spring automatically injects RestaurantService instance.
     */
    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    /**
     * Create a new restaurant.
     *
     * <p>Only RESTAURANT_OWNER and ADMIN can create restaurants.
     * The restaurant is automatically assigned to the currently authenticated user.
     *
     * @param request {@link RestaurantRequest} with name and address
     *                Validated via @Valid annotation - violations return 400 Bad Request
     *
     * @return ResponseEntity with 201 Created status and {@link RestaurantResponse} in body
     *
     * @throws DuplicateResourceException if restaurant name already exists (handled globally)
     * @throws BadRequestException if input validation fails (handled globally)
     *
     * <p>Example Request:
     * POST /api/restaurants
     * Authorization: Bearer &lt;token&gt;
     * Content-Type: application/json
     * {
     *   "name": "Pizza Palace",
     *   "address": "123 Main Street"
     * }
     *
     * <p>Example Response (201 Created):
     * {
     *   "rstid": 1,
     *   "name": "Pizza Palace",
     *   "address": "123 Main Street",
     *   "active": true,
     *   "ownerId": 3,
     *   "ownerUsername": "restaurant_owner"
     * }
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('RESTAURANT_OWNER', 'ADMIN')")
    public ResponseEntity<RestaurantResponse> create(
            @Valid @RequestBody RestaurantRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(RestaurantResponse.from(restaurantService.create(request)));
    }

    /**
     * Browse all restaurants with pagination.
     *
     * <p>Public endpoint - no authentication required.
     * Returns all restaurants regardless of active status.
     *
     * @param pageable Spring Data pageable interface for limiting and sorting results
     *                 Default: page=0, size=10, sort=rstid (ascending)
     *                 Client can override: ?page=1&size=20&sort=name,desc
     *
     * @return {@link PageResponse} containing paginated restaurants
     *
     * <p>Example Request:
     * GET /api/restaurants?page=0&size=10&sort=name
     *
     * <p>Example Response (200 OK):
     * {
     *   "content": [
     *     { "rstid": 1, "name": "Pizza Palace", ... },
     *     { "rstid": 2, "name": "Burger King", ... }
     *   ],
     *   "page": 0,
     *   "size": 10,
     *   "totalElements": 25,
     *   "totalPages": 3,
     *   "last": false
     * }
     */
    @GetMapping
    public PageResponse<RestaurantResponse> findAll(
            @PageableDefault(size = 10, sort = "rstid") Pageable pageable) {
        return PageResponse.of(
                restaurantService.findAll(pageable),
                RestaurantResponse::from  // Method reference for mapping entity to DTO
        );
    }

    /**
     * Browse only active restaurants with pagination.
     *
     * <p>Public endpoint - no authentication required.
     * Returns only restaurants where active=true.
     *
     * @param pageable pagination parameters
     * @return {@link PageResponse} containing only active restaurants
     */
    @GetMapping("/active")
    public PageResponse<RestaurantResponse> findActive(
            @PageableDefault(size = 10, sort = "rstid") Pageable pageable) {
        return PageResponse.of(
                restaurantService.findActive(pageable),
                RestaurantResponse::from
        );
    }

    /**
     * View restaurants owned by the currently authenticated user.
     *
     * <p>Protected endpoint - requires authentication.
     * Each user sees only their own restaurants (or all if ADMIN).
     *
     * @param pageable pagination parameters
     * @return {@link PageResponse} containing user's restaurants
     *
     * <p>Authorization:
     * - RESTAURANT_OWNER sees only their own restaurants
     * - ADMIN sees all restaurants
     */
    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('RESTAURANT_OWNER', 'ADMIN')")
    public PageResponse<RestaurantResponse> findMine(
            @PageableDefault(size = 10, sort = "rstid") Pageable pageable) {
        return PageResponse.of(
                restaurantService.findMine(pageable),
                RestaurantResponse::from
        );
    }

    /**
     * Get restaurant details by ID.
     *
     * <p>Public endpoint - no authentication required.
     *
     * @param id the restaurant ID
     * @return {@link RestaurantResponse} with restaurant details
     *
     * @throws ResourceNotFoundException if restaurant with given id doesn't exist (HTTP 404)
     */
    @GetMapping("/{id}")
    public RestaurantResponse getById(@PathVariable Long id) {
        return RestaurantResponse.from(restaurantService.findById(id));
    }

    /**
     * Update an existing restaurant.
     *
     * <p>Only the restaurant owner or ADMIN can update a restaurant.
     * Authorization check happens in service layer (see {@link com.santhosh.foodordering.service.RestaurantService#assertCanManage}).
     *
     * @param id the restaurant ID
     * @param request updated restaurant details
     * @return {@link RestaurantResponse} with updated restaurant details
     *
     * @throws ResourceNotFoundException if restaurant doesn't exist (HTTP 404)
     * @throws AccessDeniedException if user is not the owner and not ADMIN (HTTP 403)
     *
     * <p>Example Request:
     * PUT /api/restaurants/1
     * Authorization: Bearer &lt;token&gt;
     * {
     *   "name": "Pizza Palace Premium",
     *   "address": "456 New Address"
     * }
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('RESTAURANT_OWNER', 'ADMIN')")
    public RestaurantResponse update(
            @PathVariable Long id,
            @Valid @RequestBody RestaurantRequest request) {
        return RestaurantResponse.from(restaurantService.update(id, request));
    }

    /**
     * Set restaurant active/inactive status.
     *
     * <p>Only restaurant owner or ADMIN can toggle status.
     *
     * @param id the restaurant ID
     * @param active true to activate, false to deactivate
     * @return {@link RestaurantResponse} with updated status
     *
     * <p>Example Request:
     * PUT /api/restaurants/1/active?active=false
     *
     * @throws AccessDeniedException if unauthorized (HTTP 403)
     * @throws ResourceNotFoundException if restaurant not found (HTTP 404)
     */
    @PutMapping("/{id}/active")
    @PreAuthorize("hasAnyRole('RESTAURANT_OWNER', 'ADMIN')")
    public RestaurantResponse setActive(
            @PathVariable Long id,
            @RequestParam boolean active) {
        return RestaurantResponse.from(restaurantService.setActive(id, active));
    }

    /**
     * Delete a restaurant.
     *
     * <p>Only restaurant owner or ADMIN can delete a restaurant.
     * Deletion cascades to food items (orphanRemoval).
     *
     * @param id the restaurant ID
     * @return ResponseEntity with 204 No Content (no body)
     *
     * @throws AccessDeniedException if unauthorized (HTTP 403)
     * @throws ResourceNotFoundException if restaurant not found (HTTP 404)
     *
     * <p>Example Request:
     * DELETE /api/restaurants/1
     * Authorization: Bearer &lt;token&gt;
     *
     * <p>Response: 204 No Content
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('RESTAURANT_OWNER', 'ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        restaurantService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

