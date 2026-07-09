# INTERVIEW_GUIDE.md

# Part 1

# Complete Project Explanation

---

# Tell me about your project.

This is one of the most common questions in every Java Backend interview.

The interviewer is not asking you to explain every class.

They want to understand

* What problem your project solves
* How the system works
* Which technologies you used
* Why you chose those technologies
* How data flows through the application
* What your contribution was

The explanation below is designed to be spoken naturally during an interview.

---

# Project Introduction

"My project is a Food Ordering Service Backend developed using Spring Boot 3.3.3, Java 17, MySQL, Spring Security, JWT Authentication, Hibernate and Spring Data JPA.

It is a RESTful backend application that allows customers to browse restaurants, browse menus, place food orders, make payments and track their orders, while restaurant owners manage restaurants, food items and process customer orders.

The project follows a layered architecture with Controller, Service and Repository layers, and implements role-based security using JWT.

I also implemented centralized exception handling, validation, pagination, DTO mapping and transaction management to make the application closer to real-world enterprise applications."

---

# Why did you choose this project?

Possible Answer

"Food ordering applications involve multiple business modules such as authentication, restaurant management, menu management, order processing and payments.

Because these modules interact with one another, this project helped me understand how to build a complete backend application instead of only implementing CRUD operations."

---

# Technologies Used

If interviewer asks

"What technologies have you used?"

Answer

* Java 17
* Spring Boot 3.3.3
* Spring Security
* JWT Authentication
* Spring Data JPA
* Hibernate
* MySQL
* Maven
* Jakarta Bean Validation

---

# Why Java 17?

Possible Answer

"I used Java 17 because it is the current Long Term Support version and provides several language improvements over older Java versions.

It also works very well with Spring Boot 3.x."

---

# Why Spring Boot?

Possible Answer

"Spring Boot reduces configuration, provides embedded Tomcat, dependency management, auto-configuration and allows rapid REST API development."

---

# Why MySQL?

Possible Answer

"MySQL is a relational database suitable for transactional applications.

It provides ACID compliance, indexing, foreign key relationships and is widely used in enterprise applications."

---

# Why JWT?

Possible Answer

"I wanted a stateless authentication mechanism.

JWT allows every request to carry user identity inside the token, eliminating the need for server-side sessions."

---

# Why Spring Security?

Possible Answer

"Spring Security provides authentication, authorization, password encryption and method-level security.

Instead of implementing security manually, I used Spring Security with JWT."

---

# Explain the Architecture

Possible Answer

"My project follows Layered Architecture.

Each layer has only one responsibility.

The request first reaches the Controller.

The Controller validates the request and forwards it to the Service layer.

The Service layer contains business logic and communicates with Repository classes.

Repository classes interact with MySQL using Spring Data JPA."

---

Architecture

```text
Client

↓

Controller

↓

Service

↓

Repository

↓

MySQL
```

---

# Explain each Layer

## Controller Layer

Responsibilities

* Accept REST requests
* Validate request parameters
* Apply authorization
* Return response DTOs

Controllers do not contain business logic.

---

## Service Layer

Responsibilities

* Business logic
* Validations
* Transactions
* Workflow implementation

This is the heart of the application.

---

## Repository Layer

Responsibilities

* CRUD operations
* Custom queries
* Pagination
* EntityGraph

Repositories never contain business logic.

---

## Database

Stores

* Users
* Roles
* Restaurants
* Food Items
* Orders
* Order Items
* Payments

---

# Explain the Modules

Authentication Module

↓

User Module

↓

Restaurant Module

↓

Food Item Module

↓

Order Module

↓

Payment Module

---

# Explain Authentication

Customer registers.

↓

Password is encrypted using BCrypt.

↓

Customer logs in.

↓

Spring Security authenticates credentials.

↓

JWT Token is generated.

↓

Client stores JWT.

↓

Every protected request sends

Authorization

Bearer Token

↓

JWT Filter validates token.

↓

Request proceeds.

---

# Explain Restaurant Module

Restaurant owners create restaurants.

Restaurant owners update restaurants.

Restaurant owners activate or deactivate restaurants.

Customers browse restaurants.

---

# Explain Food Item Module

Restaurant owners add menu items.

Restaurant owners update menu items.

Customers browse menus.

Customers see only available food items.

---

# Explain Order Module

Customers select food items.

↓

Customer places order.

↓

Restaurant and menu validation occurs.

↓

Order total is calculated.

↓

Order status becomes

PLACED.

---

# Explain Payment Module

Customer submits payment.

↓

PaymentService validates

* ownership

* duplicate payment

* order status

↓

Payment record created.

↓

Order status changes

PLACED

↓

PAID

Restaurant owner has not accepted the order yet.

---

# Explain Restaurant Processing

Restaurant owner views paid orders.

↓

Accept Order

↓

CONFIRMED

↓

Prepare Order

↓

PREPARING

↓

Dispatch Order

↓

OUT_FOR_DELIVERY

↓

Deliver Order

↓

DELIVERED

---

# Why did you separate Payment and Confirmation?

Possible Interview Question

Answer

"In the original implementation payment directly confirmed the order.

That mixes two different business responsibilities.

Payment only verifies that the customer has successfully paid.

Order confirmation is a restaurant decision.

Separating them results in a cleaner business workflow and reflects how real food ordering platforms operate."

---

# Explain the Order Lifecycle

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

Customers may cancel only

* PLACED

* PAID

---

# Why explicit APIs?

Instead of

```
PUT /orders/{id}/status
```

I implemented

```
PUT /orders/{id}/accept

PUT /orders/{id}/prepare

PUT /orders/{id}/dispatch

PUT /orders/{id}/deliver
```

Reason

These endpoints describe business actions instead of exposing internal status values.

This makes the API easier to understand and safer.

---

# Explain Security

Public APIs

* Login
* Register
* Browse Restaurants
* Browse Menu

Protected APIs

Require JWT.

Authorization is enforced using

@PreAuthorize.

---

