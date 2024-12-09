
# Secure Docs

SecureDocs is a well secured document management system designed to protect sensitive information and provide robust authentication and authorization mechanisms using Spring Boot and React. The system ensures secure data handling and easy scalability.
(this repository contains the backend of the SecureDocs applciation)

## Features
- **User Authentication:** Secure login system using Spring Security.
- **JWT Authentication:** Token-based authentication for secure session management.
- **CSRF Protection:** Configured Cross-Site Request Forgery (CSRF) to restrict unauthorized access to the server.
- **Password Reset:** Feature to securely reset user passwords.
- **Role-Based Access Control:** Assign roles to users and manage permissions effectively.
- **Efficient CRUD Operations for Notes:** Perform Create, Read, Update, and Delete operations with optimal performance.
- **Build Admin Audit Functionality:** Added auditing to track user actions and maintain an audit log.
- **Implemented OAuth2 Authentication:** Integrated GitHub and Google sign-ins for seamless authentication.
- **Secure Data Transfer:** Utilizes HTTPS and encrypted password storage.

## Technology Stack
- **Backend:** Java, Spring Boot 
- **Build Tool:** Maven
- **Database:** MySQL
- **Testing:** Postman (API Testing)


## Getting Started
Follow these instructions to set up and run the SecureDocs project locally.

### Prerequisites
- Java 17+
- Maven 3.6+




## Run Locally

Clone the project

```bash
  git clone https://github.com/hesh-ftw/SecureDocs.git
```

Go to the project directory

```bash
  cd SecureDocs
```

Install dependencies

```bash
  ./mvnw clean install

```

Start the server

```bash
  ./mvnw spring-boot:run
   Access the application at http://localhost:8080.

```

