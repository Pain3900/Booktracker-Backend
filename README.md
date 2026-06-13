# BookTracker Backend
 **Looking for the client application?** Check out the [BookTracker Frontend Repository](https://github.com/keo0ke/BookTracker) developed in collaboration with [keo0ke](https://github.com/keo0ke).
[![Java Version](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=openjdk)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen?style=for-the-badge&logo=springboot)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Passed-blue?style=for-the-badge&logo=postgresql)](https://www.postgresql.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg?style=for-the-badge)](https://opensource.org/licenses/MIT)

A robust, production-ready RESTful API engineered with **Spring Boot 3.x** and **Java 17** to serve as the backend engine for library management and reading progression tracking. This service handles decoupled data layers, structured validation via DTOs, and deep data persistence via PostgreSQL. It is perfectly tailored to integrate with mobile clients (Android/Kotlin with Retrofit) or modern frontend web dashboards.

---

##  Features

-  **Comprehensive Library Management:** Full CRUD operations to catalog books, track metadata (ISBN, authors, titles, publisher, genre), and page counts.
-  **Granular Progress Tracking:** Dedicated tracking of individual reading sessions, monitoring exact page progress, time elapsed, and dynamic state updates.
-  **Decoupled Architecture:** Utilizes the Data Transfer Object (DTO) pattern to strictly isolate database entities from the REST API presentation layer.
-  **Conflict-Free Routing:** Explicitly designed endpoint hierarchies to eliminate path ambiguity between core books data and processing metrics (e.g., resolving previous Controller handler overlap bugs).
-  **Data Integrity & Validation:** Complete server-side logic validation ensuring page progress handles constraints naturally ($0 \le \text{Current Page} \le \text{Total Pages}$).

---

##  Tech Stack

- **Core Engine:** Java 17 & Spring Boot 3.x
- **Framework Modules:** Spring Web, Spring Data JPA
- **Database:** PostgreSQL (with automated schema management via Hibernate)
- **Build System:** Apache Maven 3.8+

---

##  Project Architecture Blueprint

The project follows a standard N-Tier industry architecture pattern to separate infrastructure, business logic, and presentation:

```text
src/main/java/com/pain3900/booktracker/
│
├── controller/    # REST Controllers exposing the API surface
├── dto/           # Data Transfer Objects for payloads (Requests/Responses)
├── entity/        # Relational models mapped directly to PostgreSQL tables
├── repository/    # Spring Data JPA interfaces for database access
└── service/       # Business logic implementations and strict data validation
```

---
##  API Endpoint Specification


All endpoints follow standard semantic HTTP conventions and use `application/json` payloads.

### 1. Book Catalog (`/api/v1/books`)

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| **GET** | `/api/v1/books` | Fetch all books inside the library catalog. |
| **GET** | `/api/v1/books/{id}` | Retrieve detailed metadata for a specific book by ID. |
| **POST** | `/api/v1/books` | Register and add a brand new book record. |
| **PUT** | `/api/v1/books/{id}` | Modify and update an existing book's details.[cite: 1, 2] |
| **DELETE** | `/api/v1/books/{id}` | Safely remove a book from the system.[cite: 1, 2] |

### 2. Reading Progression Tracking (`/api/v1/progress`)

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| **GET** | `/api/v1/progress/{bookId}` | Retrieve all recorded reading logs/sessions for a specific book.[cite: 1, 2] |
| **POST** | `/api/v1/progress` | Post a new reading session log (updates current page).[cite: 1, 2] |
| **DELETE** | `/api/v1/progress/{id}` | Delete a specific reading log entry by its ID.[cite: 1, 2] |

---

##  Getting Started & Local Deployment

### Prerequisites

Before running the application, make sure you have installed:
- **JDK 17** or higher[cite: 1, 2]
- **Maven 3.8+**[cite: 1, 2]
- **PostgreSQL 14+** database server[cite: 1, 2]

### 1. Database Initialization
Log in to your PostgreSQL instance and create the target application database[cite: 1, 2]:

```sql
CREATE DATABASE booktracker;
```
### 2. Configuration Setup
Open `src/main/resources/application.properties` (or `application.yml`) and update your connection strings and credentials:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/booktracker
spring.datasource.username=your_postgres_username
spring.datasource.password=your_postgres_password

# Schema management strategy
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```
### 3. Build & Execution
Navigate to the root directory of the project and execute the Maven commands to build and spin up the server:

```bash
# Clean project and compile executable fat JAR
mvn clean package

# Run the Spring Boot application
mvn spring-boot:run
```
The application will launch on your local host environment default port: `http://localhost:8080`

---

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