# Explain Exception Handling

The project uses GlobalExceptionHandler.

Instead of handling exceptions inside every controller, all exceptions are handled centrally.

Benefits

* Consistent responses

* Cleaner controllers

* Easier maintenance

---

# Explain Validation

The project uses Jakarta Bean Validation.

Examples

@NotBlank

@Email

@Positive

Validation occurs before entering business logic.

---

# Explain DTO Pattern

DTOs prevent exposing database entities.

Advantages

* Better security

* Cleaner APIs

* API versioning

* Flexible responses

---

# Explain Pagination

Pagination is used for

* Restaurants

* Food Items

* Orders

* Users

Benefits

* Better performance

* Reduced memory usage

* Faster responses

---

# Explain EntityGraph

EntityGraph helps fetch related entities efficiently.

Instead of executing multiple SQL queries, Hibernate loads related data in a single optimized query where appropriate.

This improves performance and avoids common lazy loading issues such as the N+1 query problem.

---

# Explain Transaction Management

The Service layer uses

@Transactional

to ensure multiple database operations either complete successfully together or roll back if an error occurs.

Read-only methods use

@Transactional(readOnly = true)

to optimize database access.

---

# Final Project Summary (2-minute version)

"My project is a secure Food Ordering Service Backend built with Spring Boot 3.3.3, Java 17 and MySQL.

The application supports Customers, Restaurant Owners and Administrators.

It implements JWT Authentication, Spring Security, DTO mapping, validation, pagination, centralized exception handling and transaction management.

The application follows a layered architecture and a realistic order lifecycle:

PLACED → PAID → CONFIRMED → PREPARING → OUT_FOR_DELIVERY → DELIVERED.

One of the major improvements I implemented was separating payment processing from restaurant confirmation and replacing generic status update APIs with explicit business endpoints such as accept, prepare, dispatch and deliver, making the API more maintainable and closer to real-world enterprise applications."

---
# INTERVIEW_GUIDE.md

# Part 2

# Complete Request Flow

---

# Introduction

In enterprise applications, understanding the request lifecycle is more important than remembering annotations.

An interviewer wants to know whether you understand how a request travels through your application.

This chapter explains every step of the request lifecycle from the client to the database and back.

---

# Complete Application Flow

```text
Client

↓

HTTP Request

↓

Spring Security Filter Chain

↓

JWT Authentication Filter

↓

Controller

↓

Service

↓

Repository

↓

Hibernate

↓

MySQL

↓

Hibernate

↓

Repository

↓

Service

↓

Controller

↓

JSON Response

↓

Client
```

This flow is followed by almost every API in the application.

---

# Flow 1

# User Login

Customer enters

Username

Password

↓

POST

```http
/api/auth/login
```

↓

Request reaches

AuthController

↓

AuthController calls

AuthService

↓

AuthService calls

AuthenticationManager

↓

Spring Security checks

Username

Password

↓

CustomUserDetailsService loads user from database

↓

Password is compared using BCryptPasswordEncoder

↓

If credentials are correct

↓

JwtService generates JWT

↓

Token returned to client

Example

```json
{
    "token":"eyJhbGc..."
}
```

Client stores JWT.

---

# Login Sequence Diagram

```text
Client

↓

AuthController

↓

AuthService

↓

AuthenticationManager

↓

CustomUserDetailsService

↓

UserRepository

↓

MySQL

↓

User Found

↓

Authentication Success

↓

JwtService

↓

JWT Generated

↓

Client
```

---

# Flow 2

# Access Protected API

Example

```http
GET /api/orders/me
```

Request contains

```http
Authorization

Bearer eyJhbGc...
```

↓

Spring Security intercepts request.

↓

JwtAuthenticationFilter executes.

↓

Extract JWT.

↓

JwtService validates token.

↓

Extract username.

↓

Load user from database.

↓

Create Authentication object.

↓

Store Authentication inside SecurityContext.

↓

Controller executes.

---

# JWT Validation Flow

```text
Client

↓

JWT Filter

↓

Extract Token

↓

Validate Signature

↓

Validate Expiration

↓

Extract Username

↓

Load User

↓

Authentication Success

↓

Controller
```

---

# Flow 3

# Place Order

Customer

↓

Browse Restaurants

↓

Browse Food Menu

↓

Select Food Items

↓

POST

```http
/api/orders
```

↓

OrderController

↓

OrderService

↓

RestaurantService

Validate Restaurant

↓

FoodItemService

Validate Food Items

↓

Calculate Total

↓

Create Order

↓

Create Order Items

↓

Save Order

↓

Save Order Items

↓

Return OrderResponse

---

# Order Creation Flow

```text
Customer

↓

OrderController

↓

OrderService

↓

RestaurantService

↓

FoodItemService

↓

OrderRepository

↓

MySQL

↓

Order Saved

↓

Response
```

---

# Internal Validations During Order Creation

Restaurant exists.

↓

Restaurant active.

↓

Food exists.

↓

Food belongs to restaurant.

↓

Food available.

↓

Quantity valid.

↓

Calculate Total.

↓

Save Order.

---

# Flow 4

# Payment Processing

Customer

↓

POST

```http
/api/payments
```

↓

PaymentController

↓

PaymentService

↓

Load Order

↓

Validate Customer Ownership

↓

Validate Order Status

↓

Check Duplicate Payment

↓

Create Payment

↓

Save Payment

↓

Order Status

PLACED

↓

PAID

↓

Return PaymentResponse

---

# Payment Sequence

```text
Customer

↓

PaymentController

↓

PaymentService

↓

OrderService

↓

OrderRepository

↓

PaymentRepository

↓

MySQL

↓

Payment Saved

↓

Order Updated

↓

Response
```

---

# Why Payment Changes Only To PAID?

Interview Question

Answer

Payment processing and restaurant confirmation are two separate business responsibilities.

Payment only confirms that the customer has successfully paid.

The restaurant owner decides whether to accept or reject the order.

Separating these responsibilities produces a cleaner workflow.

