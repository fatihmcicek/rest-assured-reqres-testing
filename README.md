# REST Assured ReqRes API Testing Project

This project demonstrates API testing using REST Assured framework with ReqRes API (https://reqres.in/).

## Technologies & Tools
- Java 21
- REST Assured
- TestNG
- Maven
- Lombok
- Jackson
- Log4j

## Test Scenarios
- User Management Tests
    - Get List Users
    - Get Single User
    - Create User
    - Update User
    - Delete User
    - Register User
    - Login User

## Project Structure
```plaintext
src/
├── test/
│   ├── java/
│   │   └── com/
│   │       └── restassured/
│   │           └── reqres/
│   │               ├── config/
│   │               │   └── ConfigManager.java
│   │               ├── models/
│   │               │   ├── User.java
│   │               │   ├── UserResponse.java
│   │               │   └── UsersListResponse.java
│   │               ├── tests/
│   │               │   ├── BaseTest.java
│   │               │   └── UserTests.java
│   │               └── utils/
│   │                   └── TestUtils.java
│   └── resources/
│       ├── config.properties
│       └── log4j2.xml
└── pom.xml

## How to Run Tests
mvn clean test