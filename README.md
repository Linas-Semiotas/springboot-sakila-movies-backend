# Sakila Movies Backend

## Overview
This project is the backend for the **Sakila Movies** application. Built with **Spring Boot**, it provides a RESTful API for managing movie rentals, customer data, and administrative tasks. The backend enables CRUD operations on entities such as movies, actors, and customers, while incorporating role-based access control for users and admins.


## Description
Sakila Movies is a web application built for the Sakila database, a test database used primarily for learning and portfolio purposes. The backend manages movies, stores, rentals, user profiles, and admin functionalities. The system uses JWT-based authentication for secure access. Roles are divided into USER and ADMIN for controlled access to features.

## Getting Started

1. Clone the repository:
    ```bash
    git clone https://github.com/Linas-Semiotas/springboot-sakila-movies-backend.git
    ```

2. Navigate to the project directory:
    ```bash
    cd springboot-sakila-movies-backend
    ```

3. Configure the application by editing the `application.properties` file located in `src/main/resources`. Ensure that the database credentials match your local setup.

4. Build the project:
    ```bash
    mvn clean install
    ```

5. Run the application:
    ```bash
    mvn spring-boot:run
    ```

6. The API will be accessible at `http://localhost:8080`.

## Features
- **User Roles**: Two roles are supported: USER and ADMIN.
  
### User Functions:
- View rented movies.
- Add fake balance.
- Update profile info and change password (in separate pages).
  
### Free Access:
- Access available stores and movies without logging in.

### Admin Functions:
- Manage movies, actors, categories, and languages.
- Modify user permissions and profiles.

- **Movie Management**: CRUD functionality for movies, actors, and categories.
- **User Management**: Admins can modify user roles, while users can update their own profile information.
- **Rental Management**: Track rental orders.
- **Role-Based Access Control**: Role-specific access for USER and ADMIN.

## API Design
RESTful API architecture for interaction between frontend and backend.

## Security Enhancements

- **JWT Authentication**: JWT (JSON Web Token) is a compact, URL-safe means of representing claims between two parties, ensuring secure data exchange.
- **Token Storage**: Tokens are stored in secure HTTP-only cookies to prevent XSS attacks.
- **Session Management**: Automatic session expiration and token refresh to ensure user sessions are valid and secure.


## Technologies Used
- **Java**: Version 21
- **Spring Boot**: Version 3.3.4
- **Spring Data JPA**: For ORM and database management.
- **Spring Web**: For RESTful API implementation.
- **Spring Security**: For handling authentication and authorization.
- **Database**:
  - **MySQL**: For persistent storage (using the official Sakila database).
  - **H2**: In-memory database for testing.
- **JWT**: For secure authentication.
- **OpenAPI/Swagger**: For API documentation.

## API Endpoints

### Rental Controller
- **POST** `/api/rental/rent`: Rent a movie.
- **GET** `/api/rental`: Get all rentals.
- **GET** `/api/rental/{id}`: Get rental by ID.

### Auth Controller
- **POST** `/api/auth/register`: Register a new user.
- **POST** `/api/auth/login`: Log in as a user.
- **POST** `/api/auth/logout`: Terminates user session.
- **POST** `/api/auth/refresh-token`: Generates a new access token if the user is active and token expired.
- **GET** `/api/auth/me`: Returns current user information such as username and roles.

### Stores Controller
- **GET** `/api/stores`: Fetch all stores (publicly accessible).

### Movies Controller
- **GET** `/api/movies`: Fetch all movies (publicly accessible).
- **GET** `/api/movies/{id}`: Fetch movie by ID (publicly accessible).

### Admin Controller
- **PUT** `/api/admin/users/{userId}`: Modify user permissions.
- **PUT** `/api/admin/movies/{id}`: Update movie details.
- **DELETE** `/api/admin/movies/{id}`: Delete a movie.
- **POST** `/api/admin/movies`: Add a new movie.
- **GET** `/api/admin/movies`: Fetch all movies.

### Languages
- **GET** `/api/admin/languages`: Fetch all languages.
- **POST** `/api/admin/languages`: Add a new language.
- **DELETE** `/api/admin/languages/{id}`: Delete a language.

### Categories
- **GET** `/api/admin/categories`: Fetch all categories.
- **POST** `/api/admin/categories`: Add a new category.
- **DELETE** `/api/admin/categories/{id}`: Delete a category.

### Actors
- **GET** `/api/admin/actors`: Fetch all actors.
- **POST** `/api/admin/actors`: Add a new actor.
- **DELETE** `/api/admin/actors/{id}`: Delete an actor.

