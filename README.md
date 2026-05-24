# 🧩 Distributed Order & Inventory System (gRPC + Kafka + Kubernetes + DDD)

## ✨ Highlights

- Implemented Kafka Dead Letter Queue (DLQ) for failure handling
- Designed hybrid communication using gRPC and Kafka
- Applied Domain-Driven Design (DDD) principles
- Built Redis-backed idempotent order processing using Idempotency-Key
- Implemented Redis-based Kafka event deduplication using eventId
- Added version-based stale event protection for inventory sync events
- Introduced retry, fallback and circuit breaker mechanisms
- Implemented atomic stock reservation to prevent race conditions

This project demonstrates a distributed microservices architecture using Spring Boot, gRPC, and Kafka, designed with Domain-Driven Design (DDD) principles.

It simulates a real-world e-commerce backend where order processing and inventory management are handled via both synchronous (gRPC) and asynchronous (event-driven) communication.

---

## 🚀 Tech Stack

- Java 17  
- Spring Boot  
- Apache Kafka  
- gRPC  
- PostgreSQL  
- Docker / Docker Compose 
- Kubernetes 
- JUnit / Mockito  
- Redis

---

## 🏗️ Architecture Overview

The system is composed of four core services:

- Auth Service
- Order Service
- Inventory Service
- Inventory Sync Service

### 🔄 Communication Model
```text

Client ──(JWT Auth)────────────▶ Auth Service

Order Service ──(gRPC)────────▶ Inventory Service

Inventory Sync ──(Kafka Event)─▶ Inventory Service

```
- gRPC is used for synchronous, low-latency communication.
- Kafka is used for asynchronous, event-driven inventory updates.

---

## 📦 Services

### 🔐 Auth Service

Responsible for:

* User registration
* User login
* JWT token generation

Key features:

* Spring Security integration
* JWT-based authentication
* Password encryption
* Validation layer
* PostgreSQL persistence



### 🛒 Order Service

Responsible for:

* Creating orders
* Checking stock via gRPC
* Reserving stock via atomic operation
* Handling idempotent requests

Key features:

* gRPC client integration
* Redis-backed idempotency handling
* Duplicate request protection
* Exception management
* Domain-driven structure


### 📊 Inventory Service

Responsible for:

* Managing product stock
* Serving gRPC requests
* Consuming Kafka events

Key features:

* gRPC server implementation
* Kafka consumer
* Redis-based Kafka event deduplication
* Version-based stale event protection
* Validation layer
* Persistence with JPA



### 🔁 Inventory Sync Service

Responsible for:

* Publishing inventory events to Kafka

Key features:

* REST API for stock event publishing
* Swagger documentation
* Kafka producer
* Event abstraction
* Validation layer
* Actuator health checks


## 📡 Event-Driven Flow

External Request → Inventory Sync Service
                 → Kafka Topic
                 → Inventory Service (Consumer)
                 → Database Update

## 🧠 Design Decisions

Why gRPC?

* High-performance binary communication
* Strongly typed contracts using Protocol Buffers
* Ideal for synchronous, low-latency service-to-service calls

Why Kafka?

* Decoupled, event-driven communication
* Scalable and resilient message processing
* Supports resilient at-least-once event consumption
* Redis-backed event deduplication using eventId
* Version-based stale event protection for inventory synchronization

Why both?

* gRPC is used for real-time order placement flow.
* Kafka is used for asynchronous stock synchronization.


### Why Atomic Stock Reservation?

To prevent race conditions under high concurrency, stock reservation is handled atomically at the database level.

This ensures:

- No overselling
- Thread-safe updates
- Consistent inventory state

### Result

- 10 concurrent requests against stock=5  
- 5 succeeded, 5 failed  
- Final stock = 0  

Implementation can be found in the Inventory Service repository layer.


## ⚙️ Key Concepts Implemented

* Kafka Dead Letter Queue (DLQ) for failure handling
* Domain-Driven Design style layering
* Event-driven architecture
* Redis-backed request idempotency
* Redis-based Kafka event deduplication
* Version-based stale event protection
* Validation layer
* Exception handling strategy
* Retry and fallback mechanism
* Separation of concerns between application, domain, infrastructure and interface layers
* Aggregate root pattern (Order as aggregate root)
* Encapsulation of OrderItem within Order aggregate


## 🐳 Running the Project (Docker)

Kafka infrastructure is managed centrally at the project root, while each microservice manages its own runtime dependencies.

### Start shared infrastructure

```bash
docker compose up -d
```

### Auth Service

```bash
cd auth-service
docker compose up --build
```

### Inventory Service

```bash
cd inventory-service
docker compose up --build
```

### Inventory Sync Service

```bash
cd inventorysyncservice
docker compose up --build
```

### Order Service

```bash
cd order-service
docker compose up --build
```

### Service Ports

* Auth Service: localhost:9094
* Order Service: localhost:9092
* Inventory Sync Service: localhost:9093
* Inventory Service REST: localhost:8080
* Inventory Service gRPC: localhost:9090
* Kafka: localhost:29092
* Redis: localhost:6379

---
## ☸️ Kubernetes Deployment

The system is fully deployed on Kubernetes using Docker Desktop Kubernetes.

### Deployed Components
- Auth Service
- Order Service
- Inventory Service
- Inventory Sync Service
- Kafka
- Zookeeper
- Redis
- PostgreSQL (separate instance per service)

### Key Capabilities

- Service-to-service communication via gRPC
- Event-driven communication via Kafka
- Kubernetes DNS-based service discovery
- Isolated service deployment
- End-to-end stock/order flow validation
- Atomic stock reservation validated under Kubernetes

### Running on Kubernetes

```bash
kubectl apply -f k8s/
```

### Verification

- Auth Service → http://localhost:9094/swagger-ui/index.html
- Order Service → http://localhost:9092/swagger-ui/index.html
- Inventory Sync Service → http://localhost:9093/swagger-ui/index.html



## 📘 API Documentation

Swagger UI endpoints:

- Auth Service → http://localhost:9094/swagger-ui/index.html
- Order Service → http://localhost:9092/swagger-ui/index.html
- Inventory Sync Service → http://localhost:9093/swagger-ui/index.html


## 🧪 Testing

Unit tests are implemented using JUnit and Mockito.

Examples:

* AuthApplicationServiceTest
* JwtServiceTest
* OrderServiceTest
* InventoryApplicationServiceTest
* InventoryEventServiceTest

Docker-based test execution is also supported with Dockerfile.test.



## ✅ Example Order Request

```http
POST /orders
Idempotency-Key: test-123
Content-Type: application/json
```

```json
[
  {
    "productId": "iphone-15",
    "quantity": 2
  }
]
```

### Response

```json
{
  "orderId": "50926899-f1e0-4603-bf42-9af0be83c5ab",
  "status": "COMPLETED",
  "items": [
    {
      "id": "item-id",
      "productId": "iphone-15",
      "quantity": 2,
      "status": "COMPLETED"
    }
  ]
}
```

The response represents the Order aggregate, which contains OrderItems as internal entities.

## 📌 Future Improvements
* Improve observability with correlation IDs and tracing

## 👨‍💻 Author

Developed as a hands-on distributed systems project focused on microservice communication, event-driven architecture, resilience patterns, and domain-driven design.
