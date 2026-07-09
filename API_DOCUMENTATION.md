# API Documentation

# Food Ordering Service Backend REST API

Version: **1.0**

Framework: **Spring Boot 3.3.3**

Java Version: **17**

Authentication: **JWT Bearer Token**

Database: **MySQL**

---

# Introduction

This document describes every REST endpoint exposed by the Food Ordering Service Backend.

Each API section contains:

* Purpose
* Endpoint
* HTTP Method
* Authorization
* Request Body
* Response Body
* Business Rules
* Validation Rules
* HTTP Status Codes

---

# Base URL

```text
http://localhost:8081
```

---

# Authentication

All protected APIs require a JWT access token.

Example:

```http
Authorization: Bearer <JWT_TOKEN>
```

---

# API Response Format

Successful requests return appropriate HTTP status codes together with JSON responses.

Example

```json
{
    "id":1,
    "name":"Restaurant Name"
}
```

---

# Error Response Format

Errors are returned using a common structure.

Example

```json
{
    "timestamp":"2026-07-02T11:30:20",
    "status":400,
    "error":"Bad Request",
    "message":"Validation failed",
    "path":"/api/orders"
}
```

---

# Module 1

# Authentication APIs

Authentication is handled using JWT.

Supported operations

* Register
* Login

---

## Register User

### Endpoint

```http
POST /api/auth/register
```

### Authorization

Not Required

---

### Description

Registers a new user in the system.

Passwords are encrypted using BCrypt before being stored.

---

### Request Body

```json
{
    "username":"john",
    "email":"john@gmail.com",
    "password":"Password@123",
    "roleName":"CUSTOMER"
}
```

---

### Successful Response

HTTP

```text
201 Created
```

Example

```json
{
    "id":5,
    "username":"john",
    "email":"john@gmail.com",
    "role":"CUSTOMER"
}
```

---

### Validation Rules

* Username is required.
* Username must be unique.
* Email is required.
* Email must be valid.
* Email must be unique.
* Password is required.
* Role must exist.

---

### Business Rules

* Password is encrypted using BCrypt.
* Duplicate usernames are rejected.
* Duplicate email addresses are rejected.
* Only valid roles can be assigned.

---

### Possible Status Codes

| Code | Meaning                          |
| ---- | -------------------------------- |
| 201  | User Registered                  |
| 400  | Validation Failed                |
| 404  | Role Not Found                   |
| 409  | Username or Email Already Exists |

---

## Login

### Endpoint

```http
POST /api/auth/login
```

### Authorization

Not Required

---

### Description

Authenticates a user and returns a JWT access token.

---

### Request Body

```json
{
    "username":"john",
    "password":"Password@123"
}
```

---

### Successful Response

```json
{
    "token":"eyJhbGciOiJIUzI1NiJ9..."
}
```

---

### Business Rules

* Username must exist.
* Password must match.
* JWT contains username and role.
* Token expiration is configured in application.properties.

---

### Status Codes

| Code | Meaning                      |
| ---- | ---------------------------- |
| 200  | Login Successful             |
| 401  | Invalid Username or Password |

---

# Module 2

# User Management APIs

User Management allows administrators to manage users while authenticated users can view their own profile.

---

## Get Current User Profile

### Endpoint

```http
GET /api/users/me
```

### Authorization

Required

Roles

* CUSTOMER
* RESTAURANT_OWNER
* ADMIN

---

### Description

Returns the currently authenticated user's profile.

---

### Successful Response

```json
{
    "id":1,
    "username":"customer",
    "email":"customer@gmail.com",
    "role":"CUSTOMER"
}
```

---

### Status Codes

| Code | Meaning      |
| ---- | ------------ |
| 200  | Success      |
| 401  | Unauthorized |

---

## Get All Users

### Endpoint

```http
GET /api/users
```

### Authorization

Required

Role

ADMIN

---

### Description

Returns a paginated list of all registered users.

---

### Query Parameters

| Parameter | Description   |
| --------- | ------------- |
| page      | Page Number   |
| size      | Page Size     |
| sort      | Sorting Field |

---

### Successful Response

```json
{
    "content":[
        {
            "id":1,
            "username":"admin"
        }
    ],
    "page":0,
    "size":20,
    "totalElements":5,
    "totalPages":1
}
```

---

### Business Rules

* Accessible only by ADMIN.
* Uses pagination.

---

### Status Codes

| Code | Meaning   |
| ---- | --------- |
| 200  | Success   |
| 403  | Forbidden |

