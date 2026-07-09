# Project Overview

# Food Ordering Service Backend

## Introduction

The Food Ordering Service Backend is a RESTful web application developed using **Spring Boot 3.3.3** and **Java 17**. It provides a secure and scalable backend for an online food ordering platform where customers can browse restaurants, order food, make payments, and track order progress while restaurant owners manage restaurants, menus, and customer orders.

The application follows a layered architecture and industry-standard design principles such as Separation of Concerns, Dependency Injection, Repository Pattern, DTO Pattern, JWT Authentication, Role-Based Authorization, Validation, and Global Exception Handling.

The project is designed as an intermediate-level backend application that demonstrates real-world backend development practices using Spring Boot.

---

# Business Problem

Traditional restaurant ordering systems often suffer from manual order processing, poor order tracking, and lack of secure user management.

This project solves these issues by providing a centralized platform where:

* Customers can order food online.
* Restaurant owners manage restaurants and menus.
* Orders are processed through a controlled lifecycle.
* Payments are securely recorded.
* User authentication and authorization are enforced using JWT.

---

# Project Objectives

The primary objectives of the project are:

* Build secure REST APIs.
* Implement role-based authorization.
* Manage restaurants and menus.
* Allow customers to place food orders.
* Process customer payments.
* Track the complete lifecycle of every order.
* Demonstrate clean software architecture using Spring Boot.

---

# User Roles

The application supports three different user roles.

## 1. Customer

Customers can:

* Register
* Login
* Browse restaurants
* Browse food menus
* Place orders
* View their own orders
* Cancel eligible orders
* Make payments
* View payment history

---

## 2. Restaurant Owner

Restaurant owners can:

* Login
* Create restaurants
* Update restaurant details
* Activate or deactivate restaurants
* Manage food items
* View restaurant orders
* Accept paid orders
* Prepare food
* Dispatch orders
* Mark orders as delivered

---

## 3. Administrator

Administrators have complete access to the system.

They can:

* Manage users
* Manage roles
* Manage restaurants
* Manage menus
* View all orders
* Access all protected APIs

---

# Technology Stack

| Component            | Technology         |
| -------------------- | ------------------ |
| Programming Language | Java 17            |
| Framework            | Spring Boot 3.3.3  |
| Security             | Spring Security    |
| Authentication       | JWT                |
| Database             | MySQL              |
| ORM                  | Hibernate          |
| Data Access          | Spring Data JPA    |
| Build Tool           | Maven              |
| Validation           | Jakarta Validation |
| Password Encryption  | BCrypt             |
| API Style            | REST               |

---

# Application Architecture

The application follows a layered architecture.

```text
                    Client

                       │

                       ▼

              REST Controllers

                       │

                       ▼

              Service Layer

                       │

                       ▼

            Repository Layer

                       │

                       ▼

                  MySQL Database
```

Each layer has a dedicated responsibility.

---

# Layer Responsibilities

## Controller Layer

Responsibilities:

* Accept HTTP requests.
* Validate request parameters.
* Call service methods.
* Return response DTOs.
* Apply authorization using @PreAuthorize.

Controllers never contain business logic.

---

## Service Layer

Responsibilities:

* Implement business rules.
* Perform validations.
* Coordinate repositories.
* Handle transactions.
* Enforce application workflow.

The Service layer contains the core business logic of the application.

---

## Repository Layer

Responsibilities:

* Perform database operations.
* Execute queries.
* Use Spring Data JPA.
* Load entities efficiently using EntityGraph where required.

Repositories never contain business logic.

---

## Database Layer

Stores all application data including:

* Users
* Roles
* Restaurants
* Food Items
* Orders
* Order Items
* Payments

---

# Project Modules

The project consists of the following functional modules.

## Authentication Module

Responsible for:

* User registration
* Login
* JWT generation
* Authentication
* Authorization

---

## User Module

Responsible for:

* User management
* Role assignment
* Profile retrieval

