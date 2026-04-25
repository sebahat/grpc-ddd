# 🧩 Distributed Order & Inventory System (gRPC + Kafka + DDD)

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
* Decreasing inventory
* Handling idempotent requests

Key features:

* gRPC client integration
* Idempotency handling
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

* Efficient binary protocol
* Strongly typed contracts with Protocol Buffers
* Suitable for internal service-to-service communication

Why Kafka?

* Decoupled communication
* Scalable event processing
* Supports eventual consistency

Why both?

* gRPC is used for real-time order placement flow.
* Kafka is used for asynchronous stock synchronization.


## ⚙️ Key Concepts Implemented

* Domain-Driven Design style layering
* Event-driven architecture
* Idempotent request handling
* Validation layer
* Exception handling strategy
* Retry and fallback mechanism
* Separation of concerns between application, domain, infrastructure and interface layers

---

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
[
  {
    "id": "50926899-f1e0-4603-bf42-9af0be83c5ab",
    "productId": "iphone-15",
    "quantity": 2,
    "status": "COMPLETED"
  }
]
```


## 📌 Future Improvements

* Kafka retry and Dead Letter Queue support
* Distributed tracing with OpenTelemetry
* Kubernetes deployment
* API Gateway integration
* Contract testing
* Order aggregate refactor for stronger DDD modeling

## 👨‍💻 Author

Developed as a hands-on project to explore distributed systems, microservices communication patterns, event-driven architecture and backend system design.