---

## Update User Role

### Endpoint

```http
PATCH /api/users/{userId}/role
```

*(Use `PUT` instead if your controller still uses `@PutMapping`.)*

### Authorization

Required

Role

ADMIN

---

### Description

Changes the role assigned to an existing user.

---

### Request Body

```json
{
    "roleName":"RESTAURANT_OWNER"
}
```

---

### Business Rules

* Target role must exist.
* User must exist.
* Only administrators may change roles.

---

### Status Codes

| Code | Meaning                |
| ---- | ---------------------- |
| 200  | Role Updated           |
| 404  | User or Role Not Found |
| 403  | Forbidden              |

---

# Module 3

# Role Management APIs

Role APIs are primarily administrative operations.

Supported Roles

* ADMIN
* CUSTOMER
* RESTAURANT_OWNER

---

## Create Role

### Endpoint

```http
POST /api/roles
```

### Authorization

Required

Role

ADMIN

---

### Request Body

```json
{
    "roleName":"DELIVERY_PARTNER",
    "description":"Handles order deliveries"
}
```

---

### Business Rules

* Role name must be unique.
* Duplicate roles are rejected.

---

### Status Codes

| Code | Meaning        |
| ---- | -------------- |
| 201  | Role Created   |
| 409  | Duplicate Role |

---

## Get All Roles

### Endpoint

```http
GET /api/roles
```

### Authorization

Required

Role

ADMIN

---

### Description

Returns all available roles.

---

### Status Codes

| Code | Meaning |
| ---- | ------- |
| 200  | Success |

---

## Update Role

### Endpoint

```http
PUT /api/roles/{roleId}
```

### Authorization

Required

Role

ADMIN

---

### Description

Updates role information.

---

### Status Codes

| Code | Meaning        |
| ---- | -------------- |
| 200  | Updated        |
| 404  | Role Not Found |

---

## Delete Role

### Endpoint

```http
DELETE /api/roles/{roleId}
```

### Authorization

Required

Role

ADMIN

---

### Business Rules

* Roles assigned to users should not be deleted.
* System roles should be protected.

---

### Status Codes

| Code | Meaning        |
| ---- | -------------- |
| 204  | Deleted        |
| 404  | Role Not Found |
| 409  | Role In Use    |

---

# Module 4

# Restaurant Management APIs

Restaurant Management allows restaurant owners to create and manage restaurants while customers can browse available restaurants.

---

## Create Restaurant

### Endpoint

```http
POST /api/restaurants
```

### Authorization

Required

Role

* RESTAURANT_OWNER
* ADMIN

---

### Description

Creates a new restaurant.

The authenticated restaurant owner becomes the owner of the restaurant.

---

### Request Body

```json
{
    "name":"Spicy Delight",
    "address":"Hyderabad",
    "phone":"9876543210",
    "description":"Authentic Indian Cuisine"
}
```

---

### Successful Response

HTTP

```text
201 Created
```

Example

```json
{
    "restaurantId":1,
    "name":"Spicy Delight",
    "active":true
}
```

---

### Validation Rules

* Restaurant name is required.
* Address is required.
* Phone number is required.
* Restaurant name should be unique.

---

### Business Rules

* Only RESTAURANT_OWNER or ADMIN can create restaurants.
* Newly created restaurants are active by default.
* Ownership is assigned automatically.

---

### Status Codes

| Code | Meaning                   |
| ---- | ------------------------- |
| 201  | Restaurant Created        |
| 400  | Validation Failed         |
| 403  | Forbidden                 |
| 409  | Restaurant Already Exists |

---

## Get All Restaurants

### Endpoint

```http
GET /api/restaurants
```

### Authorization

Not Required

---

### Description

Returns all restaurants using pagination.

Customers can browse restaurants without authentication.

---

### Query Parameters

| Parameter | Description |
| --------- | ----------- |
| page      | Page Number |
| size      | Page Size   |
| sort      | Sort Field  |

---

### Successful Response

```json
{
    "content":[
        {
            "restaurantId":1,
            "name":"Spicy Delight",
            "active":true
        }
    ],
    "page":0,
    "size":20,
    "totalElements":10,
    "totalPages":1
}
```

---

### Business Rules

* Public API.
* Supports pagination.
* Supports sorting.

---

### Status Codes

| Code | Meaning |
| ---- | ------- |
| 200  | Success |

---

## Get Restaurant By ID

### Endpoint

```http
GET /api/restaurants/{restaurantId}
```

### Authorization

