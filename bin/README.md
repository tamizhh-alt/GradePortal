# Grades & Marks Portal System

A comprehensive JavaFX desktop application for managing student grades and academic records in educational institutions. This system provides a complete solution for student registration, marks entry, grade calculation, and academic reporting.

## Features

### Core Functionality
- **Student Management**: Add, edit, and manage student information with validation
- **Subject Management**: Configure subjects with customizable maximum marks
- **Marks Entry**: Record student marks with automatic grade calculation
- **Results & Reports**: Generate comprehensive student reports and export data
- **Dashboard Analytics**: View key metrics, top performers, and grade distributions
- **Search & Filter**: Find students and results quickly with advanced filtering

### Technical Features
- **MVC Architecture**: Clean separation of concerns with Model-View-Controller pattern
- **DAO Pattern**: Data Access Object pattern for all database operations
- **MySQL Integration**: Full database connectivity with JDBC
- **Professional UI**: Modern JavaFX interface with CSS styling
- **Input Validation**: Comprehensive validation for all user inputs
- **Export Functionality**: Export results to CSV format
- **Configurable Grading**: Flexible grading scale (A+ to F)

## System Requirements

- **Java**: JDK 11 or higher
- **Database**: MySQL 8.0 or higher
- **IDE**: IntelliJ IDEA (recommended) or Eclipse
- **Maven**: 3.6 or higher
- **Operating System**: Windows, macOS, or Linux

## Installation & Setup

### 1. Database Setup

1. Install MySQL and ensure it's running
2. Create a new database:
   ```sql
   CREATE DATABASE grades_portal_db;
   ```
3. Run the database setup script:
   ```bash
   mysql -u root -p grades_portal_db < database_setup.sql
   ```

### 2. Database Configuration

Update the database connection settings in `DatabaseManager.java`:
```java
private static final String DB_URL = "jdbc:mysql://localhost:3306/grades_portal_db";
private static final String DB_USERNAME = "root"; // Your MySQL username
private static final String DB_PASSWORD = ""; // Your MySQL password
```

### 3. Project Setup

1. Clone or extract the project files
2. Open the project in IntelliJ IDEA
3. Ensure the project is recognized as a Maven project
4. Wait for Maven to download dependencies

### 4. Running the Application

#### Using Maven:
```bash
mvn clean javafx:run
```

#### Using IntelliJ IDEA:
1. Open the project
2. Navigate to `src/main/java/com/gradeportal/GradePortalApp.java`
3. Right-click and select "Run GradePortalApp.main()"

#### Building JAR file:
```bash
mvn clean package
```

## Project Structure

```
grades-marks-portal/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/gradeportal/
│       │       ├── GradePortalApp.java          # Main application class
│       │       ├── controller/                  # FXML controllers
│       │       │   ├── MainController.java
│       │       │   ├── DashboardController.java
│       │       │   ├── StudentsController.java
│       │       │   ├── MarksController.java
│       │       │   ├── ResultsController.java
│       │       │   └── SubjectsController.java
│       │       ├── dao/                         # Data Access Objects
│       │       │   ├── StudentDAO.java
│       │       │   ├── SubjectDAO.java
│       │       │   └── MarkDAO.java
│       │       ├── model/                       # Entity classes
│       │       │   ├── Student.java
│       │       │   ├── Subject.java
│       │       │   └── Mark.java
│       │       └── util/                        # Utility classes
│       │           ├── DatabaseManager.java
│       │           ├── GradeCalculator.java
│       │           └── AlertUtil.java
│       └── resources/
│           ├── fxml/                           # FXML view files
│           │   ├── MainWindow.fxml
│           │   ├── Dashboard.fxml
│           │   ├── Students.fxml
│           │   ├── Marks.fxml
│           │   ├── Results.fxml
│           │   └── Subjects.fxml
│           └── css/
│               └── application.css              # Application styling
├── database_setup.sql                          # Database schema
├── pom.xml                                     # Maven configuration
└── README.md                                   # This file
```

## Usage Guide

### 1. Dashboard
- View system statistics and key metrics
- Monitor top performing students
- Analyze grade distribution
- Refresh data with the refresh button