---

# Flow 5

# Restaurant Accepts Order

Restaurant Owner

↓

PUT

```http
/api/orders/{id}/accept
```

↓

OrderController

↓

OrderService

↓

Load Order

↓

Validate Ownership

↓

Validate Status

↓

PAID

↓

CONFIRMED

↓

Save

↓

Return Response

---

# Restaurant Processing

Restaurant Owner

↓

Accept

↓

CONFIRMED

↓

Prepare

↓

PREPARING

↓

Dispatch

↓

OUT_FOR_DELIVERY

↓

Deliver

↓

DELIVERED

---

# Complete Restaurant Workflow

```text
Restaurant Owner

↓

Accept Order

↓

Prepare Order

↓

Dispatch Order

↓

Deliver Order
```

---

# Flow 6

# Cancel Order

Customer

↓

PUT

```http
/api/orders/{id}/cancel
```

↓

Load Order

↓

Validate Ownership

↓

Validate Status

↓

Allowed?

PLACED

PAID

↓

YES

↓

CANCELLED

↓

Save

↓

Response

---

# Why Can't Customers Cancel Later?

Interview Question

Answer

Once the restaurant starts preparing food, resources have already been allocated.

Allowing cancellation after preparation begins could lead to operational and financial loss.

Therefore cancellation is limited to

PLACED

or

PAID

states.

---

# Complete Order Lifecycle

```text
Customer

↓

Place Order

↓

PLACED

↓

Payment

↓

PAID

↓

Restaurant Accept

↓

CONFIRMED

↓

Prepare

↓

PREPARING

↓

Dispatch

↓

OUT_FOR_DELIVERY

↓

Deliver

↓

DELIVERED
```

---

# Controller → Service → Repository Flow

Every request follows

```text
Controller

↓

Service

↓

Repository

↓

Database

↓

Repository

↓

Service

↓

Controller

↓

Client
```

Controllers never communicate directly with MySQL.

Repositories never contain business logic.

Business logic is implemented only inside Services.

---

# Why Use Services?

Interview Question

Answer

Services isolate business logic from HTTP handling.

Controllers remain lightweight while Services become reusable, testable and easier to maintain.

---

# Why Use Repositories?

Repositories provide database abstraction through Spring Data JPA.

They eliminate boilerplate JDBC code and allow custom queries, pagination and optimized fetching.

---

# Complete Backend Flow

```text
Client

↓

Security Filter

↓

JWT Validation

↓

Controller

↓

Service

↓

Repository

↓

Hibernate

↓

MySQL

↓

Hibernate

↓

Repository

↓

Service

↓

Controller

↓

JSON Response

↓

Client
```

---

# Frequently Asked Follow-up Questions

## Q. Why doesn't the Controller access the Repository directly?

Because business rules belong in the Service layer. This keeps responsibilities separated and makes the application easier to maintain and test.

---

## Q. Why does the Service layer call other Services?

For example, `OrderService` uses `RestaurantService` and `FoodItemService` to reuse validation logic instead of duplicating it. This improves consistency and reduces repeated code.

---

## Q. Why does the Repository never call another Repository?

Repositories are responsible only for database access. Coordination between multiple entities is a business concern and therefore belongs in the Service layer.

---

## Q. Why is `CurrentUserProvider` used instead of reading the SecurityContext everywhere?

It centralizes access to the authenticated user, making the code cleaner, easier to read, and easier to change if the authentication mechanism evolves.

---

# INTERVIEW_GUIDE.md

# Part 3

# Package-by-Package & Class-by-Class Explanation

---

# Introduction

This chapter explains every package and major class used in the Food Ordering Service Backend.

Instead of only describing what each class does, this guide explains:

* Why the class exists
* Why it belongs in that package
* Why it communicates with other classes
* Typical interview questions
* Recommended interview answers

---

# Complete Package Structure

```text
com.santhosh.foodordering

│
├── config
├── controller
├── dto
│     ├── request
│     └── response
├── exception
├── model
├── repo
├── security
├── service
└── FoodOrderingApplication
```

Every package has exactly one responsibility.

---

# FoodOrderingApplication

## Purpose

This is the entry point of the Spring Boot application.

Responsibilities

* Starts Embedded Tomcat
* Creates Spring Context
* Performs Component Scanning
* Initializes Spring Boot

Interview Question

### Why do we need this class?

Answer

Spring Boot applications require one main class containing

```java
@SpringBootApplication
```

This annotation combines

* @Configuration
* @EnableAutoConfiguration
* @ComponentScan

and starts the entire application.

---

# Config Package

Purpose

Contains application configuration.

Configuration classes define how Spring Boot should initialize different parts of the application.

---

# SecurityConfig

Responsibilities

* Configure Spring Security
* Register PasswordEncoder
* Register AuthenticationManager
* Configure JWT Filter
* Configure Public APIs
* Configure Protected APIs

Interview Question

### Why is SecurityConfig required?

Answer

Without SecurityConfig, Spring Boot would use the default security configuration.

We customize it to

* Disable sessions
* Configure JWT authentication
* Configure authorization
* Register custom filters

---

# DataInitializer

Responsibilities

* Create default roles
* Create default admin user
* Seed application data

Interview Question

### Why use CommandLineRunner?

Answer

CommandLineRunner executes automatically after Spring Boot starts.

It is useful for

* Initial data
* Default users
* Master data
* Testing

---

# Controller Package

Purpose

Controllers expose REST APIs.

Controllers should never contain business logic.

They

Receive Request

↓

Validate Request

↓

Call Service

↓

Return Response

---

# AuthController

Responsibilities

* Register User
* Login User

Methods

```text
register()

login()
```

Interview Question

### Why doesn't AuthController generate JWT itself?

Answer

JWT generation is business logic.

Controllers only receive requests.

Business logic belongs inside AuthService.

---

# UserController

Responsibilities

* Get Current User
* Get All Users
* Update User Role

Interview Question

### Why use /users/me?

Answer

