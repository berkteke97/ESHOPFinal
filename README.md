
# ESHOP Project

## Prerequisites

Before you start, ensure that you have the following installed on your machine:

### 1. Node.js and npm (Node Package Manager)
- **Version:** Node.js v18.x or above.
- **Check if installed:**
  ```bash
  node -v
  npm -v
  ```
- **Installation:**
  - Download from the official website: [Node.js](https://nodejs.org/)
  - Or install using a package manager:
    - **Ubuntu/Debian:**
      ```bash
      sudo apt update
      sudo apt install nodejs npm
      ```
    - **MacOS (Homebrew):**
      ```bash
      brew install node
      ```

### 2. Java Development Kit (JDK)
- **Version:** Java 17 or above.
- **Check if installed:**
  ```bash
  java -version
  ```
- **Installation:**
  - **Ubuntu/Debian:**
    ```bash
    sudo apt update
    sudo apt install openjdk-17-jdk
    ```
  - **MacOS (Homebrew):**
    ```bash
    brew install openjdk@17
    ```

### 3. Angular CLI
- **Version:** Angular CLI 15.x or above.
- **Check if installed:**
  ```bash
  ng version
  ```
- **Installation:**
  ```bash
  npm install -g @angular/cli
  ```

### 4. Maven (for Spring Boot)
- **Check if installed:**
  ```bash
  mvn -v
  ```
- **Installation:**
  - **Ubuntu/Debian:**
    ```bash
    sudo apt update
    sudo apt install maven
    ```
  - **MacOS (Homebrew):**
    ```bash
    brew install maven
    ```

## Project Setup

This project consists of two main parts:
1. **Frontend** - Angular application
2. **Backend** - Spring Boot application

### Backend - Spring Boot Setup

1. **Navigate to the backend folder:**
   ```bash
   cd ESHOP
   ```

2. **Run the Spring Boot application:**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

   The backend will start at `http://localhost:8092`.

### Frontend - Angular Setup

1. **Navigate to the frontend folder:**
   ```bash
   cd eshop-ui
   ```

2. **Install dependencies:**
   ```bash
   npm install
   ```

3. **Run the Angular development server:**
   ```bash
   npm run start
   ```

   The frontend will start at `http://localhost:4200`.

## Running Both Applications

To run both applications simultaneously:

1. **Start the Spring Boot backend:**
   - Run the backend with the commands:
     ```bash
     cd backend
     mvn spring-boot:run
     ```

2. **Start the Angular frontend:**
   - Open a new terminal and run the frontend:
     ```bash
     cd eshop-ui
     npm run start
     ```

## Additional Commands

### Running Tests

- **Backend (Spring Boot Tests):**
  ```bash
  mvn test
  ```

- **Frontend (Angular Unit Tests):**
  ```bash
  npm run test
  ```

### Building the Angular App for Production

To build the Angular app for production, use the following command:
```bash
ng build --prod
```

This will create an optimized production build in the `dist/` folder, which can be deployed on a web server.

## Features

1. **User Registration & Authentication**
   - Users can register and log in.
   - JWT-based authentication for secure endpoints.

2. **Profile Management**
   - Users can view and update their profiles.
   - Passwords are stored securely in the backend.

3. **Product Management**
   - Admins can add, update, and delete products.
   - Users can browse and view product details.

4. **Shopping Cart**
   - Users can add products to their cart and proceed to checkout.

