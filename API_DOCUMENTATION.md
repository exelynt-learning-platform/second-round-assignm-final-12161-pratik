# E-Commerce API Documentation

Base URL: `http://localhost:8080/api`

## Authentication

### Register
- `POST /auth/register`
- Body:
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "Secret123"
}
```
- Response:
```json
{
  "token": "<jwt>",
  "email": "john@example.com",
  "role": "USER"
}
```

### Login
- `POST /auth/login`
- Body:
```json
{
  "email": "john@example.com",
  "password": "Secret123"
}
```

## Products

### Get all products
- `GET /products`

### Get product by ID
- `GET /products/{id}`

### Create product (ADMIN)
- `POST /products`
- Header: `Authorization: Bearer <admin-token>`
- Body:
```json
{
  "name": "Laptop",
  "description": "High performance laptop",
  "price": 999.99,
  "stockQuantity": 10,
  "imageUrl": "https://example.com/laptop.jpg"
}
```

### Update product (ADMIN)
- `PUT /products/{id}`

### Delete product (ADMIN)
- `DELETE /products/{id}`

## Cart (Authenticated)

### View cart
- `GET /cart`
- Header: `Authorization: Bearer <token>`

### Add item
- `POST /cart/items`
- Body:
```json
{
  "productId": 1,
  "quantity": 2
}
```

### Update item quantity
- `PUT /cart/items/{productId}`
- Body:
```json
{
  "quantity": 3
}
```

### Remove item
- `DELETE /cart/items/{productId}`

## Orders (Authenticated)

### Place order from cart
- `POST /orders`
- Body:
```json
{
  "shippingAddress": "221B Baker Street, London"
}
```

### List current user's orders
- `GET /orders`

### Order details
- `GET /orders/{orderId}`

### Cancel order
- `POST /orders/{orderId}/cancel`

## Error format
```json
{
  "timestamp": "2026-03-26T22:11:50.123",
  "status": 400,
  "message": "Validation failed"
}
```