Instead of requiring the client to send their own user ID, the backend reads the authenticated user from the JWT.

This improves both security and usability.

---

# RoleController

Responsibilities

* Create Role
* Get Roles
* Update Role
* Delete Role

Interview Question

### Why separate RoleController from UserController?

Answer

Users and Roles are different resources.

Each controller should manage only one resource.

This follows the Single Responsibility Principle.

---

# RestaurantController

Responsibilities

* Create Restaurant
* Update Restaurant
* Browse Restaurants
* Activate Restaurant

Interview Question

### Why is browsing public?

Answer

Customers should be able to browse restaurants before creating an account.

Therefore GET requests are public.

Management operations remain protected.

---

# FoodItemController

Responsibilities

* Add Food Item
* Browse Menu
* Update Food Item
* Delete Food Item

Interview Question

### Why is browsing public but updating protected?

Answer

Viewing menus should not require authentication.

Only restaurant owners should manage menus.

---

# OrderController

Responsibilities

* Place Order
* View Orders
* Cancel Order
* Accept Order
* Prepare Order
* Dispatch Order
* Deliver Order

Interview Question

### Why use separate APIs instead of

```text
PUT /status
```

Answer

Separate endpoints

```text
accept

prepare

dispatch

deliver
```

represent business actions instead of exposing internal implementation.

This improves readability and API design.

---

# PaymentController

Responsibilities

* Pay Order
* View Payment

Interview Question

### Why is payment separate from orders?

Answer

Payment processing and order management are different business domains.

Keeping them separate improves maintainability and separation of concerns.

---

# Service Package

Purpose

Contains the application's business logic.

Controllers should never contain business rules.

Services

Validate

↓

Process

↓

Call Repository

↓

Return Result

---

# AuthService

Responsibilities

* Register Users
* Authenticate Users
* Generate JWT

Interview Question

### Why use AuthenticationManager?

Answer

AuthenticationManager delegates authentication to Spring Security instead of manually validating passwords.

This reduces custom security code.

---

# UserService

Responsibilities

* Manage Users
* Update Roles
* Retrieve Users

Business Rules

* Username uniqueness
* Email uniqueness
* Role validation

---

# RoleService

Responsibilities

* CRUD operations
* Duplicate role validation

Interview Question

### Why create RoleService?

Answer

Business validation should not be inside RoleController.

Services make business logic reusable.

---

# RestaurantService

Responsibilities

* Create Restaurant
* Update Restaurant
* Validate Ownership
* Activate Restaurant

Interview Question

### Why validate ownership here?

Answer

Ownership is a business rule.

Business rules belong in Services.

---

# FoodItemService

Responsibilities

* Create Food Item
* Browse Menu
* Validate Restaurant Ownership

Interview Question

### Why call RestaurantService?

Answer

Restaurant validation already exists there.

Reusing it avoids duplicated code.

---

# OrderService

This is the most important service.

Responsibilities

* Place Order
* Validate Restaurant
* Validate Food Items
* Calculate Total
* Save Order
* Cancel Order
* Manage Order Lifecycle

Interview Question

### Why is OrderService large?

Answer

Order processing involves multiple business rules.

Examples

* Restaurant validation

* Menu validation

* Price calculation

* Order creation

* Order status changes

Therefore OrderService naturally coordinates multiple components.

---

# PaymentService

Responsibilities

* Validate Customer
* Validate Payment
* Prevent Duplicate Payment
* Save Payment
* Update Order Status

Interview Question

### Why doesn't PaymentService confirm the order?

Answer

Payment confirms only that the customer has paid.

Restaurant confirmation is a separate business decision.

Therefore PaymentService changes

PLACED

↓

PAID

Restaurant owners later change

PAID

↓

CONFIRMED

---

# Repository Package

Repositories communicate with MySQL.

They contain

* CRUD
* Queries
* Pagination

Nothing else.

Interview Question

### Why not use JDBC?

Answer

Spring Data JPA eliminates boilerplate code.

It provides

* CRUD

* Pagination

* Query Methods

* EntityGraph

* Transactions

---

# OrderRepository

Interview Question

### Why use EntityGraph?

Answer

Order contains

Restaurant

User

OrderItems

FoodItems

Without EntityGraph Hibernate may execute multiple SQL queries.

EntityGraph loads related entities efficiently.

---

# DTO Package

Purpose

Transfer data between client and server.

Interview Question

### Why DTO instead of Entity?

Answer

Entities represent database tables.

DTOs represent API contracts.

Advantages

* Hide internal fields

* Flexible APIs

* Better security

* Independent evolution

---

# Request DTOs

Receive data.

Examples

* LoginRequest

* OrderRequest

* PaymentRequest

---

# Response DTOs

Return data.

Examples

* OrderResponse

* UserResponse

* RestaurantResponse

---

# Model Package

Contains JPA entities.

Interview Question

### Why don't entities return directly?

Answer

Entities may contain

* Passwords

* Relationships

* Lazy proxies

DTOs prevent exposing internal implementation.

---

# Exception Package

Purpose

Centralized exception handling.

Interview Question

### Why GlobalExceptionHandler?

Answer

Instead of writing

try-catch

inside every controller,

all exceptions are handled in one place.

This produces

* Cleaner controllers

* Consistent JSON

* Easier maintenance

---

# Security Package

Contains all authentication logic.

Classes

* JwtService
* JwtAuthenticationFilter
* CurrentUserProvider
* CustomUserDetailsService
* RestAuthenticationEntryPoint
* RestAccessDeniedHandler

Each class has one responsibility.

---

# CurrentUserProvider

Interview Question

### Why create this class?

Answer

Instead of repeatedly writing

SecurityContextHolder...

throughout the application,

CurrentUserProvider centralizes retrieval of the authenticated user.

This makes services cleaner and easier to maintain.

---

# Summary

The project follows a clean layered architecture where every package and class has a clearly defined responsibility.

Controllers handle HTTP communication.

Services implement business logic.

Repositories interact with the database.

