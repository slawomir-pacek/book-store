README.md

## Book Store API

<p align="center"> <strong>REST API for an online bookstore built with Java, Spring

Boot, Spring Security, MySQL, Docker and AWS.</strong> </p>

<p align="center"> <img src="https://img.shields.io/badge/Java-17-orange? logo=openjdk" alt="Java 17"> <img src="https://img.shields.io/badge/Spring%20Boot-3.5.4-brightgreen? logo=springboot" alt="Spring Boot"> <img src="https://img.shields.io/badge/Spring%20Security-JWT-green? logo=springsecurity" alt="Spring Security"> <img src="https://img.shields.io/badge/MySQL-8-blue?logo=mysql" alt="MySQL"> <img src="https://img.shields.io/badge/Docker-Containerized-blue?logo=docker" alt="Docker"> <img src="https://img.shields.io/badge/AWS-Deployed-orange? [URL 🔗](https://img.shields.io/badge/Docker-Containerized-blue?logo=docker)

logo=amazonaws" alt="AWS"> </p> [URL 🔗](https://img.shields.io/badge/AWS-Deployed-orange?logo=amazonaws)

## About the Project

Book Store API

The project was created as a practical Java backend application to go beyond a simple CRUD implementation and work with technologies and concepts commonly used in commercial backend projects.

The application provides:

- user registration and authentication,

- JWT-based authorization,

- role-based access control,

- book and category management,

- book searching and pagination,

- shopping cart functionality,

- database migrations,

- automated tests,

- OpenAPI / Swagger documentation,

is a backend REST API for an online bookstore.


- Docker containerization,

- AWS deployment.

The project is divided into separate application layers:

Controller

↓

Service

↓

Repository

↓

Database

Security is handled independently through Spring Security and a JWT authentication filter.

## Live API / Swagger

The application is deployed on AWS EC2 and documented with Swagger UI.

## Swagger UI

## [Open Swagger UI](http://ec2-51-21-245-28.eu-north-1.compute.amazonaws.com/swagger-ui/index.html)

Swagger can be used to:

- explore available endpoints,

- inspect request and response models,

- authenticate using a JWT token,

- execute API requests directly from the browser.

## Main Features

## Authentication

The application implements stateless authentication using JWT.

Features include:

- user registration,

- user login,

- BCrypt password hashing,


- JWT token generation,

- JWT token validation,

- Spring Security authentication,

- role-based authorization,

- protected REST endpoints.

There are two application roles:

USER

ADMIN

Regular users can access the bookstore functionality, while administrators have additional permissions for managing books and categories.

## Books

The Book API provides:

- retrieving books,

- retrieving a book by ID,

- pagination,

- sorting,

- searching,

- creating books,

- updating books,

- deleting books,

- assigning categories to books.

## Example:

GET /api/books?page=0&size=10

Authenticated users can browse the catalogue.

Administrators can additionally create, update and delete books.

## 🏷️ Categories

The Category API provides:

- retrieving categories,

- creating categories,


- updating categories,

- deleting categories,

- assigning categories to books.

Categories are connected to books through JPA relationships.

## Shopping Cart

Authenticated users can manage their own shopping cart.

Available functionality includes:

- retrieving the current user's cart,

- adding books,

- changing quantities,

- removing cart items.

The application uses the authenticated user rather than accepting an arbitrary user ID from the client.

This prevents a user from simply changing a user ID in the request and accessing another user's shopping cart.

## Authentication Flow

│


│ Credentials OK │

│

│

JWT token │

│

Authorization: Bearer <token>

│

│ JWT Authentication │

│

Filter

│

│

│ Spring Security

│

│ Authentication

│

│

│ Controller / Role │

│

validation

│

## 🏗️ Application Architecture

│

│


## Main Controllers

## AuthenticationController

Base path:

/api/auth

| Method | Endpoint | Description | Access |
| --- | --- | --- | --- |
| POST | /api/auth/register | Register a new user | Public |
| POST | /api/auth/login | Login and receive JWT | Public |

