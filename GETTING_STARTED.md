# Getting Started

This guide provides instructions on how to set up and run the project locally for development.

## Prerequisites
- Java 17
- Maven
- Docker
- MySQL

## Local Installation & Setup

1.  **Clone the repository:**
    ```sh
    git clone https://github.com/UniversityProjectKNU/learning-courses.git
    cd learning-courses
    ```

2.  **Set up environment variables**:

    The project requires two local files for environment variables at the project root. These files are ignored by git.

    -   **`app-env-local`**: Contains secrets and configuration for the Spring Boot application.
        ```properties
        # Example values
        AWS_CREDENTIALS_ACCESS_KEY=your_aws_access_key
        AWS_CREDENTIALS_SECRET_KEY=your_aws_secret_key
        JWT_SECRET_KEY=a-strong-secret-key-for-jwt-tokens
        BUCKET_REGION=eu-central-1
        BUCKET_NAME=your-unique-s3-bucket-name
        MYSQL_DATABASE=learning_courses
        MYSQL_HOST=127.0.0.1
        MYSQL_PORT=3306
        MYSQL_ROOT_PASSWORD=root
        MYSQL_ROOT_USER=root
        ```

    -   **`db-env-local`**: Contains configuration for the database initialization.
        ```properties
        # Example values
        MYSQL_USER=root
        MYSQL_PASSWORD=root
        MYSQL_DATABASE=learning_courses
        ```

3.  **Build and run with Docker Compose**:

    This command will build the Spring Boot application, create a MySQL container, and start both services.
    ```sh
    docker-compose up --build -f docker-compose-local.yml
    ```
    The application will be available at `http://localhost:8080`.
