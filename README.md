# Order & Inventory Microservice System

This project is a backend microservice system built with Spring Boot to simulate a real-world order processing flow with stock validation.

The system consists of two independent services:

- Order Service → Handles order creation via REST API  
- Inventory Service → Provides stock validation via gRPC  

---

## 🚀 Tech Stack

- Java 17  
- Spring Boot 3  
- Spring Data JPA  
- PostgreSQL  
- gRPC  
- Resilience4j (Retry & Fallback)  
- Swagger / OpenAPI  
- Docker & Docker Compose  
- JUnit 5 & Mockito  

---

## 🧠 Architecture Overview

### Order Service
- Exposes REST API  
- Communicates with Inventory Service using gRPC  
- Validates stock before creating orders  
- Implements Idempotency to prevent duplicate orders  
- Uses Swagger for API documentation  

### Inventory Service
- Exposes gRPC endpoint  
- Manages product stock  
- Handles stock validation and updates  

---

## 🔄 Flow

1. Client sends request to Order Service  
2. Order Service calls Inventory Service (gRPC)  
3. Inventory checks stock  
4. If stock is available → order is created  
5. If not → request fails  

---

## ⚡ Features

- REST-based order creation  
- gRPC inter-service communication  
- Idempotent request handling (Idempotency-Key)  
- Retry & fallback support  
- Swagger UI for API testing  
- Dockerized environment  
- Unit testing  

---

## 🐳 Running the Project

### Inventory Service

```
cd inventory-service
docker compose up --build
```

### Order Service

```
cd order-service
docker compose up --build
```

---

## 📘 API Testing (Swagger)

After starting the Order Service:

👉 http://localhost:9092/swagger-ui/index.html

You can:
- inspect endpoints  
- send requests  
- test the system end-to-end  

---

## 🧪 How to Test

### Create Order

Endpoint:
POST /orders  

Header:
Idempotency-Key: test-123  

Request Body:

```
[
  {
    "productId": "iphone-15-pro",
    "quantity": 2
  }
]
```

---

### Expected Result

- If stock is available → order is created  
- If the same request is sent again → duplicate order is NOT created  

---

### Example Response

```
[
  {
    "id": "359d1863-e5c8-44e1-b2e8-0d58857b3273",
    "productId": "iphone-15-pro",
    "quantity": 2,
    "status": "COMPLETED"
  }
]
```

---

## 🧱 Project Structure

```
grpc-ddd/
├── README.md
├── order-service/
└── inventory-service/
```

---

## 🔮 Future Improvements

- Kafka integration for asynchronous ERP-to-inventory stock updates  
- Kubernetes deployment  
- Centralized logging & monitoring  
- Improved exception handling  

---

## 🎯 Purpose

This project demonstrates:

- Microservice architecture  
- Service-to-service communication (gRPC)  
- Resilient communication (retry + fallback)  
- Idempotent API design  
- Containerized development  
- API documentation with Swagger  
- Real-world debugging and problem solving  