Not Required

---

### Description

Returns details of a single restaurant.

---

### Status Codes

| Code | Meaning              |
| ---- | -------------------- |
| 200  | Success              |
| 404  | Restaurant Not Found |

---

## Update Restaurant

### Endpoint

```http
PUT /api/restaurants/{restaurantId}
```

### Authorization

Required

Role

* RESTAURANT_OWNER
* ADMIN

---

### Description

Updates restaurant information.

---

### Business Rules

* Restaurant owner can update only their own restaurant.
* Administrator can update any restaurant.

---

### Status Codes

| Code | Meaning              |
| ---- | -------------------- |
| 200  | Updated Successfully |
| 403  | Forbidden            |
| 404  | Restaurant Not Found |

---

## Activate / Deactivate Restaurant

### Endpoint

```http
PATCH /api/restaurants/{restaurantId}/active
```

### Authorization

Required

Role

* RESTAURANT_OWNER
* ADMIN

---

### Description

Changes restaurant availability.

---

### Request Parameter

```text
active=true
```

or

```text
active=false
```

---

### Business Rules

Inactive restaurants:

* Cannot receive new orders.
* Continue displaying existing information.

---

### Status Codes

| Code | Meaning              |
| ---- | -------------------- |
| 200  | Restaurant Updated   |
| 403  | Forbidden            |
| 404  | Restaurant Not Found |

---

# Module 5

# Food Item Management APIs

Food Item APIs allow restaurant owners to manage menus while customers browse available food.

---

## Add Food Item

### Endpoint

```http
POST /api/restaurants/{restaurantId}/food-items
```

### Authorization

Required

Role

* RESTAURANT_OWNER
* ADMIN

---

### Description

Adds a new food item to a restaurant.

---

### Request Body

```json
{
    "name":"Chicken Biryani",
    "price":299.00,
    "category":"MAIN_COURSE",
    "available":true
}
```

---

### Successful Response

HTTP

```text
201 Created
```

---

### Validation Rules

* Name is required.
* Price must be greater than zero.
* Category is required.

---

### Business Rules

* Restaurant owner can add food only to their own restaurant.
* Administrator can add food to any restaurant.

---

### Status Codes

| Code | Meaning              |
| ---- | -------------------- |
| 201  | Created              |
| 400  | Validation Failed    |
| 403  | Forbidden            |
| 404  | Restaurant Not Found |

---

## Browse Restaurant Menu

### Endpoint

```http
GET /api/restaurants/{restaurantId}/food-items
```

### Authorization

Not Required

---

### Description

Returns the menu of a restaurant.

---

### Query Parameters

| Parameter     | Description  |
| ------------- | ------------ |
| availableOnly | true / false |
| page          | Page Number  |
| size          | Page Size    |
| sort          | Sort Field   |

---

### Example

```http
GET /api/restaurants/1/food-items?availableOnly=true
```

---

### Business Rules

* Public API.
* Supports pagination.
* Can filter only available food items.

---

### Status Codes

| Code | Meaning              |
| ---- | -------------------- |
| 200  | Success              |
| 404  | Restaurant Not Found |

---

## Get Food Item By ID

### Endpoint

```http
GET /api/restaurants/{restaurantId}/food-items/{foodItemId}
```

### Authorization

Not Required

---

### Description

Returns a specific food item.

---

### Business Rules

* Food item must belong to the specified restaurant.

---

### Status Codes

| Code | Meaning             |
| ---- | ------------------- |
| 200  | Success             |
| 404  | Food Item Not Found |

---

## Update Food Item

### Endpoint

```http
PUT /api/restaurants/{restaurantId}/food-items/{foodItemId}
```

### Authorization

Required

Role

* RESTAURANT_OWNER
* ADMIN

---

### Description

Updates food item information.

---

### Request Body

```json
{
    "name":"Special Chicken Biryani",
    "price":349.00,
    "category":"MAIN_COURSE",
    "available":true
}
```

---

### Business Rules

* Restaurant owner can update only their own menu.
* Administrator can update any menu.

---

### Status Codes

| Code | Meaning              |
| ---- | -------------------- |
| 200  | Updated Successfully |
| 403  | Forbidden            |
| 404  | Food Item Not Found  |

---

## Delete Food Item

### Endpoint

```http
DELETE /api/restaurants/{restaurantId}/food-items/{foodItemId}
```

### Authorization

Required

Role

* RESTAURANT_OWNER
* ADMIN

---

### Description

Deletes a food item from the restaurant menu.