---

## Restaurant Module

Responsible for:

* Restaurant registration
* Restaurant updates
* Restaurant activation
* Restaurant browsing

---

## Food Item Module

Responsible for:

* Menu creation
* Menu updates
* Food availability
* Menu browsing

---

## Order Module

Responsible for:

* Order creation
* Order retrieval
* Order cancellation
* Restaurant order management

---

## Payment Module

Responsible for:

* Payment processing
* Payment retrieval
* Recording payment details
* Updating the order to the PAID state

---

# Complete Business Workflow

The following workflow represents the complete business process.

```text
Customer Registration

        │

Customer Login

        │

Browse Restaurants

        │

Browse Food Menu

        │

Place Order

        │

Order Status = PLACED

        │

Customer Payment

        │

Order Status = PAID

        │

Restaurant Accepts Order

        │

Order Status = CONFIRMED

        │

Restaurant Starts Preparing

        │

Order Status = PREPARING

        │

Restaurant Dispatches Order

        │

Order Status = OUT_FOR_DELIVERY

        │

Restaurant Delivers Order

        │

Order Status = DELIVERED
```

---

# Order Lifecycle

Every order progresses through the following lifecycle.

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

Orders may only be cancelled while in the following states:

* PLACED
* PAID

---

# Payment Workflow

The payment process follows these rules.

1. Customer places an order.
2. Order status becomes PLACED.
3. Customer submits payment.
4. Payment record is created.
5. Order status changes to PAID.
6. Restaurant owner reviews the paid order.
7. Restaurant owner accepts the order.

This design separates payment processing from restaurant order confirmation.

---

# Security Workflow

Authentication Flow

```text
Register

↓

Login

↓

JWT Generated

↓

JWT Sent in Authorization Header

↓

Spring Security Filter

↓

User Authenticated

↓

Protected API Access
```

Authorization is enforced using:

* Spring Security
* JWT Authentication
* Role-Based Access Control
* Method-Level Security using @PreAuthorize

---

# Validation Strategy

The project validates client requests using Jakarta Bean Validation.

Examples include:

* Required fields
* Email format
* Positive prices
* Positive quantities
* Non-empty names

Invalid requests return HTTP 400 responses with field-level validation messages.

---

# Exception Handling

All exceptions are handled centrally using GlobalExceptionHandler.

Supported exceptions include:

* Resource Not Found
* Duplicate Resource
* Bad Request
* Validation Errors
* Authentication Errors
* Authorization Errors
* Internal Server Errors

All error responses follow a consistent JSON structure.

---

# Database Entities

The application consists of the following entities.

* Users
* Role
* Restaurant
* FoodItem
* Order
* OrderItem
* Payment

These entities are mapped using JPA relationships.

---

# Design Patterns Used

The project implements several common software design patterns.

* Layered Architecture
* Repository Pattern
* DTO Pattern
* Dependency Injection
* Builder Pattern (JWT)
* Exception Handling Pattern

---

# Performance Considerations

The project includes several optimizations.

* Pagination
* EntityGraph for optimized fetching
* Constructor Injection
* Stateless Authentication
* Transaction Management

---

# Future Enhancements

Possible future improvements include:

* Email Notifications
* Delivery Partner Module
* Payment Gateway Integration
* Redis Caching
* Docker
* Kubernetes
* Swagger/OpenAPI
* Unit Testing
* Integration Testing
* CI/CD Pipeline
* Cloud Deployment

---

# Conclusion

The Food Ordering Service Backend demonstrates the development of a secure, scalable, and maintainable RESTful application using Spring Boot.

The project follows modern backend development practices by combining layered architecture, JWT-based authentication, role-based authorization, centralized exception handling, validation, pagination, transaction management, and a clearly defined business workflow.

It provides a realistic implementation of an online food ordering platform and serves as a strong reference project for learning Spring Boot as well as preparing for backend development interviews.
