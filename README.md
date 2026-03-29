# 🏀 Basketball Pickup Game API
Backend service built with **Java** and **Spring Boot** that allows users to organize pickup basketball games, join games, and automatically generate balanced teams based on player skill levels.

Includes an **AI-powered** feature that lets users to create games using natural language (e.g. "I want to play a 5v5 game tomorrow at 7pm in Brownsville, FL")

## ⭐️ Features
- Player registration
- Create pickup games
- Join games with skill rating
- Generate balanced teams
- AI-based game creation from natural langauge

## 🛠 Tech Stack
- Java
- Spring Boot
- MySQL
- REST API

## 🚀 Building and Running
### Prerequisites
- **Java 25** installed.
- **MySQL** database running locally (default port is `3306`).

### Database Configuration
Update your `src/main/resources/application.properties` to match your local MySQL setup. For example:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/{your_database_name}
spring.datasource.username={your_username}
spring.datasource.password={your_password}
spring.jpa.hibernate.ddl-auto=update
```

### Commands
- **Build the project:**
  ```bash
  ./mvnw clean install
  ```
- **Run the application:**
  ```bash
  ./mvnw spring-boot:run
  ```
- **Run tests:**
  ```bash
  ./mvnw test
  ```

## 📡 API Endpoints
### Players
- `POST /players/register`
- `GET /players/{id}`
- `GET /players (params: [name], [bornAfter], [page=0], [size=2])`
- `PUT /players/{id}`
- `PUT /players/{id}/password`
- `PATCH /players/{id}`
- `DELETE /players/{id}`

## 📥 Example request
### Player registration
- `POST /players/register`

**Request body:**
```json
{
    "name": "Kevin",
    "lastName": "Durant",
    "birthDate": "1988-09-29",
    "email": "kd@gmail.com",
    "password": "*********"
}
```

**Response body:**
```json
{
    "id": 1,
    "name": "Kevin",
    "lastName": "Durant",
    "birthDate": "1988-09-29",
    "email": "kd@gmail.com",
    "_links": {
        "self": {
            "_href": "http://localhost:8080/players/1"
        }
    }
}
```