## BookController

Base path:


## /api/books

| Method | Endpoint | Description | Access |
| --- | --- | --- | --- |
| GET | /api/books | Get paginated books | USER |
| GET | /api/books/search | Search books | USER |
| GET | /api/books/{id} | Get book by ID | USER |
| POST | /api/books | Create book | ADMIN |
| PUT | /api/books/{id} | Update book | ADMIN |
| DELETE | /api/books/{id} | Delete book | ADMIN |

The controller uses validation, pagination and role-based method security.

## CategoryController

Base path:

/api/categories

Provides category management and retrieval functionality with appropriate authentication and authorization.

## ShoppingCartController

Base path:

/api/cart

| Method Endpoint GET /api/cart POST /api/cart | Description Get current user's cart Add a book to cart |
| --- | --- |
| PUT /api/cart/cart-items/{cartItemId} DELETE /api/cart/cart-items/{cartItemId} | Update quantity Remove item |


| Technologies Technology Usage Java 17 Programming language Spring Boot 3.5.4 Backend framework Spring Web REST API Spring Security Authentication and authorization JWT / JJWT 0.13.0 Stateless authentication Spring Data JPA Database access Hibernate ORM MySQL 8 Relational database Liquibase 4.31.1 Database migrations H2 Test database MapStruct DTO mapping Lombok Boilerplate reduction Springdoc OpenAPI Swagger documentation JUnit Testing Mockito Unit testing MockMvc Controller testing Maven Build and dependency management Checkstyle Code quality |
| --- |
| Docker Containerization Docker Compose Local environment |


AWS EC2

Application hosting

AWS RDS

Production database

Amazon ECR Public

Docker image registry

## 🗄️ Database

The project uses MySQL as the main relational database.

Database access is implemented using:

- Spring Data JPA,

- Hibernate,

- JPA entity relationships,

- repositories,

- Liquibase migrations.

Liquibase manages database changes using versioned changesets.

This makes database changes reproducible across environments.

## Production architecture

AWS EC2

│

│ Docker

Book Store API

│

│ JDBC

AWS RDS

│

MySQL

Database credentials are supplied at runtime and should never be committed to the repository.


## Testing

The project contains tests for the main application layers.

## Repository tests

Repository tests verify database operations using an H2 test database.

## Service tests

Service tests use Mockito to isolate business logic from repositories.

## Controller tests

REST controllers are tested using:

MockMvc

Security-related scenarios are also tested, including:

- authenticated users,

- different roles,

- protected endpoints,

- CSRF handling where required,

- service interactions.

## Run all tests:

./mvnw test

On Windows:

.\mvnw.cmd test

Run the full Maven verification:

./mvnw clean verify

## Swagger / OpenAPI

The project uses Springdoc OpenAPI to automatically generate API documentation.

## Local Swagger:

http://localhost:8080/swagger-ui/index.html


## Production Swagger:

http://ec2-51-21-245-28.eu-north-1.compute.amazonaws.com/swagger-ui/index.h

Swagger provides an interactive way to test the API.

## Using JWT in Swagger

- 1. Register a user.

- 2. Login using /api/auth/login .

- 3. Copy the JWT token from the response.

- 4. Click Authorize in Swagger.

- 5. Enter:

Bearer <your-token>

- 6. Execute protected endpoints.

## Docker

The application is containerized using Docker.

The local environment can run the application together with MySQL using Docker Compose.

## Example:

docker compose up --build

The application runs on:

http://localhost:8080

## Swagger:

http://localhost:8080/swagger-ui/index.html

## ☁️ AWS Deployment

The application was deployed to AWS using:

│ Local project │


## Docker image

The deployed image is stored in Amazon ECR Public:

public.ecr.aws/c5q4d8k8/bookstore-api:v02

## The EC2 instance runs:

```
EC2 port 80
↓
Docker port 8080
↓
Spring Boot application
```

The application connects to the AWS RDS MySQL database.

## Local Setup

## Requirements


