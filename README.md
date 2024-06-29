# LearningCourses API System

LearningCourses API System is an online application designed to manage courses effectively. 
Its primary aim is to facilitate efficient interaction between students and lecturers during the submission of assignments
and the receipt of corresponding feedback from lecturers.

## Functional Requirements

### Registration
- User registration.
- User authorization and authentication.

### Administrator
- Manage all information in the system.
- Add users to courses without a queue.
- Add new lecturers (lecturers can only be added this way) and, if required, students.

### Lecturer
- Create, edit, and delete course templates.
- View the list of course templates.
- View detailed information about specific course templates.
- Create courses from course templates. Edit and delete own courses.
- View the list of courses.
- View detailed information about specific courses.
- Send a request to join a course as a lecturer.
- Approve student requests to access courses.
- Remove students from courses.
- View the list of students in their courses.
- Assign homework.
- Check homework submitted by students.
- Grade student homework.
- Complete courses.

### Student
- View the list of all available courses.
- View detailed information about specific courses.
- Send a request to join a course as a student.
- View the list of courses they have participated/are participating in and their information.
- Receive homework assignments.
- Submit completed homework as files (less than 1 MB).

## Technical Requirements

- **Java 17**
- **Spring Boot 3.0.13**
- **Relational Database**: MySQL
- **JPA Hibernate**
- **Database Migration Tool**: Flyway
- **Unit Tests**: For service level
- **API Unit Tests**: For controller level
- **Integration Tests**: For DAO level
- **Adherence to RESTful API Specifications**
- **JWT Token-Based Authorization**
- **Build Tool**: Maven
- **API Docker Support**
- **CI/CD Pipeline**: Using Amazon CodePipeline and Amazon CodeDeploy
- **Deployment on AWS**

## Getting Started

### Prerequisites
- Java 17
- Maven
- Docker
- MySQL

### Installation

1. **Clone the repository:**
   ```sh
   git clone https://github.com/UniversityProjectKNU/learning-courses.git
   cd learning-courses
   ```


2. **Set up environment variables**:

   The __.src/main/resources/application.yml__ file stores all public variables.
   All other variables are considered secret and must be provided as environment variables. 
In order to run the project with docker-compose-local, you must create the following two files in the project root: 

- __app-env-local__ - responsible for the variables needed for the application to work:
    ```app-env-local
       AWS_CREDENTIALS_ACCESS_KEY=*some_access_key*
       AWS_CREDENTIALS_SECRET_KEY=*some_secret_key*
  
       JWT_SECRET_KEY=*some_jwt_key* //example. Needed to encrypt/decrypt JWT tokens.
  
       BUCKET_REGION=eu-central-1 //example. AWS Region where bucker is located.
       BUCKET_NAME=nazar-grynko-learning-courses-bucket //example. AWS S3 bucket in the BUCKET_REGION.
  
       MYSQL_DATABASE=learning_courses //example. Name of the database schema.
       MYSQL_HOST=127.0 0.1 //example. Database host.
       MYSQL_PORT=3306 //example. Database port.
       MYSQL_ROOT_PASSWORD=root //example. User password to enter to enter the database.
       MYSQL_ROOT_USER=root //example. User login to enter to enter the database.
    ```


- __db-env-local__ - responsible for the variables needed for the database to work:
    ```db-env-local 
       MYSQL_USER=root //example. Login to create a user in the database.
       MYSQL_PASSWORD=root //example. Password to create a user in the database.
       MYSQL_DATABASE=learning_courses //example. Name of the database schema.
    ```


3. **Build and run the Docker containers**:
    ```sh
    docker-compose up --build -f docker-compose-local.yml
    ```


## API Endpoints
All the endpoints you can find by this URL: /swagger-ui/index.html
