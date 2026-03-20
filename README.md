# 🏀 Basketball Pickup Game API
Backend service built with Java and Spring Boot that allows users to organize pickup basketball games, join games, and automatically generate balanced teams based on player skill levels.

Includes an AI-powered feature that lets users to create games using natural language (e.g. "I want to play a 5v5 game tomorrow at 7pm in Brownsville, FL")

## 🚀 Features
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

## 📡 API Endpoints
### Players
- POST /players/register
- GET /players/{id}

## 📥 Example request
### Player registration
- POST /players/register

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