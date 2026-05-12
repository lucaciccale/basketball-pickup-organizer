# 🏀 Basketball Pickup Game API

<p align="center">
  <img src="https://img.shields.io/badge/Java-25-orange?style=for-the-badge&logo=openjdk" alt="Java 25">
  <img src="https://img.shields.io/badge/Spring%20Boot-4.0.3-brightgreen?style=for-the-badge&logo=springboot" alt="Spring Boot 4.0.3">
  <img src="https://img.shields.io/badge/MySQL-8.0+-blue?style=for-the-badge&logo=mysql" alt="MySQL">
  <img src="https://img.shields.io/badge/API-REST-red?style=for-the-badge" alt="REST API">
</p>

---

> [!WARNING]
> **Work in Progress:** This project is currently under active development and is **not ready for production use**.

### 🌟 Overview
A robust backend service built with **Java** and **Spring Boot** designed to streamline pickup basketball organization. It enables players to register, create games, and join matches with an automated team balancing system based on skill levels.

> [!TIP]
> **AI-Powered:** Create games using natural language! 
> *Example: "I want to play a 5v5 game tomorrow at 7pm in Brownsville, FL"*

---

## ✨ Key Features
- 👤 **Player Management:** Seamless registration and profile updates.
- 🏟 **Game Coordination:** Create and manage pickup games at various locations.
- 🤝 **Seamless Joining:** Join games with a skill rating for better matchmaking.
- ⚖️ **Team Balancing:** Automatically generate balanced teams based on player skill.
- 🤖 **AI Integration:** Natural language processing for intuitive game creation.

---

## 🛠 Tech Stack
| Technology | Usage |
| :--- | :--- |
| **Java 25** | Core language of the project |
| **MySQL** | Relational Database |
| **Spring Boot 4** | Core Framework & REST API |
| **Spring Data JPA** | Database interaction and entity mapping |
| **Spring HATEOAS** | Hypermedia-driven API responses |

---

## 🚀 Getting Started

### 📋 Prerequisites
- **Java 25** installed and configured.
- **MySQL** database running locally (default: `3306`).

### ⚙️ Database Configuration
1. Initialize your environment:
   ```bash
   cp .env.example .env
   ```
2. Configure your `.env` file with local credentials:
   ```env
   DB_URL=jdbc:mysql://localhost:3306/your_database_name
   DB_USERNAME=your_username
   DB_PASSWORD=your_password
   ```

### 💻 Commands
| Action | Command |
| :--- | :--- |
| **Run App** | `./run.sh` |
| **Clean & Run** | `./run.sh clean` |
| **Run Tests** | `./run.sh test` |

---

## 📡 API Reference

### 👥 Players
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/players` | **Register** a new player |
| `GET` | `/players/{id}` | **List** player details |
| `GET` | `/players` | **Search** players (name, bornAfter, page, size) |
| `PUT` | `/players/{id}` | **Replace** player data |
| `PUT` | `/players/{id}/password` | **Update** player password |
| `PATCH` | `/players/{id}` | **Partial** update player |
| `DELETE` | `/players/{id}` | **Remove** player |

### 🏀 Games
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/games` | **Create** a new game |
| `POST` | `/games/{id}/players` | **Join** a game |
| `POST` | `/games/{id}/cancel` | **Cancel** a game |
| `GET` | `/games/{id}` | **List** game details |
| `GET` | `/games` | **List** games (status, from, to, page, size) |
| `PATCH` | `/games/{id}` | **Update** game details |
| `DELETE` | `/games/{gameId}/players/{playerId}` | **Leave** a game |
| `DELETE` | `/games/{id}` | **Delete** a game |

---

## 📥 Usage Example

### Player Registration
`POST /players`

**Request Body:**
```json
{
    "name": "Kevin",
    "lastName": "Durant",
    "birthDate": "1988-09-29",
    "email": "kd@gmail.com",
    "password": "*********"
}
```

**Response Body (201 Created):**
```json
{
    "id": 1,
    "name": "Kevin",
    "lastName": "Durant",
    "birthDate": "1988-09-29",
    "email": "kd@gmail.com",
    "_links": {
        "self": {
            "href": "http://localhost:8080/players/1"
        }
    }
}
```
