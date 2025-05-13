# Workout Tracker REST API
A simple REST API to let users trackers their workouts and progress.

## Overview
This project is a RESTful API built using Spring Boot that provides endpoints for creating, retrieving, updating and deleting workouts, as well as generating reports.

## Prerequisites
- Java 21 or higher
- Maven 3.6+
- IDE (IntelliJ IDEA, VS Code, Eclipse, etc.)

## Getting Started
### Clone the repository
```shell
git clone "https://github.com/playerblair/spring-boot-workout-tracker"
cd spring-boot-workout-tracker
```
### Build the application
```shell
mvn clean install
```
### Run the application
```shell
mvn spring-boot:run
```
The application will be available at: http://localhost:8080

## API Endpoints
| Method | URL                  | Description                                             |
|--------|----------------------|---------------------------------------------------------|
| POST   | /login               | User login.                                             |
| POST   | /signup              | User registration.                                      |
| GET    | /api/exercises       | Get all exercises.                                      |
| GET    | /api/workouts        | Get user workouts.                                      |
| GET    | /api/workouts/report | Generates a report on workouts and progress.            |
| POST   | /api/workouts        | Allows users to create a workout composed of exercises. |
| PUT    | /api/workouts/{id}   | Allows users to update a workout.                       |
| PATCH  | /api/workouts/{id}   | Allows users to update the schedule of a workout.       |
| DELETE | /api/workouts/{id}   | Delete a workout.                                       |

## Request & Response Examples
### PATCH /signup
Request:
```json
{
  "username": "johndoe",
  "password": "securePassword123"
}
```
### PATCH /login
Request:
```json
{
  "username": "johndoe",
  "password": "securePassword123"
}
```
### GET /api/workouts
Response:
```json
[
  {
    "id": 1,
    "name": "Full Body Workout",
    "exercises": [
      {
        "id": 101,
        "name": "Push-Up",
        "sets": 3,
        "reps": 15
      },
      {
        "id": 102,
        "name": "Squat",
        "sets": 3,
        "reps": 20
      }
    ],
    "dateTime": "2025-05-12T10:30",
    "status": "PENDING",
    "comment": "Morning session workout",
    "userId": 42
  }

]
```
### GET /api/workouts/report
Response:
```text
Workout Report: johndoe

Total Workouts: 2

Workout: Full Body Blast
Date & Time: 2025-05-10T07:30
Status: COMPLETED
Comment: Felt great!
Exercises:
  Name: Push Ups,Reps: 15,Sets: 3,Weights: 0
  Name: Squats,Reps: 20,Sets: 3,Weights: 0
  Name: Dumbbell Rows,Reps: 12,Sets: 3,Weights: 25

Workout: Cardio Session
Date & Time: 2025-05-11T18:00
Status: PLANNED
Comment: Evening cardio planned
Exercises:
  Name: Treadmill,Reps: 1,Sets: 1,Weights: 0
  Name: Jump Rope,Reps: 100,Sets: 2,Weights: 0

```
### POST /api/workouts
Request:
```json
{
  "name": "Leg Day",
  "exercises": [
    {
      "exerciseId": 1,
      "sets": 4,
      "reps": 12,
      "weights": 50
    },
    {
      "exerciseId": 2,
      "sets": 3,
      "reps": 15,
      "weights": 0
    }
  ],
  "dateTime": "2025-05-12T08:00",
  "status": "PENDING",
  "comment": "Targeting quads and hamstrings"
}

```
### PUT /api/workouts/{id}
Request:
```json
{
  "id": 1,
  "name": "Leg Day",
  "exercises": [
    {
      "exerciseId": 1,
      "sets": 4,
      "reps": 12,
      "weights": 50
    },
    {
      "exerciseId": 2,
      "sets": 3,
      "reps": 15,
      "weights": 0
    }
  ],
  "dateTime": "2025-05-12T08:00",
  "status": "COMPLETED",
  "comment": "UPDATED: Targeting quads and hamstrings"
}
```
### PATCH /api/workouts/{id}
Request:
```json
{
  "id": 1,
  "dateTime": "2025-05-12T08:00"
}
```

## Configuration
The application can be configured through the `application.properties` file:
```properties
# Server port
server.port=8080
```

## Running Tests
```shell
mvn tests
```

## Built With
- Spring Boot - The web framework used
- Spring Boot Data JPA - Data persistence
- Spring Boot OAuth2 Resource Server - JWT Authentication and Spring Security
- Maven - Dependency Management

## Source
This project idea came from roadmap.sh, https://roadmap.sh/projects/fitness-workout-tracker.