---

### Business Rules

* Restaurant owner can delete only their own menu items.
* Administrator can delete any food item.

---

### Status Codes

| Code | Meaning              |
| ---- | -------------------- |
| 204  | Deleted Successfully |
| 403  | Forbidden            |
| 404  | Food Item Not Found  |

---

---

# Module 6

# Order Management APIs

The Order Management module handles the complete lifecycle of customer orders.

Supported operations:

* Place Order
* View My Orders
* View Order Details
* Cancel Order
* View Restaurant Orders
* Accept Order
* Prepare Order
* Dispatch Order
* Deliver Order

---

# Order Lifecycle

Every order follows the workflow below.

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

Customers can cancel an order only while it is in the **PLACED** or **PAID** state.

---

## Place Order

### Endpoint

```http
POST /api/orders
```

### Authorization

Required

Role

* CUSTOMER

---

### Description

Creates a new order for a restaurant.

---

### Request Body

```json
{
    "restaurantId":1,
    "items":[
        {
            "foodItemId":1,
            "quantity":2
        },
        {
            "foodItemId":2,
            "quantity":1
        }
    ]
}
```

---

### Successful Response

HTTP

```text
201 Created
```

Example

```json
{
    "orderId":1,
    "status":"PLACED",
    "totalAmount":675.00
}
```

---

### Business Rules

* Restaurant must be active.
* Food items must belong to the selected restaurant.
* Food items must be available.
* Price is copied into the order.
* Total amount is calculated automatically.

---

### Status Codes

| Code | Meaning                        |
| ---- | ------------------------------ |
| 201  | Order Created                  |
| 400  | Invalid Order                  |
| 404  | Restaurant/Food Item Not Found |

---

## Get My Orders

### Endpoint

```http
GET /api/orders/me
```

### Authorization

Required

Role

* CUSTOMER

---

### Description

Returns all orders placed by the currently logged-in customer.

---

### Query Parameters

| Parameter | Description   |
| --------- | ------------- |
| page      | Page Number   |
| size      | Page Size     |
| sort      | Sorting Field |

---

### Business Rules

* Returns only the authenticated customer's orders.
* Supports pagination.

---

### Status Codes

| Code | Meaning |
| ---- | ------- |
| 200  | Success |

---

## Get Order By ID

### Endpoint

```http
GET /api/orders/{orderId}
```

### Authorization

Required

Roles

* CUSTOMER
* RESTAURANT_OWNER
* ADMIN

---

### Description

Returns detailed information for a specific order.

---

### Business Rules

* Customer can view only their own orders.
* Restaurant owner can view orders belonging to their restaurant.
* Administrator can view any order.

---

### Status Codes

| Code | Meaning         |
| ---- | --------------- |
| 200  | Success         |
| 403  | Forbidden       |
| 404  | Order Not Found |

---

## Cancel Order

### Endpoint

```http
PUT /api/orders/{orderId}/cancel
```

### Authorization

Required

Role

* CUSTOMER

---

### Description

Cancels an existing order.

---

### Business Rules

Cancellation is allowed only when the order status is:

* PLACED
* PAID

Orders already accepted by the restaurant cannot be cancelled.

---

### Status Codes

| Code | Meaning                  |
| ---- | ------------------------ |
| 200  | Cancelled                |
| 400  | Cancellation Not Allowed |
| 403  | Forbidden                |

---

## Get Restaurant Orders

### Endpoint

```http
GET /api/orders/restaurants/{restaurantId}
```

*(If your controller still uses `/restaurant/{restaurantId}`, document that path instead.)*

### Authorization

Required

Roles

* RESTAURANT_OWNER
* ADMIN

---

### Description

Returns all orders placed for a restaurant.

---

### Business Rules

* Restaurant owners can view only their own restaurant's orders.
* Administrator can view any restaurant's orders.

---

### Status Codes

| Code | Meaning   |
| ---- | --------- |
| 200  | Success   |
| 403  | Forbidden |

---

## Accept Order

### Endpoint

```http
PUT /api/orders/{orderId}/accept
```

### Authorization

Required

Roles

* RESTAURANT_OWNER
* ADMIN

---

### Description

Accepts a paid order.

---

### Business Rules

* Order must be in **PAID** status.
* Changes status to **CONFIRMED**.

---

### Status Codes

| Code | Meaning              |
| ---- | -------------------- |
| 200  | Accepted             |
| 400  | Invalid Order Status |
| 403  | Forbidden            |

---

## Prepare Order

