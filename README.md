# Netflix Clone — Spring Boot REST API

**Author: Pushkar**

A production-style streaming service API built with **Spring Boot only** (no database, no external
libraries). Data lives in thread-safe in-memory stores, so the project runs immediately with zero setup.

## Highlights

| Concern | Implementation |
|---|---|
| Layered design | `controller` → `service` → `repository`, DTOs never leak entities |
| Validation | Bean Validation (`@Valid`) on every request DTO |
| Error handling | `@RestControllerAdvice` + RFC 7807 `ProblemDetail` |
| Custom filters | request logging + Bearer token auth |
| Password safety | SHA-256 hashing, never stored or logged in plain text |
| Dependency injection | constructor injection everywhere, no field injection |

## Package layout

```
com.pushkar.netflix
├── common      GlobalExceptionHandler
├── config      SeedDataRunner
├── controller  AuthController, MovieController, SubscriptionController
├── dto         request + response records
├── entity      Movie, User, Subscription, Genre, SubscriptionPlan
├── exception   custom runtime exceptions
├── filter      RequestLoggingFilter, AuthFilter
├── repository  in-memory concurrent stores
└── service     AuthService, MovieService, SubscriptionService
```

## Run

```bash
mvn spring-boot:run
```

Server starts on `http://localhost:8080`. Four movies are seeded automatically.

## API

### Auth — public

| Method | Path | Body | Result |
|---|---|---|---|
| POST | `/api/auth/register` | `{"email","password"}` | `201` + `{email, token, registeredAt}` |
| POST | `/api/auth/login` | `{"email","password"}` | `200` + `{email, token, registeredAt}` |

### Movies — public read, protected write

| Method | Path | Notes |
|---|---|---|
| GET | `/api/movies` | optional filter: `?genre=SCI_FI` |
| GET | `/api/movies/{id}` | `404` if missing |
| POST | `/api/movies` | requires Bearer token, `201` |
| PUT | `/api/movies/{id}` | requires Bearer token |
| DELETE | `/api/movies/{id}` | requires Bearer token, `204` |

Movie body:

```json
{
  "title": "Dune Part Three",
  "description": "Paul Atreides unites the Fremen.",
  "releaseYear": 2026,
  "genre": "SCI_FI",
  "rating": 8.4,
  "durationMinutes": 166,
  "releaseDate": "2026-03-20"
}
```

### Subscription — protected

| Method | Path | Notes |
|---|---|---|
| POST | `/api/subscription` | `{"plan":"PREMIUM"}`, creates or upgrades |
| GET | `/api/subscription` | current plan |
| DELETE | `/api/subscription` | deactivates |

Plans: `BASIC` ₹199 / 1 screen, `PREMIUM` ₹499 / 4 screens, `FAMILY` ₹799 / 6 screens.

Protected routes expect a header:

```
Authorization: Bearer <token from login>
```

Missing or invalid token returns `401` with a JSON body.

## Sample run

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email":"demo@netflix.com","password":"secret123"}'

curl -X POST http://localhost:8080/api/movies \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{"title":"Dune Part Three","description":"Paul unites the Fremen.","releaseYear":2026,"genre":"SCI_FI","rating":8.4,"durationMinutes":166,"releaseDate":"2026-03-20"}'

curl "http://localhost:8080/api/movies?genre=THRILLER"
```

## Status codes

`200` ok · `201` created · `204` deleted · `400` validation failed · `401` bad or missing token ·
`404` resource not found · `409` duplicate registration

## Scope note

Authentication is a self-contained token store so the project has no external dependencies. A real
deployment would swap the in-memory repositories for Spring Data JPA and the token store for JWT or
an OAuth2 provider — the service and controller layers stay unchanged.
