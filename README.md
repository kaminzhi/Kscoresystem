# Student Grade Management System

This is a student grade management system based on Java, implemented using Object-Oriented Programming (OOP) principles.

## Features

- Supports management of grades for core subjects and elective subjects
- Automatically calculates weighted total scores and average scores
- Automatically ranks students in the class
- Generates detailed individual and class grade reports
- Comprehensive error handling and input validation

## System Architecture

The system consists of the following components:

- **Subject** (abstract class): Base class for subjects
- **CoreSubject**: Core subject class
- **ElectiveSubject**: Elective subject class
- **Student**: Student class
- **GradeManager**: Grade management class

## Usage

1. Compile the project:
   ```bash
   mvn clean compile
   ```

2. Run the project:
   ```bash
   mvn exec:java -Dexec.mainClass="Main"
   ```

## System Requirements
- Java 21 or higher
- Maven 3.6 or higher

## License

MIT License