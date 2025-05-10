# Virtual Power Plant (VPP) Backend Application

`The VPP API provides endpoints to manage and analyze distributed battery data within a Virtual Power Plant system. It
enables clients to store battery metadata (e.g., name, postcode, watt capacity) and retrieve filtered statistics such as
total, average, minimum, and maximum capacities within specified ranges. This API is designed for high-performance data
ingestion and querying, with support for range filtering, validation, and error handling for real-time energy management
applications.`
------------------------

# ⚡ Feature Highlights

## 🔋 Battery Management

### 📥 Bulk Battery Registration

- Accepts a list of battery entries via a single API call.
- Validates each entry for required fields like name, postcode, and watt capacity.
- Stores data efficiently using **batch** insertion.

### 🔍 Battery Filtering & Statistics

- Retrieve batteries based on:
    - Postcode range (e.g., 1000–2000)
    - Optional wattage range (e.g., 500–1000)
- Dynamically supports filtering by one or both criteria.

### 🔡 Sorted Output

- Returns the battery names alphabetically for consistent presentation.

### 📊 Battery Statistics

- Calculates and returns:
    - **Total watt capacity** – Sum of watt capacities of all matched batteries.
    - **Average watt capacity** – Average of watt capacities for the matched batteries.
    - **Total battery count** – Number of batteries that matched the filter.
    - **Battery with highest capacity** – Name of the battery with the maximum watt capacity.
    - **Battery with lowest capacity** – Name of the battery with the minimum watt capacity.

### ✅ **_85%_** Test Coverage

- Includes both unit and integration test suites.
- Covers:
    - Battery registration success & failure scenarios.
    - Battery filtering logic with and without optional parameters.
    - Error handling, edge cases, and empty input.

### 🧾 Detailed Error Responses

- Includes invalid field names and messages in a structured format.
- Uses a unified error response object for consistency across all endpoints.
- Helpful for frontend validation and debugging.
- Ensured graceful application shutdown to:
    - Complete in-flight requests.
    - Properly close DB connections and background tasks.
    - Avoid partial writes or data corruption during termination.

## 📈 API Functionality

### 🌐 RESTful APIs

- Clean and intuitive endpoints following REST conventions.
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

## ▶️ Prerequisites to Run the Application

To run the application, you only need:

- **Docker**
- **Docker Compose**

## 🛠️ Prerequisites to Develop the Application

To successfully develop the application, ensure the following are installed and configured:

- **Java 21** (JDK 21)
- **Maven 3.9+**
- **Git** (to clone and manage the project repository)
- **PostgreSQL** (locally or remotely available)

### Optional Tools for Development & Monitoring:

- **IntelliJ (recommended IDE)** / STS / VS Code
- **Postman** or **cURL** (for API testing)
- **pgAdmin** (for managing PostgreSQL)

## 🚀 Running the Application

Before running the application using either method below, first **download or clone the repository**:

```bash
git clone git@github.com:Hafiz-SE/vpp-backend.git 
cd vpp-backend
```

### 🐳 Containerized Mode (Recommended)

Ensure Docker and Docker Compose are installed.

1. Run the following command:
   ```bash
   docker-compose up --build
   ```

*Note: This may take longer during the first run as dependencies are downloaded. However, thanks to Docker's multi-stage
build process, subsequent runs will be significantly faster.*

2. To stop the application, press Ctrl+C in the terminal or close the terminal window.

### 🖥️ Standalone Mode (Local JVM)

1. Update the following values in the file. `src/main/resources/application-dev.properties`

