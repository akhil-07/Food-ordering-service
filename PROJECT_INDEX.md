# PROJECT_INDEX.md

# Food Ordering Service Backend

## Project Index

---

# Introduction

This document provides a complete index of the Food Ordering Service Backend project.

Its purpose is to help developers quickly understand the project structure, package organization, and the responsibility of each major class.

The project follows a layered architecture where each package has a single responsibility.

---

# Project Structure

```text
src
└── main
    ├── java
    │   └── com.santhosh.foodordering
    │       ├── config
    │       ├── controller
    │       ├── dto
    │       │   ├── request
    │       │   └── response
    │       ├── exception
    │       ├── model
    │       ├── repo
    │       ├── security
    │       ├── service
    │       └── FoodOrderingApplication.java
    │
    └── resources
        ├── application.properties
        └── data.sql (optional)
```

---

# Package Overview

| Package    | Responsibility                   |
| ---------- | -------------------------------- |
| config     | Application configuration        |
| controller | REST API endpoints               |
| dto        | Request and Response models      |
| exception  | Exception handling               |
| model      | JPA Entities                     |
| repo       | Spring Data JPA repositories     |
| security   | Authentication and authorization |
| service    | Business logic                   |
| resources  | Configuration files              |

---

# Main Application

## FoodOrderingApplication

Responsibilities

* Starts Spring Boot
* Enables component scanning
* Loads Spring context
* Boots the application

---

# Config Package

The configuration package initializes and configures the application.

---

## SecurityConfig

Responsibilities

* Configure Spring Security
* Register JWT filter
* Configure authorization rules
* Disable HTTP sessions
* Configure PasswordEncoder
* Configure AuthenticationManager

---

## DataInitializer

Responsibilities

* Create default roles
* Create default administrator
* Seed initial application data
* Prevent duplicate initialization

---

# Controller Package

Controllers expose REST APIs.

Controllers should never contain business logic.

---

## AuthController

Responsibilities

* User Registration
* User Login
* JWT Generation

Delegates all business logic to AuthService.

---

## UserController

Responsibilities

* Get logged-in user
* Get all users
* Update user role

---

## RoleController

Responsibilities

* Create roles
* Retrieve roles
* Update roles
* Delete roles

---

## RestaurantController

Responsibilities

* Create restaurant
* Update restaurant
* Browse restaurants
* Activate/deactivate restaurant

---

## FoodItemController

Responsibilities

* Add food item
* Browse menu
* View food item
* Update food item
* Delete food item

---

## OrderController

Responsibilities

* Place order
* Get customer orders
* Get restaurant orders
* Cancel order
* Accept order
* Prepare order
* Dispatch order
* Deliver order

---

## PaymentController

Responsibilities

* Process payment
* Retrieve payment details

---

# Service Package

Services contain business logic.

---

## AuthService

Responsibilities

* Register users
* Authenticate users
* Generate JWT

---

## UserService

Responsibilities

* User management
* Role assignment
* User retrieval

---

## RoleService

Responsibilities

* CRUD operations for roles

---

## RestaurantService

Responsibilities

* Restaurant ownership validation
* Restaurant management
* Restaurant activation
* Restaurant retrieval

---

## FoodItemService

Responsibilities

* Food item management
* Menu browsing
* Ownership validation

---

## OrderService

Responsibilities

* Place orders
* Validate food items
* Calculate total amount
* Manage order lifecycle
* Cancel orders
* Restaurant order processing

Current Order Lifecycle

```text
PLACED
↓
PAID
↓
CONFIRMED
↓
PREPARING
↓
OUT_FOR_DELIVERY
↓
DELIVERED
```

---

## PaymentService

Responsibilities

* Process customer payments
* Validate order ownership
* Prevent duplicate payments
* Create payment record
* Change order status to PAID

---

# Repository Package

Repositories interact with MySQL using Spring Data JPA.

---

## UserRepository