DTOs define the API contract.

Security classes manage authentication and authorization.

Configuration classes initialize the application.

This separation improves readability, maintainability, testing, and scalability.

---

# INTERVIEW_GUIDE.md

# Part 4

# Database Design & Entity Relationships

---

# Introduction

The Food Ordering Service Backend uses **MySQL** as its relational database and **Spring Data JPA with Hibernate** as the ORM framework.

Instead of writing SQL manually for every operation, JPA maps Java objects (Entities) to database tables.

Every Entity in the project represents one business object.

---

# Complete Database Design

The project contains seven main entities.

```text
Role
 │
 │ 1
 │
 ▼
Users
 │
 │ 1
 │
 ▼
Restaurant
 │
 │ 1
 │
 ▼
FoodItem

Users
 │
 │ 1
 │
 ▼
Order
 │
 ├───────────────┐
 │               │
 │               ▼
 │           Payment
 │
 ▼
OrderItem
 │
 ▼
FoodItem
```

---

# Database Tables

The application contains the following tables.

| Table       | Purpose                  |
| ----------- | ------------------------ |
| roles       | Stores application roles |
| users       | Stores registered users  |
| restaurants | Stores restaurants       |
| food_items  | Stores restaurant menu   |
| orders      | Stores customer orders   |
| order_items | Stores ordered items     |
| payments    | Stores payment records   |

---

# Entity 1

# Role

Purpose

Stores application roles.

Examples

```text
ADMIN

CUSTOMER

RESTAURANT_OWNER
```

Typical Fields

```java
roleId

roleName

description
```

Relationship

```text
Role

1

↓

Many

Users
```

Annotation

```java
@OneToMany(mappedBy = "role")
```

Interview Question

### Why OneToMany?

One role is assigned to many users.

Example

```text
CUSTOMER

↓

John

↓

David

↓

Alice
```

Therefore

One Role

↓

Many Users

---

# Entity 2

# Users

Purpose

Stores registered users.

Typical Fields

```java
id

username

email

password

role
```

Relationships

```text
Many Users

↓

One Role
```

```java
@ManyToOne
private Role role;
```

Interview Question

### Why ManyToOne?

Many users can share the same role.

Example

```text
Customer

↓

John

David

Alice
```

All reference the same CUSTOMER role.

---

Relationship

```text
User

1

↓

Many

Orders
```

Customer places many orders.

---

Relationship

```text
Restaurant Owner

1

↓

Many

Restaurants
```

One restaurant owner may own multiple restaurants.

---

# Entity 3

# Restaurant

Purpose

Stores restaurant information.

Typical Fields

```java
restaurantId

name

address

phone

description

active
```

Relationships

```text
Restaurant

1

↓

Many

Food Items
```

One restaurant has many menu items.

---

Relationship

```text
Restaurant

1

↓

Many

Orders
```

Customers place many orders for the same restaurant.

---

Relationship

```text
Restaurant

Many

↓

One

Owner
```

Owner is stored inside Users.

---

Interview Question

### Why store owner inside Restaurant?

Because ownership is part of the Restaurant.

Restaurant always belongs to one owner.

---

# Entity 4

# FoodItem

Purpose

Stores menu items.

Typical Fields

```java
foodId

name

price

category

available
```

Relationship

```text
Many Food Items

↓

One Restaurant
```

Annotation

```java
@ManyToOne
```

Interview Question

### Why ManyToOne?

Many food items belong to one restaurant.

---

# Entity 5

# Order

Purpose

Represents a customer's order.

Fields

```java
orderId

status

totalAmount

orderDate
```

Relationship

```text
Order

Many

↓

One Customer
```

One customer places many orders.

---

Relationship

```text
Order

Many

↓

One Restaurant
```

Restaurant receives many orders.

---

Relationship

```text
Order

1

↓

Many

Order Items
```

One order contains multiple food items.

---

Relationship

```text
Order

1

↓

1

Payment
```

One payment per order.

---

Interview Question

### Why separate Order and OrderItem?

Because one order may contain many menu items.

Example

```text
Burger

Pizza

Coke
```

Each becomes one OrderItem.

---

# Entity 6

# OrderItem

Purpose

Represents one line inside an order.

Example

```text
Burger

Quantity = 2

Price = 200
```

Another

```text
Pizza

Quantity = 1

Price = 300
```

Relationships

```text
Many Order Items

↓

One Order
```

and

```text
Many Order Items

↓

One Food Item
```

---

Interview Question

### Why store price here?

Food prices may change later.

OrderItem stores a snapshot of the price when the order was placed.

Therefore old orders remain accurate.

---

# Entity 7

# Payment

Purpose

Stores payment information.

Typical Fields

```java
paymentId

amount

paymentMode

paymentStatus

paidAt
```

Relationship

```text
One Payment

↓

One Order
```

Business Rule

One order can only have one successful payment.

---

Interview Question

### Why separate Payment table?

Payments contain payment-specific information.

Keeping them separate follows normalization and separation of concerns.

---

# Complete Relationship Diagram

```text
Role
 │
 └───────< Users
              │
      ┌───────┴─────────┐
      │                 │
Restaurants         Orders
      │                 │
      │                 │
 FoodItems         OrderItems
                        │
                        │
                    FoodItems
                        │
                        │
                    Payment
```

---

# Cascade Types

The project uses cascade operations where child entities should be managed automatically.

Example

```text
Order

↓

OrderItem
```

Saving an Order automatically saves its OrderItems.

Typical Annotation

```java
cascade = CascadeType.ALL
```

Interview Question

### Why CascadeType.ALL?

Because OrderItem should not exist without an Order.

---

# Orphan Removal

Example

```java
orphanRemoval = true
```

Meaning

If an OrderItem is removed from an Order,

Hibernate automatically deletes it from the database.

---

Interview Question

### Why orphanRemoval?

It prevents unused child records from remaining in the database.

---

# Fetch Types

Two fetch strategies exist.

## EAGER

Loads related entity immediately.

