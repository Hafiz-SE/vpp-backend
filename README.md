# ⚡ Virtual Power Plant (VPP) Backend Application

The **VPP API** provides endpoints to manage and analyze distributed battery data within a Virtual Power Plant system.
It enables clients to store battery metadata (e.g., name, postcode, watt capacity) and retrieve filtered statistics such
as total, average, minimum, and maximum capacities. This API is optimized for **high-performance data ingestion and
querying**, supporting validation, range filtering, and robust error handling for real-time energy systems.

---

## 📚 Table of Contents

1. [Feature Highlights](#-feature-highlights)
2. [API Functionality](#-api-functionality)
3. [Technology Stack](#-technology-stack)
4. [Prerequisites](#️-prerequisites)
5. [Running the Application](#-running-the-application)
6. [Running Test Cases](#-running-test-cases)
7. [Viewing the Application](#-viewing-the-application)
8. [Architectural Decisions](#-architectural-decisions)
9. [Improvements for Future Implementation](#️-improvements-for-future-implementation)

---

## ⚡ Feature Highlights

### 🔋 Battery Management

#### 📥 Bulk Battery Registration

- Accepts a list of battery entries via a single API call.
- Validates each entry for required fields like name, postcode, and watt capacity.
- Stores data efficiently using **batch** insertion.

#### 🔍 Battery Filtering & Statistics

- Retrieve batteries based on:
    - Postcode range (e.g., 1000–2000)
    - Optional wattage range (e.g., 500–1000)
- Dynamically supports filtering by one or both criteria.

#### 🔡 Sorted Output

- Returns the battery names alphabetically for consistent presentation.

#### 📊 Battery Statistics

- Calculates and returns:
    - **Total watt capacity** – Sum of watt capacities of all matched batteries.
    - **Average watt capacity** – Average of watt capacities for the matched batteries.
    - **Total battery count** – Number of batteries that matched the filter.
    - **Battery with highest capacity** – Name of the battery with the maximum watt capacity.
    - **Battery with lowest capacity** – Name of the battery with the minimum watt capacity.

#### ✅ **_85%_** Test Coverage

- Includes both unit and integration test suites.
- Covers:
    - Battery registration success & failure scenarios.
    - Battery filtering logic with and without optional parameters.
    - Error handling, edge cases, and empty input.

#### 🧾 Detailed Error Responses

- Includes invalid field names and messages in a structured format.
- Uses a unified error response object for consistency across all endpoints.
- Helpful for frontend validation and debugging.
- Ensured graceful application shutdown to:
    - Complete in-flight requests.
    - Properly close DB connections and background tasks.
    - Avoid partial writes or data corruption during termination.

## 📈 API Functionality

### 🌐 RESTful APIs

- Base URL: `/api/batteries`

#### 🔧 `POST /api/batteries`

- Accepts a JSON array of battery objects for bulk registration.
- Validates each object (name, postcode, wattCapacity).
- Saves all batteries efficiently in a single transaction.
- Responds with a list of generated battery IDs.

#### 📊 `GET /api/batteries`

- Accepts query parameters:
    - `postcodeFrom` and `postcodeTo` (required)
    - `wattageFrom` and `wattageTo` (optional)
- Returns:
    - Alphabetically sorted battery names.
    - Total and average watt capacity.
    - Total matched count.
    - Names of the highest and lowest capacity batteries.

----

## 🧰 Technology Stack

- **Programming Language**: Java 21 (LTS)
- **Framework**: Spring Boot
- **Persistence**: Spring Data JPA with Hibernate
- **Database**: PostgreSQL (with optional PostGIS extension)
- **Batch Processing**: Spring Batch
- **Retry Mechanism**: Spring Retry
- **Build Tool**: Maven
- **Testing**: JUnit 5, Mockito, Testcontainers
- **Documentation**: Springdoc OpenAPI (Swagger UI)
- **Monitoring**: Spring Boot Actuator
- **Containerization**: Docker
- **Version Control**: Git
- **Logging**: SLF4J + Logback
- **CI/CD**: GitHub Actions
- **Cloud Ready**: Dockerized

---

## 🛠️ Prerequisites

### To Run

- **Docker**
- **Docker Compose**

### To Develop

- **Java 21**
- **Maven 3.9+**
- **Git**
- **PostgreSQL**

#### Optional Tools

- IntelliJ / STS / VS Code
- Postman / cURL
- pgAdmin

---

## 🚀 Running the Application

Before running the application using either method below, first **download or clone the repository**:

```bash 
git clone git@github.com:Hafiz-SE/vpp-backend.git
cd vpp-backend
```

### 🐳 Containerized Mode (Recommended)

Ensure Docker and Docker Compose are installed.

1. Run the following command:

   `docker-compose up --build`

*Note: This may take longer during the first run as dependencies are downloaded. However, thanks to Docker's multi-stage
build process, subsequent runs will be significantly faster.*

2. To stop the application, press Ctrl+C in the terminal or close the terminal window.

### 🖥️ Standalone Mode (Local JVM)

1. Update the following values in the file. `src/main/resources/application-dev.properties`

- `SPRING_DATASOURCE_URL` **important:  please update the table name here also**
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`

2. Run the following command in the terminal

```bash
mvn clean install
mvn clean run
```

### 🧪 Running Test Cases

To run the test cases, use the following command:

`mvn run verify`

This will execute all unit and integration tests defined in the project and generate a test report at
`target/site/index.html`.

## 👀 Viewing the Application

Once the application is running, you can access and interact with it through the following interfaces:

- **Base API URL**: [http://localhost:8080/api](http://localhost:8080/api)
- **Swagger UI (API Documentation)
  **: [http://localhost:8080/api/swagger-ui/index.html](http://localhost:8080/api/swagger-ui/index.html)
- **OpenAPI JSON Spec**: [http://localhost:8080/api/v3/api-docs](http://localhost:8080/api/v3/api-docs)
- **Health Check & Metrics (via Actuator)**:
    - **Health**: [http://localhost:8080/api/actuator/health](http://localhost:8080/api/actuator/health)
    - **Metrics**: [http://localhost:8080/api/actuator/metrics](http://localhost:8080/api/actuator/metrics)

These endpoints validate that the application is running correctly and provide insights into its behavior and health.

---

# 🧠 Architectural Decisions

## 🛢️ PostgreSQL vs Other Databases

**Decision**: Selected **PostgreSQL** as the primary database.

**Why**:

- Strong transactional guarantees (ACID)
- Rich SQL capabilities
- Native support for **PostGIS** (geospatial queries)
- Ideal for write-heavy applications

---

## 📦 Sequential ID vs Identity ID

**Decision**: Used `saveAll()` with **sequence-based ID generation**.

**Why**:

- Enables batch insertions with minimal SQL overhead
- Supports high-concurrency operations
- Improves fault tolerance and resilience

---

## ☕ Framework & Java Version

**Decision**: Adopted **Spring Boot** and **Java 21 (LTS)**.

**Why**:

- Fast development with production-ready defaults
- Extensive community and plugin support
- Modern language enhancements and performance improvements
- Long-term compatibility and support
- Extensive experience

---

## 🔄 AOP vs Filter-Based Logging

**Decision**: Chose **Servlet Filter** over AOP for request/response logging.

**Why**:

- Filters provide early access to raw request and response streams
- Essential for accurate I/O logging and payload analysis
- AOP cannot intercept low-level HTTP streams

---

## 🧱 Layered Architecture

**Decision**: Followed a traditional layered pattern:  
**Controller → Service → Repository → Model**

**Why**:

- Clean separation of concerns
- Better modularity and testability
- Adheres to SOLID principles`
- Easier maintenance and refactoring
  `

---

## 🔁 Spring Batch & Retry Mechanism

**Chosen**: Spring Batch and Spring Retry

**Why**:

- **Spring Batch** enables chunk-based, transactional batch processing reducing JDBC calls
- **Spring Retry** adds automatic retries for transient failures (e.g., timeouts, connection issues)

---

## 🛠️ Improvements for Future Implementation

### 🔐 Security

- Add **JWT-based authentication** and **authorization**
- Implement **Role-Based Access Control (RBAC)**

### ⚙️ Reactive Stack

- Migrate to **Spring WebFlux** and **R2DBC** for better scalability and non-blocking I/O

### ☁️ Horizontal Scaling

- Deploy using **Kubernetes** or similar orchestration tools for auto-scaling and resilience

### 🛡️ Monitoring & Observability

- Integrate tools like **Prometheus**, **Grafana**, or **New Relic** for metrics and monitoring

### 🧽 CRUD Enhancements

- Add `PUT` and `DELETE` endpoints to allow update and delete operations on battery records

### ⚡ Caching

- Use **Redis** or Spring’s **Caching Abstraction** to speed up repetitive queries

### 📍 Geospatial Postcode Mapping

- Enrich data using **Google Geocoding API**
- Leverage **PostGIS** for spatial queries and visualization

### 📤 Centralized Logging

- Stream logs to **ELK Stack**, **Datadog**, or **Loggly** for centralized analysis and debugging
