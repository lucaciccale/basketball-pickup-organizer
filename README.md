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
Copy the `.env.example` file to a new file named `.env` and update the values with your local MySQL setup:

```bash
cp .env.example .env
```

Edit the `.env` file:
```env
DB_URL=jdbc:mysql://localhost:3306/your_database_name
DB_USERNAME=your_username
DB_PASSWORD=your_password
```

### Commands
- **Run the application:**
  ```bash
  ./run.sh
  ```
- **Clean and run the application:**
  ```bash
  ./run.sh clean
  ```
- **Run tests:**
  ```bash
  ./run.sh test
  ```

## 📡 API Endpoints
### Players
- `POST /players`
- `GET /players/{id}`
- `GET /players (params: [name], [bornAfter], [page=0], [size=2])`
- `PUT /players/{id}`
- `PUT /players/{id}/password`
- `PATCH /players/{id}`
- `DELETE /players/{id}`

### Games
- `POST /games`
- `POST /games/{id}/cancel`
- `GET /games/{id}`
- `GET /games (params: [status], [from], [to], [page=0], [size=2])`
- `PATCH /games/{id}`
- `DELETE /games/{id}`

## 📥 Example request
### Player registration
- `POST /players`

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