## LAZY

Loads only when needed.

Interview Question

### Which one should we prefer?

Usually

```text
LAZY
```

Reason

Better performance.

Load only required data.

---

# EntityGraph

The project uses EntityGraph where required.

Problem

Without EntityGraph

```text
Order

↓

Restaurant

↓

Customer

↓

OrderItems

↓

FoodItems
```

Hibernate may execute multiple SQL queries.

EntityGraph fetches related entities efficiently.

Benefits

* Better performance
* Fewer SQL queries
* Avoids N+1 Problem

---

# Why Use JPA Instead of JDBC?

Interview Question

Answer

JPA provides

* Object Mapping
* Automatic CRUD
* Transactions
* Relationships
* Pagination
* JPQL
* Repository Support

JDBC requires manual SQL and object mapping.

---

# Normalization

The project follows database normalization.

Examples

Roles stored separately.

Food Items stored separately.

Payments stored separately.

OrderItems stored separately.

This reduces duplication.

---

# Database Design Advantages

* Minimal redundancy
* Strong relationships
* Easy maintenance
* Better scalability
* Supports complex queries
* Easier reporting

---

# Common Interview Questions

### Why is Users → Role ManyToOne?

Many users can have the same role.

---

### Why is Restaurant → FoodItem OneToMany?

One restaurant has many menu items.

---

### Why Order → Payment OneToOne?

Each order has only one payment record.

---

### Why Order → OrderItem OneToMany?

One order contains multiple products.

---

### Why FoodItem → OrderItem ManyToOne?

The same food item can appear in many different customer orders.

---

### Why snapshot the food price in OrderItem?

To preserve historical order accuracy even if the menu price changes later.

---

### Why separate Payment from Order?

Because payment information belongs to a different business domain and contains payment-specific attributes such as payment mode, payment status, amount, and payment timestamp.

---

# INTERVIEW_GUIDE.md

# Part 5

# Security, JWT & Spring Security

---

# Introduction

Security is one of the most important aspects of any backend application.

In this project, security is implemented using:

* Spring Security
* JWT (JSON Web Token)
* BCrypt Password Encoder
* Method Level Authorization
* Stateless Authentication

Instead of storing user sessions on the server, every request carries a JWT token that identifies the authenticated user.

---

# Why Security is Needed?

Without security:

* Anyone could access APIs.
* Users could modify other users' data.
* Orders could be manipulated.
* Payments could be accessed without authorization.

Therefore authentication and authorization are essential.

---

# Authentication vs Authorization

Interview Question

### What is Authentication?

Authentication verifies **who the user is**.

Example

```text
Username

Password
```

If credentials are correct, the user is authenticated.

---

### What is Authorization?

Authorization determines **what the authenticated user is allowed to do**.

Example

Customer

↓

Cannot create restaurants.

Restaurant Owner

↓

Cannot manage users.

Administrator

↓

Can access all APIs.

---

# Why JWT?

Interview Question

### Why did you choose JWT?

Answer

JWT provides stateless authentication.

Instead of storing sessions on the server, the server sends a signed token after successful login.

The client includes this token in every request.

Benefits

* Stateless
* Scalable
* Fast
* Suitable for REST APIs
* No server-side session storage

---

# JWT Structure

A JWT consists of three parts.

```text
Header

.

Payload

.

Signature
```

Example

```text
xxxxx.yyyyy.zzzzz
```

---

# What does your JWT contain?

In this project the JWT stores:

* Username (Subject)
* Role (Custom Claim)
* Issued Time
* Expiration Time

Example Payload

```json
{
  "sub":"santhosh",
  "role":"CUSTOMER",
  "iat":1710000000,
  "exp":1710003600
}
```

---

# JWT Login Flow

```text
Client

↓

POST /login

↓

AuthController

↓

AuthService

↓

AuthenticationManager

↓

CustomUserDetailsService

↓

UserRepository

↓

MySQL

↓

Authentication Success

↓

JwtService

↓

Generate Token

↓

Return JWT

↓

Client Stores Token
```

---

# Using JWT

Every protected request sends

```http
Authorization: Bearer eyJhbGc...
```

Spring Security automatically intercepts the request.

---

# Security Filter Chain

Interview Question

### What is the Security Filter Chain?

Answer

Spring Security processes every incoming HTTP request through a chain of filters.

Each filter performs one responsibility.

Examples

* CORS
* CSRF
* Authentication
* Authorization
* Exception Handling

Your custom JWT filter is added to this chain.

---

# JwtAuthenticationFilter

Purpose

Authenticates every protected request.

Responsibilities

* Read Authorization header.
* Extract JWT.
* Validate JWT.
* Extract username.
* Load user details.
* Create Authentication object.
* Store Authentication in SecurityContext.

Flow

```text
HTTP Request

↓

Authorization Header

↓

Extract JWT

↓

JwtService.validate()

↓

Load User

↓

Authentication Object

↓

SecurityContext

↓

Controller
```

---

Interview Question

### Why not validate JWT inside the Controller?

Answer

Authentication should happen before any controller is executed.

The filter guarantees that only authenticated requests reach protected controllers.

---

# JwtService

Purpose

Handles JWT operations.

Responsibilities

* Generate Token
* Validate Token
* Extract Username
* Extract Role
* Parse Claims

Interview Question

### Why separate JwtService from JwtAuthenticationFilter?

Answer

JwtService focuses only on token operations.

JwtAuthenticationFilter focuses on request authentication.

Keeping these responsibilities separate improves maintainability and testability.

---

# CurrentUserProvider

Purpose

Provides information about the authenticated user.

Methods

* getUser()
* getId()
* isAdmin()

Interview Question

### Why create CurrentUserProvider?

Answer

Without it, every service would need to interact directly with `SecurityContextHolder`.

Centralizing this logic avoids duplication and makes services cleaner.

---

# CustomUserDetailsService

Purpose

Loads users from the database.

Responsibilities

* Find user by username.
* Convert the user into Spring Security's `UserDetails`.