- `SPRING_DATASOURCE_URL` **important:  please update the table name here also**
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`

2. Run the following command in the terminal

``` 
mvn clean install
mvn clean run
```

🧪 Running Test Cases

To run the test cases, use the following command:

`mvn run verify`

This will execute all unit and integration tests defined in the project and generate a test report at
`target/site/index.html`.

## 👀 Viewing the Application

Once the application is running, you can access and interact with it through the following interfaces:

- **Base API URL**: [http://localhost:8080/api](http://localhost:8080/api)
- **Swagger UI** (API
  Documentation): [http://localhost:8080/api/swagger-ui/index.html](http://localhost:8080/api/swagger-ui/index.html)
- **OpenAPI JSON Spec**: [http://localhost:8080/api/v3/api-docs](http://localhost:8080/api/v3/api-docs)
- **Health Check & Metrics** (via Actuator):
    - Health: [http://localhost:8080/api/actuator/health](http://localhost:8080/api/actuator/health)
    - Metrics: [http://localhost:8080/api/actuator/metrics](http://localhost:8080/api/actuator/metrics)

These endpoints help validate that the application is running correctly and provide insight into its behavior and
health.

# 🧠 Architectural Decisions

## 🛢️ PostgreSQL vs Other Databases

**Decision:** Selected PostgreSQL as the primary database

**Why:**

- Strong transactional guarantees (ACID)
- Rich SQL support
- **PostGIS** support enables spatial/geolocation features.
- Ideal for write-heavy systems

## 📦 Sequential ID vs Identity ID

**Decision:** Utilized `saveAll()` to enable batch insertions with sequence-based ID generation

**Why:**

- Minimizes SQL statements for large insert operations
- Supports high concurrency
- Enhances resilience and fault tolerance

## ☕ Framework & Java Version

**Decision:** Framework: Spring Boot & Java 21

Reasons:

- Hands-on experience
- Rapid application development with production-ready defaults
- Wide community support and extensive ecosystem
- Java 21 is a Long-Term Support (LTS) release with performance, stability, and modern language enhancements
- Ensures compatibility with latest libraries and tools

## 🔄 AOP vs Filter Based Logging

**Decision:** Preferred Servlet Filter over AOP for request/response handling

**Why:**

- Filters allow access to raw input/output streams early in the request lifecycle
- Crucial for accurate logging and monitoring
- AOP lacks low-level stream access needed for this purpose

## 🧱 Layered Architecture

**Decision:** Adopted a traditional layered architecture — Controller → Service → Repository → Model

**Why:**

- Enhances modularity
- Improves testability
- Encourages adherence to SOLID principles
- Clearly separates concerns and responsibilities
- Easier to test

## 🔁 Spring Batch & Retry Mechanism

- **Chosen**: Spring Batch and Spring Retry
- **Why**:
    - **Spring Batch** was added to efficiently manage and process large volumes of data in a structured, chunk-oriented
      way
    - Enables robust scheduling, checkpointing, and transactional job management for batch tasks
    - **Spring Retry** improves system resilience by allowing retry attempts for transient failures (e.g., network
      issues, temporary DB timeouts)
    - Reduces manual error recovery and improves overall system robustness
    - Seamlessly integrates with Spring Boot and aligns with the layered architecture

## 🛠️ Improvements for Future Implementation

### 🔐 Security

- Implement authentication & authorization (e.g., JWT)
- Use Role-Based Access Control (RBAC) to manage battery registration rights

### ⚙️ Reactive Stack

- Consider switching to **Spring WebFlux + R2DBC** for better scalability in high-concurrency environments

### ☁️ Horizontal Scaling

- Deploy on **Kubernetes** or other container orchestration platforms for scalability and fault tolerance

### 🛡️ Monitoring & Observability

- Integrate tools like **New Relic**, **Prometheus**, or **Grafana** for performance tracking and log analysis

### 🧽 CRUD Enhancements

- Add support for **updating** and **deleting** battery records using `PUT` and `DELETE` endpoints

### ⚡ Caching

- Use **Redis** or **Spring Cache abstraction** to optimize repeated read operations (e.g., postcode-based filtering)

### 📍 Geospatial Postcode Mapping

- Use **Google Geocoding API** to enrich postcode data with latitude/longitude
- Integrate with **PostGIS** to enable spatial queries and data visualization

### 📤 Centralized Logging

- Stream logs to cloud-based solutions like **ELK Stack**, **Loggly**, or **Datadog** for centralized log management and
  debugging
