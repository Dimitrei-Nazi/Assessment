Document Access Control System

This is a Spring Boot application designed to manage document access control through a permissions model.

## Features

* Create, read, and manage documents with content and file type
* Assign per-user permissions (READ, WRITE) to documents
* Enforce access control using the `X-User` request header
* Admin override support (via special `X-User: admin`)
* Swagger UI for API exploration
* In-memory H2 database for demo/testing

## Project Structure
* src
* - config # OpenAPI and global interceptors
* - controller # REST endpoints
* - dto # Request/response DTOs
* - enums # Values for Permissions
* - model # Entities and embedded types
* - repository # JPA repositories
* - service # Business logic
* - validation # Custom validators (e.g., UniqueUsername)
* - ProgresssoftApplication.java

## Running the Application

### Prerequisites
* Java 17+
* Maven

### Run locally

```bash
mvn spring-boot:run

```

# Access:

Swagger UI: http://localhost:8080/swagger-ui.html

H2 Console: http://localhost:8080/h2-console

JDBC URL: jdbc:h2:mem:testdb

# Header-Based Authorization
All endpoints require a X-User header:

| X-User Value | Behavior                                     |
|--------------|----------------------------------------------|
| admin        | Full access to all endpoints                 |
|  username    | Must have explicit permissions on documents  | 

Example header:
X-User: user1

# Validation Rules
* name, content, fileType: @NotBlank
* accessibleUsers: Must contain unique, non-blank usernames
* Requests are rejected with 400 Bad Request for validation failures

# Example: Create Document
```bash
POST /documents
X-User: admin

{
    "name": "Quarterly Report",
    "content": "Confidential...",
    "fileType": "pdf",
    "accessibleUsers": [
        { "username": "alice", "permission": "READ" },
        { "username": "bob", "permission": "WRITE" }
    ]
}
```

# Permission Enforcement
* Permissions are enforced via a request interceptor.

* GET /documents: requires READ permission

* GET /documents/{id}: requires READ permission

* POST /documents: requires admin

* POST /documents/{id}/grant: requires WRITE or admin

* POST /documents/access-check: requested permission or admin

* DELETE /documents/{id}: requires DELETE permission

# Testing
Basic test coverage is provided for:

* Validation logic
* Permission checking
* Custom annotations

To run tests:
```bash
mvn test
```

# Javadoc Documentation
This project includes Javadoc comments for all major classes and services.

How to Generate Javadoc (Maven)
Make sure you have Maven installed, then run:

```bash
mvn javadoc:javadoc
```
This will generate the documentation in the following directory:
```bash
target/site/apidocs/
```
How to View
Open the generated documentation by opening this file in your browser:
```bash
target/site/apidocs/index.html
```
Contents
The Javadoc covers:

* Controllers
* Services
* Security components
* Configuration
* Validation logic
