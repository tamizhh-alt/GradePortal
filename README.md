# 🎓 GradePortal - Student Grade Management System

A comprehensive JavaFX desktop application for managing student grades and academic records in educational institutions. Built with Java 21, JavaFX, and MySQL.

## ✨ Features

### 🔐 Authentication & User Roles
- **Admin Login**: Full access to manage students, subjects, marks, and generate reports
- **Student Login**: View personal profile, enrolled courses, grades, and download reports
- **Role-based Access Control**: Secure separation between admin and student functionalities

### 👨‍💼 Admin Dashboard
- **Student Management**: Add, edit, delete, and search student records
- **Subject Management**: Create and manage course subjects
- **Marks Entry**: Assign grades to students for different subjects
- **Results & Reports**: Generate comprehensive academic reports
- **Data Export**: Export student data and grade reports

### 👨‍🎓 Student Dashboard
- **Personal Profile**: View student information and registration details
- **Course Overview**: See enrolled subjects and course details
- **Grade Display**: View marks and grades for all subjects
- **Report Download**: Export personal grade reports in CSV format

## 🛠️ Tech Stack

- **Java**: 21 (LTS)
- **JavaFX**: 21.0.3
- **Database**: MySQL 8.0+
- **Build Tool**: Maven 3.8+
- **IDE**: VS Code (recommended)

## 📋 Prerequisites

- Java 21 JDK installed
- MySQL 8.0+ server running
- Maven 3.8+ installed
- Git for version control

## 🚀 Quick Start

### 1. Clone the Repository
```bash
git clone <repository-url>
cd GradePortal
```

### 2. Database Setup
1. **Start MySQL Server**
2. **Create Database and Tables**
   ```bash
   mysql -u root -p < database_setup.sql
   ```
   
   Or manually run the SQL commands in your MySQL client:
   ```sql
   CREATE DATABASE grades_portal_db;
   USE grades_portal_db;
   -- Run the table creation scripts from database_setup.sql
   ```

### 3. Configure Database Connection
Update `src/main/java/com/gradeportal/util/DatabaseManager.java` with your MySQL credentials:
```java
private static final String DB_URL = "jdbc:mysql://localhost:3306/grades_portal_db";
private static final String DB_USERNAME = "your_username";
private static final String DB_PASSWORD = "your_password";
```

### 4. Build and Run
```bash
# Clean and compile
mvn clean compile

# Run the application
mvn javafx:run
```

## 🔑 Default Login Credentials

### Admin Access
- **Username**: `admin`
- **Password**: `admin123`
- **Role**: Administrator

### Student Access
- **Username**: `ST001`, `ST002`, `ST003`, `ST004`
- **Password**: `student123`
- **Role**: Student

## 📁 Project Structure

```
GradePortal/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/gradeportal/
│   │   │       ├── controller/          # FXML Controllers
│   │   │       ├── dao/                 # Data Access Objects
│   │   │       ├── model/               # Entity Classes
│   │   │       ├── util/                # Utility Classes
│   │   │       └── GradePortalApp.java  # Main Application
│   │   ├── resources/
│   │   │   ├── fxml/                    # FXML UI Files
│   │   │   ├── css/                     # Stylesheets
│   │   │   └── com/gradeportal/view/    # Additional Views
│   │   └── module-info.java             # Java Module Configuration
├── database_setup.sql                    # Database Setup Script
├── pom.xml                               # Maven Configuration
└── README.md                             # This File
```

## 🎯 Core Components

### Controllers
- **LoginController**: Handles user authentication and role-based routing
- **AdminDashboardController**: Main admin interface with navigation
- **StudentDashboardController**: Student-specific dashboard
- **StudentsController**: Student CRUD operations
- **SubjectsController**: Subject management
- **MarksController**: Grade entry and management
- **ResultsController**: Report generation and export

### Data Access Layer
- **StudentDAO**: Student database operations
- **SubjectDAO**: Subject database operations
- **MarkDAO**: Grade and mark database operations

### Models
- **Student**: Student entity with personal information
- **Subject**: Course subject entity
- **Mark**: Grade/mark entity linking students and subjects

## 🔧 Configuration

### Database Configuration
The application connects to MySQL using JDBC. Update the connection parameters in `DatabaseManager.java`:
- Database URL
- Username
- Password
- Driver class

### Application Settings
- Window dimensions and properties in `GradePortalApp.java`
- CSS styling in `application.css`
- FXML layouts in the `fxml/` directory

## 📊 Database Schema

### Users Table
- Authentication and role management
- Links to students via roll number

### Students Table
- Student personal information
- Registration details

### Subjects Table
- Course information
- Credit system

### Marks Table
- Grade records
- Links students and subjects

## 🚀 Running the Application

### Development Mode
```bash
mvn clean javafx:run
```

### Production Build
```bash
mvn clean package
java -jar target/grades-marks-portal-1.0.0.jar
```

### Debug Mode
```bash
mvn clean javafx:run -Djavafx.debug=true
```

## 🧪 Testing

### Unit Tests
```bash
mvn test
```

### Integration Tests
```bash
mvn verify
```

## 📝 Development Guidelines

### Code Style
- Follow Java naming conventions
- Use meaningful variable and method names
- Add comprehensive JavaDoc comments
- Implement proper error handling

### FXML Guidelines
- Use consistent naming conventions
- Implement proper controller bindings
- Follow JavaFX best practices

### Database Guidelines
- Use prepared statements for security
- Implement proper connection pooling
- Handle SQL exceptions gracefully

## 🐛 Troubleshooting

### Common Issues

1. **Database Connection Failed**
   - Verify MySQL server is running
   - Check database credentials
   - Ensure database exists

2. **FXML Loading Errors**
   - Verify file paths are correct
   - Check controller class names
   - Ensure all imports are present

3. **JavaFX Runtime Issues**
   - Verify Java 21 is installed
   - Check JavaFX dependencies
   - Ensure proper module configuration

### Debug Mode
Enable debug logging by setting system properties:
```bash
-Djavafx.debug=true
-Dcom.gradeportal.debug=true
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 👥 Authors

- **GradePortal Team** - Initial work

## 🙏 Acknowledgments

- JavaFX community for the excellent framework
- MySQL team for the robust database system
- Maven community for the build tool

## 📞 Support

For support and questions:
- Create an issue in the repository
- Contact the development team
- Check the troubleshooting section

---

**GradePortal** - Empowering Education Through Technology 🎓✨