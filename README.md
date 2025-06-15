
# grpc-ddd

A sample Java microservices project demonstrating **gRPC communication** and **Domain-Driven Design (DDD)** principles.

---

## Table of Contents

- [Overview](#overview)  
- [Features](#features)  
- [Prerequisites](#prerequisites)  
- [Getting Started](#getting-started)  
- [Running the Services](#running-the-services)  
- [Project Structure](#project-structure)  
- [How It Works](#how-it-works)  
- [Contributing](#contributing)  
- [License](#license)

---

## Overview

This project showcases a clean and modular microservices architecture implemented in Java. It uses gRPC for efficient inter-service communication and applies Domain-Driven Design principles to organize the codebase.

There are two main services:

- **Inventory Service**: Manages product stock and availability  
- **Order Service**: Handles order creation and communicates with Inventory Service to verify stock before confirming orders

---

## Features

- Layered architecture following DDD patterns (Domain, Application, Infrastructure)  
- gRPC-based communication between microservices  
- Dockerized services with Docker Compose for simplified setup  
- PostgreSQL and H2 integration for data persistence  
- Clean, maintainable, and scalable code structure

---

## Prerequisites

Before you begin, ensure you have installed:

- Java 17 or later  
- Maven 3.6 or later  
- Docker and Docker Compose

---

## Getting Started

Clone the repository:

```bash
git clone https://github.com/sebahat/grpc-ddd.git
cd grpc-ddd
```
Build each service individually by navigating to its directory and running:
For Inventory Service:
```bash
cd inventory-service
mvn clean install
```
For Order Service:
```bash
cd order-service
mvn clean install
```
To start all services and dependencies with Docker Compose, run the following command from the root grpc-ddd directory:

For Inventory Service:
```bash
cd inventory-service
docker-compose up --build
```
For Order Service:
```bash
cd order-service
docker-compose up --build
```
---

## Running the Services
- Inventory Service runs internally on port 50051 (not exposed externally) and uses PostgreSQL for data persistence.

- Order Service runs on port 9002, is the only service exposed for external access, and uses H2 in-memory database.

You can inspect and test the Order Service APIs via Swagger UI by opening an extra tab at:

http://localhost:9002/swagger-ui.html

---

## Project Structure
- inventory-service/ — Inventory microservice implementation using PostgreSQL

- order-service/ — Order microservice using H2 database that calls Inventory Service via gRPC

- proto/ — Protocol buffer (.proto) definitions for gRPC services
  
---
## How It Works

- The Order Service sends gRPC requests to the Inventory Service to check product stock availability.

- Inventory Service persists data using PostgreSQL.

- Order Service uses an H2 in-memory database for quick, lightweight persistence.

- The project follows Domain-Driven Design principles to maintain clear separation between business logic and infrastructure.

- ---
## Contributing
Contributions, bug reports, and feature requests are welcome! Feel free to fork the project and submit pull requests.