### User Controller
#### Profile Management
- **GET** `/api/user/profile/personal-info`: Fetch user's personal info.
- **PUT** `/api/user/profile/personal-info`: Update user's personal info.
- **GET** `/api/user/profile/address-info`: Fetch user's address info.
- **PUT** `/api/user/profile/address-info`: Update user's address info.

#### Security
- **POST** `/api/user/security/change-password`: Change user's password.

#### Balance
- **POST** `/api/user/balance/add`: Add fake balance.
- **GET** `/api/user/balance`: Get user's current balance.

#### Orders
- **GET** `/api/user/orders`: Fetch user's rental orders.

## API Documentation

Once the application is running, you can access API documentation via Swagger at: `http://localhost:8080/swagger-ui.html`


## Database Setup (Using Official Sakila Database)
The Sakila Movies backend uses the official Sakila database provided by MySQL for testing and learning purposes. In addition, the database has been modified to support authentication functionality. Follow these steps to install and modify the Sakila database:

### Step 1: Download the Sakila Database
You can download the official Sakila database files from the MySQL website:

**Download Sakila Database** (Scroll down to the Example Databases section and download the appropriate Sakila database script files).

### Step 2: Install Sakila Database

1. Open **MySQL Workbench** or any other MySQL client of your choice.
2. Once connected to your MySQL server, run the Sakila schema and data scripts:
   - Load the **sakila-schema.sql** file to create the necessary tables:
     ```sql
     SOURCE /path/to/sakila-schema.sql;
     ```
   - Load the **sakila-data.sql** file to populate the tables with data:
     ```sql
     SOURCE /path/to/sakila-data.sql;
     ```


### Step 3: Modify the Sakila Database for Authentication
Run the following SQL script to add authentication functionality (user management and roles) to the Sakila database:

```sql
-- Disable Safe Update Mode
SET SQL_SAFE_UPDATES = 0;

-- Create Users Table
CREATE TABLE IF NOT EXISTS `users` (
  `user_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(255) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `balance` DOUBLE NOT NULL DEFAULT '0',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Create Roles Table
CREATE TABLE IF NOT EXISTS `roles` (
  `user_id` INT UNSIGNED NOT NULL,
  `role` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`user_id`, `role`),
  CONSTRAINT `roles_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Alter Customer Table to Add user_id Column
ALTER TABLE `customer`
ADD COLUMN `user_id` INT UNSIGNED DEFAULT NULL AFTER `customer_id`;

-- Insert Users based on existing Customers
INSERT INTO `users` (`username`, `password`, `balance`)
SELECT CONCAT(`first_name`, `last_name`) AS `username`, 
       '' AS `password`, 
       0 AS `balance`
FROM `customer`
WHERE `user_id` IS NULL;  -- Only for customers without a user_id

-- Adjust AUTO_INCREMENT to continue from the last created user
SET @max_user_id = (SELECT MAX(user_id) FROM users);
SET @next_user_id = IFNULL(@max_user_id, 0) + 1;

-- Update the AUTO_INCREMENT value for users table
SET @sql = CONCAT('ALTER TABLE users AUTO_INCREMENT = ', @next_user_id);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Update Customer Table with Corresponding User IDs
UPDATE `customer` c
JOIN `users` u ON u.username = CONCAT(c.first_name, c.last_name)
SET c.user_id = u.user_id
WHERE c.user_id IS NULL;  -- Only for customers without a user_id

-- Enable Safe Update Mode
SET SQL_SAFE_UPDATES = 1;
```
### Step 4: Configure the Backend
In the `application.properties` file located in `src/main/resources`, update the following properties with your database credentials:

``` properties
spring.datasource.url=jdbc:mysql://localhost:3306/sakila
spring.datasource.username=<your-username>
spring.datasource.password=<your-password>
```

In the `application.properties` file located in `src/main/resources`, update the following properties:
- `jwt.secret`: secret key for token generation.
- `jwtExpirationMs`: token expiration time in miliseconds.
- `cors.allowed.origins`: input your website url.
- `cookie.secure`: `false` for development, `true` for production.

``` properties
jwt.secret=<secret-key>
jwtExpirationMs=3600000
cors.allowed.origins=http://localhost:3000
cookie.secure=false
```

Run the application using Maven:

``` bash
./mvnw spring-boot:run
```
At this point, the backend will be connected to the modified Sakila database and ready to serve API requests.
    
## Admin Role Assignment
By default, when registering users, the system automatically checks if there are any existing users with the "ADMIN" role. If no admin is found, the first registered user will be assigned the "ADMIN" role automatically.

Default Password: Upon user generation, the default password for all users is set to `'password'`. It is recommended to change this password immediately after the first login.

## Contact
- Name: Linas Šemiotas
- Email: linas.semiotas@gmail.com
- GitHub: [Linas Šemiotas](https://github.com/Linas-Semiotas/springboot-sakila-movies-backend)
