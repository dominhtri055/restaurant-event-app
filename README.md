# Restaurant Event App

A Java Spring Boot restaurant event management application built as a school project.  
This project was developed by **Tri Do** in collaboration with partner **Nhat Le**.

## Project Overview

Restaurant Event App is a full-stack web application for managing restaurant-related event information. The project includes a web frontend, REST APIs, authentication, persistence, and supporting shared modules. It follows a modular Maven structure to separate the main application layers and services.

The application was created for academic purposes to practise Spring Boot, MVC architecture, REST API development, authentication, database integration, and multi-module project organization.

## Contributors

- **Tri Do**
- **Nhat Le**

## Tech Stack

- Java 21
- Spring Boot
- Spring MVC
- Spring Data JPA / Hibernate
- Thymeleaf
- MySQL
- Maven
- JWT Authentication
- REST API
- HTML / CSS

## Main Features

- Restaurant event management
- Create, view, update, search, and delete/archive event records
- Web interface for interacting with restaurant/event data
- REST API layer for restaurant data
- Authentication service with JWT support
- MySQL database integration
- Form validation
- Modular project structure
- Email configuration support for local development/testing

## Project Structure

```text
restaurant-event-app/
├── app-web/              # Main web application
├── app-resto-rest-api/   # Restaurant REST API application
├── app-auth-rest-api/    # Authentication REST API application
├── common/               # Shared infrastructure/common utilities
├── auth/                 # Authentication-related modules
├── email/                # Email service module
├── restaurant/           # Restaurant domain, persistence, service, client, and API modules
├── Artifacts/            # Build or deployment artifacts
└── pom.xml               # Parent Maven configuration