Interview Question

### Why implement UserDetailsService?

Answer

Spring Security delegates user loading to a `UserDetailsService`.

By implementing it, the application authenticates users from the database instead of using in-memory users.

---

# Password Encryption

The project uses

```java
BCryptPasswordEncoder
```

Passwords are never stored as plain text.

Example

```text
Plain Password

↓

BCrypt

↓

$2a$10$...
```

---

Interview Question

### Why BCrypt?

Answer

BCrypt automatically generates a unique salt and is intentionally slow, making brute-force attacks much harder.

---

# SecurityConfig

Purpose

Central configuration for Spring Security.

Responsibilities

* Register PasswordEncoder.
* Register AuthenticationManager.
* Configure JWT filter.
* Configure authorization rules.
* Disable sessions.
* Configure exception handling.

---

Interview Question

### Why disable Session Management?

Answer

JWT is stateless.

Every request contains all the authentication information.

Server-side HTTP sessions are therefore unnecessary.

Configuration

```java
SessionCreationPolicy.STATELESS
```

---

# Why disable CSRF?

Interview Question

Answer

CSRF protection is mainly required for session-based web applications.

Since this project uses JWT in the Authorization header instead of cookies and HTTP sessions, CSRF protection is disabled.

---

# Public APIs

Accessible without authentication.

Examples

```text
POST /api/auth/register

POST /api/auth/login

GET /api/restaurants

GET /api/restaurants/{id}

GET /api/restaurants/{id}/food-items
```

---

# Protected APIs

Require JWT.

Examples

```text
POST /api/orders

POST /api/payments

PUT /api/orders/{id}/accept

PUT /api/orders/{id}/prepare

PUT /api/orders/{id}/dispatch

PUT /api/orders/{id}/deliver
```

---

# Method-Level Security

The project uses

```java
@PreAuthorize
```

Example

```java
@PreAuthorize("hasRole('ADMIN')")
```

or

```java
@PreAuthorize("hasAnyRole('ADMIN','RESTAURANT_OWNER')")
```

---

Interview Question

### Why use @PreAuthorize instead of checking roles manually?

Answer

`@PreAuthorize` keeps authorization rules declarative and centralized.

Controllers and services remain focused on business logic instead of role-checking code.

---

# RestAuthenticationEntryPoint

Purpose

Handles unauthenticated requests.

Example

Client sends request without JWT.

↓

Spring Security

↓

Returns

401 Unauthorized

instead of a default HTML login page.

---

# RestAccessDeniedHandler

Purpose

Handles authenticated users who do not have sufficient permissions.

Example

Customer

↓

Attempts to create a restaurant

↓

Returns

403 Forbidden

---

# Security Flow

```text
Client

↓

JWT Token

↓

Security Filter Chain

↓

JwtAuthenticationFilter

↓

JwtService

↓

CustomUserDetailsService

↓

SecurityContext

↓

Controller

↓

Service

↓

Repository

↓

Database
```

---

# Authentication vs Authorization Example

Customer logs in successfully.

Authentication

✔ Successful

Customer tries

```text
POST /api/restaurants
```

Authentication

✔ Passed

Authorization

✘ Failed

Response

```text
403 Forbidden
```

---

# Common Security Interview Questions

### Why JWT instead of Session?

JWT is stateless, scalable, and suitable for REST APIs.

---

### Why store role inside JWT?

It allows Spring Security to make authorization decisions without querying the database for every request. (The application may still load the user for additional validation depending on the implementation.)

---

### Why BCrypt?

Passwords are hashed securely and cannot be reversed.

---

### Why use Spring Security?

It provides authentication, authorization, password encoding, filters, and method-level security out of the box.

---

### Why create JwtAuthenticationFilter?

To authenticate every incoming request before it reaches the controller.

---

### Why use CurrentUserProvider?

To centralize access to the authenticated user and avoid repeating `SecurityContextHolder` code.

---

### Why use @PreAuthorize?

To declare authorization rules close to the business functionality while keeping role checks out of the method implementation.

---

# INTERVIEW_GUIDE.md

# Part 6

# Service Layer Deep Dive

---

# Introduction

The Service layer is the heart of the Food Ordering Service Backend.

Controllers only receive HTTP requests.

Repositories only communicate with MySQL.

The Service layer contains all business rules, validations, workflow management, and transaction handling.

This separation makes the application easier to maintain, test, and extend.

---

# Why Do We Need a Service Layer?

Interview Question

### Why not write everything inside the Controller?

Answer

Controllers should only handle HTTP requests and responses.

If business logic is written inside controllers:

* Controllers become very large.
* Logic gets duplicated.
* Testing becomes difficult.
* Code becomes tightly coupled.

The Service layer separates business logic from HTTP handling.

---

# Overall Service Flow

```text
Controller
      │
      ▼
Service
      │
      ▼
Repository
      │
      ▼
Database
```

Every request in your project follows this structure.

---

# Why Services Call Other Services

One of the strengths of your project is that services reuse each other instead of duplicating logic.

Example

```text
OrderService
      │
      ├────────► RestaurantService
      │
      └────────► FoodItemService
```

Instead of validating restaurants inside OrderService, it delegates that responsibility to RestaurantService.

---

Interview Question

### Why does OrderService call RestaurantService?

Answer

Restaurant validation already exists inside RestaurantService.

Reusing it:

* avoids duplicate code
* keeps validation consistent
* improves maintainability

---

# AuthService

Purpose

Handles authentication and registration.

Responsibilities

* Register new users
* Validate duplicate username
* Validate duplicate email
* Encrypt password
* Authenticate user
* Generate JWT

Flow

```text
Register Request

↓

Validate User

↓

Encode Password

↓

Save User

↓

Return Response
```

---

Interview Question

### Why encode the password inside AuthService?

Answer

Password encryption is business logic.

The controller should never know how passwords are stored.

---

# RestaurantService

Responsibilities

* Create restaurant
* Update restaurant
* Activate restaurant
* Find restaurant
* Validate ownership

