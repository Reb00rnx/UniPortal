# 🎓 UniPortal — Backend

> Spring Boot REST API for the UniPortal university management system.



---

## ✨ Features

- JWT authentication with role-based access (STUDENT / TEACHER)
- Course management — enrollment, modules, details
- Grade management — assign and view grades per student
- Schedule management — weekly calendar events
- Teacher consultation slots
- Automatic test data seeding on startup (DataLoader)

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Framework | Spring Boot 3.3 |
| Language | Java 21 |
| Security | Spring Security + JWT (jjwt) |
| Persistence | Spring Data JPA / Hibernate |
| Database | PostgreSQL (prod) / H2 in-memory (dev) |
| Build | Maven |
| Deploy | Render (Docker) |

## 🚀 Run Locally

Requires Java 21 and Maven.

```bash
./mvnw spring-boot:run
```

App runs at `http://localhost:8080`. Uses H2 in-memory database by default — no setup needed.

H2 console available at: `http://localhost:8080/h2-console`

## 📁 Structure

```
src/main/java/com/uniportal/
├── Auth/             # Login & register DTOs
├── Config/           # DataLoader (seed data)
├── Controller/       # REST controllers
├── Course/           # Course, Grade, Module entities & DTOs
├── Enums/            # Role, GradeValue, Day...
├── Exceptions/       # Global exception handling
├── Repository/       # JPA repositories
├── Schedule/         # Schedule entity & DTOs
├── SecurityConfig/   # JWT filter, SecurityFilterChain, CORS
├── Service/          # Business logic
└── User/             # Student, Teacher, User entities & DTOs
```

## 🔐 API Endpoints

| Method | Endpoint | Auth |
|---|---|---|
| POST | `/api/auth/authenticate` | Public |
| POST | `/api/auth/register/student` | Public |
| POST | `/api/auth/register/teacher` | Public |
| GET | `/api/courses/all` | STUDENT / TEACHER |
| POST | `/api/courses/{id}/enroll/{studentId}` | STUDENT |
| GET | `/api/grade/student/{id}` | STUDENT / TEACHER |
| GET | `/api/schedule/...` | Authenticated |
| GET/PUT | `/api/users/...` | Authenticated |

## 🔐 Test Accounts

| Role | Email | Password |
|---|---|---|
| Student | alice.brown@student.uni.edu | Student123! |
| Teacher | john.smith@uni.edu | Teacher123! |

---

*First full-stack portfolio project. Built to learn Java, Spring Boot, and REST API design from scratch.*