### 2. Student Management
- **Add Students**: Fill in student details and click "Add Student"
- **Edit Students**: Select a student from the table, modify details, and click "Update"
- **Delete Students**: Select a student and click "Delete" (with confirmation)
- **Search**: Use the search field to find students by name or roll number

### 3. Subject Management
- **Add Subjects**: Enter subject name and maximum marks
- **Edit Subjects**: Select and modify existing subjects
- **Delete Subjects**: Remove subjects (only if no marks are associated)

### 4. Marks Entry
- **Select Student**: Choose from the dropdown list
- **Select Subject**: Choose the subject for marks entry
- **Enter Marks**: Input marks (0-100), grade is calculated automatically
- **View Grade**: Real-time grade calculation and description
- **Manage Entries**: Update or delete existing marks entries

### 5. Results & Reports
- **Filter Results**: Filter by student or search across all results
- **Generate Reports**: Select a student and generate comprehensive academic report
- **Export Data**: Export filtered results to CSV format
- **View Details**: Click on any result to see student report

## Database Schema

### Students Table
- `id` (INT, Primary Key, Auto Increment)
- `name` (VARCHAR(100), Not Null)
- `roll_number` (VARCHAR(20), Unique, Not Null)
- `class` (VARCHAR(20), Not Null)
- `registration_date` (DATE, Not Null)

### Subjects Table
- `id` (INT, Primary Key, Auto Increment)
- `subject_name` (VARCHAR(100), Unique, Not Null)
- `max_marks` (INT, Not Null, Default 100)

### Marks Table
- `id` (INT, Primary Key, Auto Increment)
- `student_id` (INT, Foreign Key)
- `subject_id` (INT, Foreign Key)
- `marks_obtained` (DECIMAL(5,2), Not Null)
- `grade` (CHAR(2), Not Null)
- `entry_date` (DATE, Not Null)

## Grading Scale

| Grade | Percentage Range | Description |
|-------|-----------------|-------------|
| A+    | 95-100%         | Excellent   |
| A     | 90-94%          | Excellent   |
| A-    | 85-89%          | Very Good   |
| B+    | 80-84%          | Good        |
| B     | 75-79%          | Good        |
| B-    | 70-74%          | Above Average |
| C+    | 65-69%          | Average     |
| C     | 60-64%          | Average     |
| C-    | 55-59%          | Below Average |
| D     | 50-54%          | Poor        |
| F     | Below 50%       | Fail        |

## Troubleshooting

### Common Issues

**Database Connection Failed:**
- Ensure MySQL is running
- Verify database credentials in `DatabaseManager.java`
- Check if the database `grades_portal_db` exists

**JavaFX Runtime Issues:**
- Ensure Java 11+ is installed
- Verify JavaFX modules are available
- Try running with: `java --module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml -cp target/classes com.gradeportal.GradePortalApp`

**Maven Dependencies:**
- Run `mvn clean install` to download dependencies
- Check internet connection for dependency downloads

**FXML Loading Errors:**
- Ensure all FXML files are in `src/main/resources/fxml/`
- Verify controller class names match FXML fx:controller attributes

### Performance Tips

- Regular database maintenance and indexing
- Limit large data exports to avoid memory issues
- Use search and filters for better performance with large datasets
- Regular database backups recommended

## Contributing

1. Follow the existing code structure and naming conventions
2. Maintain MVC architecture principles
3. Add proper error handling and validation
4. Update documentation for new features
5. Test thoroughly before committing changes

## Security Considerations

- Input validation is implemented for all user inputs
- SQL injection prevention through prepared statements
- Database credentials should be externalized in production
- Consider implementing user authentication for production use

## Future Enhancements

- User authentication and role-based access
- Advanced reporting with charts and graphs
- Email notifications for report generation
- Integration with external systems
- Mobile application companion
- Automated backup scheduling

## Support

For technical support or questions:
1. Check this README file
2. Review the code comments and documentation
3. Verify database setup and configuration
4. Check system requirements compatibility

## License

This project is created for educational purposes. Feel free to modify and distribute according to your institution's requirements.

---

**Version**: 1.0.0  
**Last Updated**: January 2024  
**Developed By**: Grade Portal Development Team