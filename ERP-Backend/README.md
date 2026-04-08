# Java ERP Backend

Spring Boot backend for the Java Full Stack ERP Management System.

## Features

- **User Management**: Role-based authentication (Admin, Teacher, Student)
- **Course Management**: Create and manage courses
- **Assignment System**: Create assignments, submit work, grade submissions
- **Attendance Tracking**: Mark and track student attendance
- **Enrollment Management**: Course enrollment system
- **JWT Authentication**: Secure token-based authentication
- **MySQL Database**: Persistent data storage

## Technology Stack

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **Spring Security**
- **MySQL Database**
- **JWT Authentication**
- **Maven Build Tool**

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+

## Database Setup

1. Install MySQL and create a database:
```sql
CREATE DATABASE java_erp_db;
```

2. Update database credentials in `src/main/resources/application.properties`:
```properties
spring.datasource.username=your_username
spring.datasource.password=your_password
```

## Running the Application

1. **Build the project:**
```bash
mvn clean install
```

2. **Run the application:**
```bash
mvn spring-boot:run
```

The backend will start on `http://localhost:8080`

## API Endpoints

### Authentication
- `POST /api/auth/login` - User login
- `POST /api/auth/register` - User registration

### Users
- `GET /api/users` - Get all users
- `GET /api/users/{id}` - Get user by ID
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user

### Courses
- `GET /api/courses` - Get all courses
- `POST /api/courses` - Create course
- `GET /api/courses/{id}` - Get course by ID
- `PUT /api/courses/{id}` - Update course
- `DELETE /api/courses/{id}` - Delete course

### Assignments
- `GET /api/assignments` - Get all assignments
- `POST /api/assignments` - Create assignment
- `GET /api/assignments/{id}` - Get assignment by ID
- `PUT /api/assignments/{id}` - Update assignment
- `DELETE /api/assignments/{id}` - Delete assignment

### Attendance
- `GET /api/attendance` - Get attendance records
- `POST /api/attendance` - Mark attendance
- `GET /api/attendance/student/{studentId}` - Get student attendance

## Default Users

After running the application, these unique default users are created:

- **System Maestro (Admin)**: `quantum_admin` / `Qu@ntum2024!`
- **Knowledge Architect (Teacher)**: `prof_innovator` / `EduC@t2024#`
- **Future Innovator (Student)**: `student_explorer` / `L3@rn2024$`
- **Data Scientist (Admin)**: `data_scientist` / `D@t@Sc!2024`
- **Mentor Guide (Teacher)**: `mentor_guide` / `M3nt0r2024!`
- **Future Leader (Student)**: `future_leader` / `FutuR3_2024#`

## Project Structure

```
src/main/java/com/erp/backend/
├── controller/              # REST API controllers
├── entity/                  # JPA entities
├── repository/              # Data repositories
├── service/                 # Business logic
└── config/                  # Configuration classes

src/main/resources/
└── application.properties   # Application config
```

## Security

- JWT-based authentication
- Password encryption using BCrypt
- Role-based access control
- CORS configuration for frontend communication

## Development

### Adding New Entities
1. Create entity class in `entity/` package
2. Create repository interface in `repository/` package
3. Create service class in `service/` package
4. Create controller in `controller/` package

### Database Migrations
The application uses JPA auto-ddl with `update` strategy. For production, consider using Flyway or Liquibase for proper migrations.