Example

Restaurant Owner

↓

Update Restaurant

↓

RestaurantService

↓

Verify Owner

↓

Save

---

Interview Question

### Why validate ownership here?

Answer

Ownership is a business rule.

Business rules belong in the Service layer.

---

# FoodItemService

Responsibilities

* Add food item
* Update food item
* Delete food item
* Browse menu
* Validate restaurant ownership

Flow

```text
Owner

↓

FoodItemController

↓

FoodItemService

↓

RestaurantService

↓

Repository
```

---

Interview Question

### Why is browseMenu() inside FoodItemService?

Answer

Even though browsing only reads data, it still belongs to the business layer.

The service:

* verifies the restaurant exists
* applies business filters (such as `availableOnly`)
* delegates database access to the repository

This keeps controllers thin and repositories focused only on persistence.

---

# OrderService

This is the most important service in the project.

Responsibilities

* Validate restaurant
* Validate food items
* Create order
* Calculate total amount
* Save order
* Manage lifecycle
* Cancel order
* View orders

---

Order Creation Flow

```text
Customer

↓

OrderController

↓

OrderService

↓

RestaurantService

↓

FoodItemService

↓

OrderRepository

↓

Database
```

---

# Why Calculate Total Inside OrderService?

Interview Question

Answer

Clients should never send the final amount.

The server calculates the amount using current menu prices.

This prevents price manipulation.

---

# Why Snapshot Food Price?

Interview Question

Answer

Menu prices may change later.

OrderItem stores the price at the time of purchase so historical orders remain accurate.

---

# Why Validate Restaurant?

Interview Question

Answer

Before creating an order we verify:

* Restaurant exists.
* Restaurant is active.

Otherwise the order is rejected.

---

# Why Validate Food Items?

Interview Question

Answer

Each selected food item must:

* exist
* belong to the selected restaurant
* be available

This prevents invalid orders.

---

# Order Status Management

Your project uses controlled state transitions.

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

The service checks whether a requested transition is valid before updating the status.

---

Interview Question

### Why not update status directly?

Answer

Without validation, an order could incorrectly move from `PLACED` directly to `DELIVERED`.

Controlled transitions enforce the business workflow.

---

# Why Use EnumMap?

Your OrderService stores allowed transitions using:

```java
EnumMap<OrderStatus, Set<OrderStatus>>
```

Benefits

* Fast lookup
* Easy to maintain
* Centralized workflow rules
* No long if-else chains

---

Interview Question

### Why EnumMap instead of if-else?

Answer

`EnumMap` makes the workflow declarative.

Adding a new status usually requires updating only the transition map instead of modifying multiple conditional statements.

---

# PaymentService

Responsibilities

* Validate ownership
* Prevent duplicate payments
* Create payment
* Update order to PAID

---

Payment Flow

```text
Customer

↓

PaymentController

↓

PaymentService

↓

OrderService

↓

PaymentRepository

↓

Database
```

---

Interview Question

### Why doesn't PaymentService confirm the order?

Answer

Payment confirms that money has been received.

Restaurant confirmation is an operational decision.

Keeping these responsibilities separate produces a cleaner workflow.

---

# CurrentUserProvider in Services

Instead of writing:

```java
SecurityContextHolder.getContext()
```

inside every service,

your project uses:

```java
CurrentUserProvider
```

Benefits

* Cleaner services
* Less duplicate code
* Easier testing
* Centralized security access

---

Interview Question

### Why create CurrentUserProvider?

Answer

It wraps Spring Security details behind a simple API, allowing services to focus on business logic instead of framework code.

---

# Read-Only Transactions

Many service methods use:

```java
@Transactional(readOnly = true)
```

Examples

* Find Restaurant
* Browse Menu
* View Orders
* Get Payment

---

Interview Question

### Why readOnly = true?

Answer

These methods do not modify data.

Marking them as read-only allows Spring and the persistence provider to optimize transaction handling and clearly communicates intent.

---

# Why @Transactional?

Example

Placing an order:

* Save Order
* Save Order Items
* Update Relationships

All of these operations must succeed together.

If any step fails:

Everything is rolled back.

---

Interview Question

### What happens without @Transactional?

Answer

Partial data could be saved.

For example:

* Order saved
* OrderItems failed

The database would become inconsistent.

Transactions prevent this.

---

# Service Validation Strategy

Every service follows the same validation pattern.

```text
Receive Request

↓

Validate Input

↓

Validate Business Rules

↓

Load Required Entities

↓

Perform Operation

↓

Save

↓

Return Result
```

This keeps business logic consistent across the application.

---

# Why Throw Custom Exceptions?

Instead of returning null or generic runtime exceptions, your services throw:

* ResourceNotFoundException
* BadRequestException
* DuplicateResourceException

These are handled centrally by GlobalExceptionHandler to produce consistent API responses.

---

# Why Constructor Injection?

Every service uses constructor injection.

Benefits

* Dependencies are immutable.
* Easier unit testing.
* Clear required dependencies.
* Recommended by Spring.

---

Interview Question

### Why not use @Autowired on fields?

Answer

Constructor injection makes dependencies explicit, avoids hidden state, and is considered the preferred approach in modern Spring applications.

---

# Common Service Layer Interview Questions

### Why is business logic never written in repositories?

Repositories are responsible only for persistence.

Business rules belong in services.

---

### Why do services communicate with other services?

To reuse business logic and avoid duplication.

---

### Why use helper methods such as assertCanManageRestaurant()?

To centralize repeated validation logic, making the code easier to read and maintain.

---

### Why does OrderService have the most logic?

Order processing is the core business workflow and coordinates multiple entities, validations, and state transitions.

---

### Why keep services small and focused?

Each service represents a business domain.

Examples:

* AuthService → Authentication
* RestaurantService → Restaurant Management
* FoodItemService → Menu Management
* OrderService → Order Processing
* PaymentService → Payment Processing

This follows the Single Responsibility Principle.

---