Before running the project, install:

- JDK 17

- Docker Desktop

- Git

Maven Wrapper is included in the project.

## Clone the repository

git clone https://github.com/slawomir-pacek/book-store.git

cd book-store

## Run with Docker Compose

docker compose up --build

The API will be available at:

http://localhost:8080

## Swagger:

http://localhost:8080/swagger-ui/index.html

## Stop containers

docker compose down

## Run from IntelliJ IDEA

The project can also be started directly from IntelliJ IDEA.

Make sure the database is available and configure the datasource for your local environment.

Then run:

BookStoreApplication

The application starts on:

http://localhost:8080


## Spring Security is configured with:

- stateless sessions,

- JWT authentication,

- custom UserDetailsService ,

- BCrypt password encoder,

- JWT authentication filter,

- method-level security,

- role-based authorization.

## Public endpoints include:

/api/auth/register /api/auth/login /swagger-ui/**

/v3/api-docs/**

Other endpoints require authentication.

Administrative operations require the ADMIN role.

## Challenges & Solutions

## JWT authentication

Challenge: Protecting REST endpoints without using server-side sessions.

Solution: Implemented JWT authentication with a custom authentication filter and

Spring Security.

## Role-based authorization

Challenge: Users and administrators require different permissions.

Solution: Implemented Spring Security authorities and method-level authorization

using @PreAuthorize .

## Example:

@PreAuthorize("hasRole('ADMIN')")


## Database migrations

Challenge: Keeping database structure consistent between development and deployment environments.

Solution: Liquibase changesets are used to version and automatically apply database changes.

## Testing secured controllers

Challenge: Controller tests need to simulate authenticated users and different roles.

Solution: Used MockMvc together with Spring Security Test and Mockito.

## Docker networking

Challenge: The database hostname differs between local Docker, IntelliJ and AWS environments.

Solution: Database configuration is adapted to the environment:

```
Local Docker
↓
MySQL Docker service
AWS
↓
AWS RDS endpoint
```

## AWS deployment

Challenge: Deploying a Spring Boot application required coordinating Docker, ECR, EC2, RDS, networking and application configuration.

Solution: Created a Docker image, pushed it to Amazon ECR Public and deployed it to an EC2 instance connected to an RDS MySQL database.


During deployment Swagger initially returned:

HTTP 403 Forbidden

The Spring Security configuration was updated to allow Swagger UI and OpenAPI endpoints.

After deploying the corrected image, the EC2 instance returned:

HTTP/1.1 200

confirming that Swagger UI was successfully available.

## Future Improvements

Possible next steps include:

- refresh tokens,

- order management,

- checkout functionality,

- book cover images,

- shopping order history,

- CI/CD with GitHub Actions,

- automated AWS deployment,

- Testcontainers,

- application monitoring,

- metrics and health checks,

- frontend application.

## What I Learned

Through this project I gained practical experience with:

- Java backend development,

- Spring Boot,

- REST API design,

- Spring Security,

- JWT authentication,

- role-based authorization,

- Spring Data JPA,

- Hibernate,

- relational database design,

- Liquibase,

- unit and integration-style testing,

- Mockito,

- MockMvc,

- Docker,

- Docker Compose,

- AWS EC2,

- AWS RDS,

- Amazon ECR,

- Swagger / OpenAPI,

- debugging deployed applications,

- API documentation.

The most valuable part of the project was learning how these technologies work together as one backend system rather than treating each technology as a separate exercise.


## Project Links

## GitHub Repository

[https://github.com/slawomir-pacek/book-store](https://github.com/slawomir-pacek/book-store)

## Live Swagger

[http://ec2-51-21-245-28.eu-north-1.compute.amazonaws.com/swagger-ui/index.html](http://ec2-51-21-245-28.eu-north-1.compute.amazonaws.com/swagger-ui/index.html)

## Author

## Sławomir Pacek

Java Backend Developer in progress, focused on building practical backend applications using Java, Spring Boot, databases, testing, Docker and AWS.
