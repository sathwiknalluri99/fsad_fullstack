# ERP Management System

A complete Enterprise Resource Planning (ERP) system with separate frontend and backend applications for better project organization and deployment flexibility.

## Project Structure

```
ERPMANAGEMENT/
├── ERP-Frontend/          # React frontend application
│   ├── src/
│   ├── public/
│   ├── package.json
│   ├── vite.config.js
│   └── README.md
├── ERP-Backend/           # Spring Boot + Hibernate backend API
│   ├── src/
│   ├── pom.xml
│   ├── application.properties
│   └── README.md
└── README.md              # This file
```

## Features

- **User Management**: Role-based authentication (Admin, Teacher, Student)
- **Course Management**: Create and manage courses
- **Assignment System**: Create assignments, submit work, grade submissions
- **Attendance Tracking**: Mark and track student attendance
- **Enrollment Management**: Course enrollment system
- **Dashboard**: Role-specific dashboards for different user types

## Technology Stack

### Frontend (ERP-Frontend)
- **React 18** - UI framework
- **Vite** - Build tool and dev server
- **React Router** - Client-side routing
- **Axios** - HTTP client for API calls
- **CSS** - Styling

### Backend (ERP-Backend)
- **Java 17** - Programming language
- **Spring Boot 3.2.0** - Enterprise framework
- **Hibernate** - ORM for database handling
- **Spring Data JPA** - Data access layer
- **Spring Security** - Security framework
- **MySQL** - Database
- **JWT** - Authentication tokens
- **Maven** - Build tool

## Prerequisites

- **Node.js** 18+ and npm (for frontend)
- **Java 17** or higher (for backend)
- **Maven 3.6+** (for backend)
- **MySQL 8.0+** database server

## Database Setup

1. **Install MySQL** and create a database:
```sql
CREATE DATABASE erp_management;
```

2. **Update database credentials** in `ERP-Backend/src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/erp_management
spring.datasource.username=your_username
spring.datasource.password=your_password
```

## Quick Start

### Backend Setup

1. **Navigate to backend directory:**
```bash
cd ERP-Backend
```

2. **Run setup script:**
```bash
# Windows
setup.bat

# Linux/Mac
./setup.sh
```

3. **Start the Spring Boot server:**
```bash
mvn spring-boot:run
```

Backend API will be available at `http://localhost:8080`

### Frontend Setup

1. **Navigate to frontend directory:**
```bash
cd ERP-Frontend
```

2. **Run setup script:**
```bash
# Windows
setup.bat

# Linux/Mac
./setup.sh
```

3. **Start the frontend development server:**
```bash
npm run dev
```

Frontend will be available at `http://localhost:3000`

## API Endpoints

### Authentication
- `POST /api/auth/login` - User login
- `POST /api/auth/register` - User registration

### Users
- `GET /api/users` - Get all users
- `POST /api/users` - Create user
- `GET /api/users/:id` - Get user by ID
- `PUT /api/users/:id` - Update user
- `DELETE /api/users/:id` - Delete user

### Courses
- `GET /api/courses` - Get all courses
- `POST /api/courses` - Create course
- `GET /api/courses/:id` - Get course by ID
- `PUT /api/courses/:id` - Update course
- `DELETE /api/courses/:id` - Delete course

### Assignments
- `GET /api/assignments` - Get all assignments
- `POST /api/assignments` - Create assignment
- `GET /api/assignments/:id` - Get assignment by ID
- `PUT /api/assignments/:id` - Update assignment
- `DELETE /api/assignments/:id` - Delete assignment

### Attendance
- `GET /api/attendance` - Get attendance records
- `POST /api/attendance` - Mark attendance
- `GET /api/attendance/student/:studentId` - Get student attendance

## Development Workflow

### Working on Frontend Only
```bash
cd ERP-Frontend
npm run dev
```
Make sure backend is running separately.

### Working on Backend Only
```bash
cd ERP-Backend
npm run dev
```
Frontend can be served from built files or another instance.

### Full Stack Development
1. Start backend: `cd ERP-Backend && npm run dev`
2. Start frontend: `cd ERP-Frontend && npm run dev`
3. Both servers run independently

## Deployment

### Backend Deployment
- Build and deploy the Node.js application
- Set up production database
- Configure environment variables
- Use process manager like PM2

### Frontend Deployment
- Build for production: `npm run build`
- Deploy the `dist/` folder to web server
- Configure API base URL for production

## Security

- **JWT Authentication**: Secure token-based authentication
- **Password Hashing**: bcrypt for secure password storage
- **Input Validation**: Sanitize all user inputs
- **CORS**: Configured for cross-origin requests
- **Environment Variables**: Sensitive data stored securely

## Troubleshooting

### Common Issues

1. **Database Connection**: Check MySQL server and credentials
2. **Port Conflicts**: Change ports in .env files if needed
3. **CORS Errors**: Verify API URLs and CORS configuration
4. **Build Errors**: Clear node_modules and reinstall

### Logs
- Backend logs appear in terminal/console
- Frontend logs in browser developer tools
- Check network tab for API call issues

## Contributing

1. **Fork the repository**
2. **Create feature branches** for new features
3. **Follow coding standards** in each project
4. **Test thoroughly** before submitting
5. **Update documentation** as needed

## License

This project is licensed under the MIT License..
