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
- JUnit / Mockito  
- Redis

---

## 🏗️ Architecture Overview

The system consists of three main services:

- Order Service  
- Inventory Service  
- Inventory Sync Service  

### 🔄 Communication Model
```text

Order Service  ──(gRPC)────────▶ Inventory Service

Inventory Sync ──(Kafka Event)─▶ Inventory Service

```
- gRPC is used for synchronous, low-latency communication.
- Kafka is used for asynchronous, event-driven inventory updates.

---

## 📦 Services

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

* REST endpoint to trigger events
* Kafka producer
* Event abstraction


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


## 🐳 Running the Project

Each service has its own Docker setup.

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
- Environment-based configuration override
- Atomic stock reservation validated under Kubernetes

### Running on Kubernetes

```bash
kubectl apply -f k8s/
```

### Verification

- Swagger UI: http://localhost:9092/swagger-ui/index.html
- Inventory Sync Health: http://localhost:9093/api/inventory-events/ping



## 📘 API Documentation

After starting the Order Service, Swagger UI is available at:
http://localhost:9092/swagger-ui/index.html

## 🧪 Testing

Unit tests are implemented using JUnit and Mockito.

Examples:

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
* Add authentication and JWT-based user context
* Improve observability with correlation IDs and tracing

## 👨‍💻 Author

Developed as a hands-on project to explore distributed systems, microservices communication patterns, event-driven architecture and backend system design.
