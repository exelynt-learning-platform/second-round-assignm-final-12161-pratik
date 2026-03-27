# Full-Stack E-Commerce App

Portfolio-ready e-commerce project with Spring Boot backend and React frontend.

## Tech Stack
- Backend: Java 8+, Spring Boot, Spring Security + JWT, Spring Data JPA, PostgreSQL, Maven
- Frontend: React, React Router, Axios, CSS

## Project Structure
- `backend/` Spring Boot APIs
- `frontend/` React UI
- `API_DOCUMENTATION.md` detailed API references
- `ecommerce-postman-collection.json` importable Postman collection

## Backend API Endpoints

### Auth
- `POST /api/auth/register`
- `POST /api/auth/login`

### Products
- `GET /api/products`
- `GET /api/products/{id}`
- `POST /api/products` (ADMIN)
- `PUT /api/products/{id}` (ADMIN)
- `DELETE /api/products/{id}` (ADMIN)

### Cart (Authenticated)
- `GET /api/cart`
- `POST /api/cart/items`
- `PUT /api/cart/items/{productId}`
- `DELETE /api/cart/items/{productId}`

### Orders (Authenticated)
- `POST /api/orders`
- `GET /api/orders`
- `GET /api/orders/{orderId}`
- `POST /api/orders/{orderId}/cancel`

## Key Features Implemented
- JWT registration/login with BCrypt password hashing
- Role model with `USER`, `ADMIN`
- Product, Cart, Order domain with proper relationships
- Stock validation + stock reduction during order placement
- Cart auto-clear after successful order creation
- Global exception handling and request validation
- Unit tests for `AuthService`, `CartService`, `OrderService`
- Protected React routes and token-based API calls
- Responsive modern UI with product cards/navbar/cart/orders

## Run with STS (Backend)
1. Open STS
2. Import backend project:
   - `File -> Import -> Maven -> Existing Maven Projects`
   - Select `eCommerce/backend`
3. Configure database in `backend/src/main/resources/application.yml`:
   - Use `backend/src/main/resources/application.properties`
   - Current values:
     - `spring.datasource.url=jdbc:postgresql://localhost:5432/postgres`
     - `spring.datasource.username=postgres`
     - `spring.datasource.password=admin`
4. Run `EcommerceApplication` as Spring Boot App

## Run Frontend
1. Open terminal in `eCommerce/frontend`
2. Install dependencies:
   - `npm install`
3. Start app:
   - `npm start`
4. Open [http://localhost:3000](http://localhost:3000)

## Demo Checklist (Interview Ready)
1. Register a new user from UI or Postman
2. Login and store JWT token
3. Login as admin (`admin@shop.com` / `Admin@123`) and create a product
4. Add products to cart as normal user
5. Place order and verify:
   - Order status is `PLACED`
   - Stock gets reduced
   - Cart is cleared
6. Cancel order and verify stock is restored

## Default Notes
- Backend runs on `http://localhost:8080`
- Frontend uses this API base URL by default
- No payment gateway is integrated (order is simulated internally)
- Default seeded admin:
  - Email: `admin@shop.com`
  - Password: `Admin@123`