Purpose

Manage Users entity.

---

## RoleRepository

Purpose

Manage Roles.

---

## RestaurantRepository

Purpose

Manage Restaurant entity.

---

## FoodItemRepository

Purpose

Manage Food Items.

Supports pagination.

---

## OrderRepository

Purpose

Manage Orders.

Supports:

* Pagination
* EntityGraph optimization

---

## PaymentRepository

Purpose

Manage Payments.

Supports:

* Find payment by order
* Check duplicate payments

---

# DTO Package

DTOs transfer data between client and server.

---

## Request DTOs

Used to receive client input.

Examples

* LoginRequest
* RegisterRequest
* RestaurantRequest
* FoodItemRequest
* OrderRequest
* PaymentRequest
* RoleRequest

---

## Response DTOs

Used to return API responses.

Examples

* LoginResponse
* UserResponse
* RestaurantResponse
* FoodItemResponse
* OrderResponse
* PaymentResponse
* RoleResponse
* PageResponse

---

# Model Package

Contains JPA entities.

---

## Users

Stores user information.

---

## Role

Stores application roles.

---

## Restaurant

Stores restaurant information.

---

## FoodItem

Stores menu items.

---

## Order

Stores customer orders.

---

## OrderItem

Stores individual ordered food items.

---

## Payment

Stores payment transactions.

---

# Security Package

Responsible for authentication and authorization.

---

## JwtService

Responsibilities

* Generate JWT
* Validate JWT
* Extract username
* Extract role

---

## JwtAuthenticationFilter

Responsibilities

* Read Authorization header
* Validate JWT
* Authenticate user

---

## CurrentUserProvider

Responsibilities

* Return logged-in user
* Return logged-in user ID
* Return user role

---

## CustomUserDetailsService

Responsibilities

* Load users from database
* Create Spring Security UserDetails

---

## RestAuthenticationEntryPoint

Responsibilities

Return JSON response for HTTP 401 Unauthorized.

---

## RestAccessDeniedHandler

Responsibilities

Return JSON response for HTTP 403 Forbidden.

---

# Exception Package

Centralized exception handling.

---

## GlobalExceptionHandler

Responsibilities

* Handle validation errors
* Handle business exceptions
* Build consistent API responses

---

## ApiError

Represents the standard error response.

---

## Custom Exceptions

Examples

* ResourceNotFoundException
* DuplicateResourceException
* BadRequestException

---

# Resources

## application.properties

Contains

* MySQL configuration
* JWT secret
* JWT expiration
* Hibernate settings
* Server configuration

---

# API Summary

| Module         | Main Responsibility   |
| -------------- | --------------------- |
| Authentication | Register & Login      |
| Users          | User Management       |
| Roles          | Role Management       |
| Restaurants    | Restaurant Management |
| Food Items     | Menu Management       |
| Orders         | Order Processing      |
| Payments       | Payment Processing    |

---

# Complete Request Flow

```text
Client
   │
   ▼
Controller
   │
   ▼
Service
   │
   ▼
Repository
   │
   ▼
MySQL
```

---

# Dependency Flow

```text
Controller
      ↓
Service
      ↓
Repository
      ↓
Database
```

Controllers never communicate directly with repositories.

Repositories never contain business logic.

Business rules are implemented only in the Service layer.

---

# Coding Standards

The project follows these conventions:

* Constructor-based Dependency Injection
* DTO Pattern
* Repository Pattern
* Layered Architecture
* RESTful API Design
* Global Exception Handling
* Pagination
* Bean Validation
* Transaction Management
* EntityGraph Optimization

---

# Conclusion

This project is organized using a clean and scalable package structure.

Each package has a clearly defined responsibility, making the codebase easier to understand, maintain, and extend. The layered architecture, combined with Spring Boot best practices, ensures that business logic, persistence, security, and API handling remain well separated, resulting in a maintainable enterprise-style backend application.
