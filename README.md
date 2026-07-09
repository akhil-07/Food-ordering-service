# 🍔 Food Ordering Service Backend

A production-inspired **Food Ordering Service REST API** built using **Spring Boot 3.3.3**, **Java 17**, **MySQL**, **Spring Security**, and **JWT Authentication**.

This project provides a secure, role-based backend for an online food ordering platform where customers can browse restaurants, place orders, make payments, and track their orders while restaurant owners manage restaurants, menus, and order processing.

The project follows a layered architecture with clean separation of concerns using Controllers, Services, Repositories, DTOs, Security, Validation, and Global Exception Handling.

---

# Features

## Authentication & Authorization

* JWT-based Authentication
* Spring Security
* BCrypt Password Encryption
* Role-Based Access Control (RBAC)
* Stateless REST APIs

Supported Roles:

* ADMIN
* RESTAURANT_OWNER
* CUSTOMER

---

## Restaurant Management

Restaurant owners can:

* Create restaurants
* Update restaurant details
* Activate or deactivate restaurants
* View their restaurants

Customers can:

* Browse all active restaurants
* View restaurant details

---

## Food Item Management

Restaurant owners can:

* Add food items
* Update food items
* Delete food items
* Change food availability

Customers can:

* Browse restaurant menus
* View individual food items
* Filter available food items

---

## Order Management

Customers can:

* Place orders
* View their order history
* View order details
* Cancel eligible orders

Restaurant owners can:

* View restaurant orders
* Accept paid orders
* Start preparing orders
* Dispatch orders
* Mark orders as delivered

---

## Payment Management

Customers can:

* Pay for placed orders
* View payment details

Business Rules:

* Only **PLACED** orders can be paid.
* Successful payment changes the order status to **PAID**.
* Restaurant owners can only accept **PAID** orders.

---

## Security Features

* JWT Authentication
* Stateless Session Management
* Method-Level Authorization using `@PreAuthorize`
* Password Encryption using BCrypt
* Centralized Authentication and Authorization
* Secure REST APIs

---

## Validation

The project validates incoming requests using Jakarta Bean Validation.

Examples include:

* Required fields
* Email validation
* Password validation
* Positive quantities
* Valid prices

---

## Exception Handling

A centralized Global Exception Handler provides consistent API responses.

Supported Exceptions:

* Resource Not Found
* Duplicate Resource
* Bad Request
* Validation Errors
* Authentication Errors
* Authorization Errors
* Internal Server Errors

---

# Technology Stack

| Technology         | Version |
| ------------------ | ------- |
| Java               | 17      |
| Spring Boot        | 3.3.3   |
| Spring Security    | 6       |
| Spring Data JPA    | Latest  |
| Hibernate          | 6       |
| MySQL              | 8+      |
| JWT                | JJWT    |
| Maven              | Latest  |
| Lombok             | Latest  |
| Jakarta Validation | Latest  |

---

# Project Architecture

```
Client

        │

        ▼

Controller Layer

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

---

# Project Structure

```
src
 ├── config
 ├── controller
 ├── dto
 │    ├── request
 │    └── response
 ├── exception
 ├── model
 ├── repo
 ├── security
 ├── service
 └── FoodOrderingApplication.java
```

---

# Order Lifecycle

The application follows the complete business lifecycle below.

```
Customer

Browse Restaurants

        │

Browse Menu

        │

Place Order

        │

Status
PLACED

        │

Pay Order

        │

Status
PAID

        │

Restaurant Accepts

        │

Status
CONFIRMED

        │

Preparing Food

        │

Status
PREPARING

        │

Dispatch Order

        │

Status
OUT_FOR_DELIVERY

        │

Deliver Order

        │

Status
DELIVERED
```

Customers may cancel an order only while it is in the **PLACED** or **PAID** state.

---

# Payment Workflow

```
Customer

        │

Place Order

        │

POST /api/orders

        │

Status = PLACED

        │

POST /api/payments

        │

Status = PAID

        │

Restaurant accepts order

        │

Status = CONFIRMED
```

---

# Authentication Flow

```
Register

      │

Login

      │

JWT Token Generated

      │

Client Stores Token

      │

Authorization Header

Bearer <JWT Token>

      │

Protected APIs
```

---

# Database

The application uses **MySQL** as the relational database.

Major entities include:

* Users
* Roles
* Restaurants
* FoodItems
* Orders
* OrderItems
* Payments

Relationships are managed using Spring Data JPA and Hibernate.

---

# Security

Authentication is implemented using JWT.

Authorization is implemented using role-based access control.

Public APIs:

* Login
* Register
* Browse Restaurants
* Browse Food Menu

Protected APIs require a valid JWT token.

---

# Default Administrator

The application automatically creates the following administrator account during startup if it does not already exist.

| Username | Password  |
| -------- | --------- |
| admin    | admin@123 |

---

# Running the Project

## Prerequisites

* Java 17
* Maven
* MySQL Server
* IntelliJ IDEA (Recommended)
* Postman

---

## Configure MySQL

Create a database.

```
foodordering
```

Update the database configuration inside:

```
application.properties
```

Example:

```
spring.datasource.url=jdbc:mysql://localhost:3306/foodordering

spring.datasource.username=root

spring.datasource.password=your_password
```

---

## Build the Project

```
mvn clean install
```

---

## Run the Application

```
mvn spring-boot:run
```

or

Run the `FoodOrderingApplication` class from IntelliJ IDEA.

---

# API Testing

The project includes a complete Postman Collection covering:

* Authentication
* User Management
* Restaurant Management
* Food Item Management
* Order Management
* Payment Management
* Role Management

---

# Design Principles

The project follows several widely used software engineering principles:

* Layered Architecture
* Separation of Concerns
* Constructor-Based Dependency Injection
* DTO Pattern
* Repository Pattern
* RESTful API Design
* Role-Based Security
* Global Exception Handling
* Pagination
* Entity Graph Optimization
* Transaction Management

---

# Future Enhancements

Possible future improvements include:

* Email Notifications
* Order Tracking
* Delivery Partner Module
* Payment Gateway Integration
* Refresh Tokens
* Docker Support
* Kubernetes Deployment
* Redis Caching
* Swagger/OpenAPI Documentation
* Unit and Integration Testing
* CI/CD Pipeline
* Cloud Deployment

---

# Author

**Santhosh Putsala**

Java Backend Developer

Spring Boot | Java | MySQL | Spring Security | JWT | REST APIs