### Endpoint

```http
PUT /api/orders/{orderId}/prepare
```

### Authorization

Required

Roles

* RESTAURANT_OWNER
* ADMIN

---

### Description

Marks the order as being prepared.

---

### Business Rules

* Order must be **CONFIRMED**.
* Changes status to **PREPARING**.

---

### Status Codes

| Code | Meaning              |
| ---- | -------------------- |
| 200  | Preparing            |
| 400  | Invalid Order Status |

---

## Dispatch Order

### Endpoint

```http
PUT /api/orders/{orderId}/dispatch
```

### Authorization

Required

Roles

* RESTAURANT_OWNER
* ADMIN

---

### Description

Marks the order as out for delivery.

---

### Business Rules

* Order must be **PREPARING**.
* Changes status to **OUT_FOR_DELIVERY**.

---

### Status Codes

| Code | Meaning              |
| ---- | -------------------- |
| 200  | Dispatched           |
| 400  | Invalid Order Status |

---

## Deliver Order

### Endpoint

```http
PUT /api/orders/{orderId}/deliver
```

### Authorization

Required

Roles

* RESTAURANT_OWNER
* ADMIN

---

### Description

Marks the order as delivered.

---

### Business Rules

* Order must be **OUT_FOR_DELIVERY**.
* Changes status to **DELIVERED**.

---

### Status Codes

| Code | Meaning              |
| ---- | -------------------- |
| 200  | Delivered            |
| 400  | Invalid Order Status |

---

# Module 7

# Payment Management APIs

Payment APIs allow customers to pay for orders and retrieve payment details.

---

## Payment Workflow

```text
Place Order
      ↓
Status = PLACED

Pay Order
      ↓
Status = PAID

Restaurant Accepts
      ↓
Status = CONFIRMED
```

---

## Pay for Order

### Endpoint

```http
POST /api/payments
```

### Authorization

Required

Role

* CUSTOMER

---

### Description

Processes payment for an order.

---

### Request Body

```json
{
    "orderId":1,
    "paymentMode":"CARD"
}
```

---

### Business Rules

* Order must belong to the authenticated customer.
* Order must be in **PLACED** status.
* An order can only be paid once.
* Payment amount is always the order total.
* Successful payment changes the order status to **PAID**.

---

### Status Codes

| Code | Meaning                             |
| ---- | ----------------------------------- |
| 200  | Payment Successful                  |
| 400  | Already Paid / Invalid Order Status |
| 403  | Forbidden                           |
| 404  | Order Not Found                     |

---

## Get Payment Details

### Endpoint

```http
GET /api/payments/order/{orderId}
```

### Authorization

Required

Role

* CUSTOMER

---

### Description

Returns payment details for a specific order.

---

### Business Rules

* Customer can view only their own payment.
* Administrator can view any payment.

---

### Status Codes

| Code | Meaning           |
| ---- | ----------------- |
| 200  | Success           |
| 403  | Forbidden         |
| 404  | Payment Not Found |

---

# Common Error Responses

| HTTP Status | Description           |
| ----------- | --------------------- |
| 400         | Bad Request           |
| 401         | Unauthorized          |
| 403         | Forbidden             |
| 404         | Resource Not Found    |
| 409         | Duplicate Resource    |
| 500         | Internal Server Error |

---

# Global Business Rules

* JWT authentication is required for all protected APIs.
* Passwords are stored using BCrypt hashing.
* DTOs are used for all request and response payloads.
* Validation is performed using Jakarta Bean Validation.
* Business logic is implemented only in the Service layer.
* Controllers delegate processing to Services.
* Repository classes handle database access only.
* Global Exception Handling provides consistent error responses.
* Pagination is used where large datasets are returned.

---

# Complete API Execution Flow

```text
Authentication
│
├── Register
├── Login
│
Restaurant Management
│
├── Create Restaurant
├── Update Restaurant
│
Food Item Management
│
├── Add Food Item
├── Browse Menu
│
Order Management
│
├── Place Order
├── View Orders
├── Cancel Order
│
Payment Management
│
├── Pay for Order
│
Restaurant Order Processing
│
├── Get Restaurant Orders
├── Accept Order
├── Prepare Order
├── Dispatch Order
└── Deliver Order
```

---

# End of API Documentation

This document provides a complete technical reference for the Food Ordering Service Backend REST API and reflects the current implementation using Spring Boot 3.3.3, Java 17, MySQL, Spring Security, JWT Authentication, and the updated order and payment workflow.

