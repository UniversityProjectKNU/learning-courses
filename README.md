# Learning Courses Platform (Backend)

This repository contains the backend service for the Learning Courses platform. It's a course management application with role-based access for administrators, lecturers, and students.

The frontend is available in a separate repository: [learning-courses-frontend](https://github.com/UniversityProjectKNU/learning-courses-frontend).

## Key Features

The platform supports three main user roles with distinct permissions:

### Administrator
-   **User Management**: Can add, remove, and manage all users, including creating Lecturer accounts.
-   **Course Enrollment**: Can directly enroll any user into any course.
-   **System Oversight**: Has access to all data within the system.

### Lecturer
-   **Curriculum Management**: Can create, edit, and delete course templates.
-   **Course Management**: Can launch new courses from templates, edit course details, and manage course status.
-   **Student Management**: Approves enrollment requests and can remove students from their courses.
-   **Homework & Grading**: Assigns homework, reviews submissions, and provides grades.

### Student
-   **Course Discovery**: Can browse and view all available courses.
-   **Enrollment**: Can send requests to join courses.
-   **Learning**: Receives and submits homework assignments (as file uploads).
-   **Progress Tracking**: Can view their courses and grades.

## Tech Stack

-   **Framework**: Java 17 & Spring Boot 3
-   **Data**: Spring Data JPA / Hibernate
-   **Database**: MySQL with Flyway for migrations
-   **Authentication**: JWT Token-Based Security
-   **File Storage**: AWS S3 for homework submissions
-   **Build Tool**: Maven
-   **Containerization**: Docker

## Getting Started & API

-   For instructions on how to build and run the project locally, see [Setup & Installation](GETTING_STARTED.md).
-   The complete API documentation is available via Swagger UI at the `/swagger-ui/index.html` endpoint once the application is running.
