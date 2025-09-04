# Coworking Hub (Spring Boot)

## Pokretanje
1. `mvn spring-boot:run` (Java 17)
2. Swagger UI: `http://localhost:8080/swagger-ui.html`

## Koraci
- POST `/auth/register` (kreiraj MEMBER i MANAGER)
- Autorizuj se u Swagger-u (HTTP Basic)
- MANAGER: POST `/rooms`
- MEMBER: POST `/reservations`
- MANAGER: GET `/reservations?status=PENDING`, zatim PUT `/reservations/{id}/approve` ili `/reject?reason=...`

## Napomene
- Baza H2 (in-memory) za brzo testiranje.
- U `docs/sql/it355-jun.sql` je MySQL/H2 kompatibilna šema koju si poslao.
