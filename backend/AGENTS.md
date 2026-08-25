# The Project Management MVP web app

## Business Requirements

## Technical Decisions

- Java Spring Boot backend, including serving the static NextJS site at /
- Use JDK 21 for the backend
- Clean code with clean architecture principles, including separation of concerns and dependency injection
- Use SOLID principles for maintainable and extensible code with clean unit tests
- Test behaviour of the system, not implementation details, with unit tests and integration tests
- Everything packaged into a Docker container
- Use "mvn" as the package manager for java in the Docker container
- Keep it simple - NEVER over-engineer, ALWAYS simplify, NO unnecessary defensive programming. No extra features - focus on simplicity.
- prove problems with evidence before fixing them.

## Existing Code

Generated with Spring Boot 4.1.1 / Java 21 via start.spring.io (dependency: Spring Web).

- `pom.xml` - Maven project, `spring-boot-starter-webmvc` for the web app, `spring-boot-starter-webmvc-test` + `spring-boot-restclient` for tests (JUnit 5, AssertJ, MockMvc, TestRestTemplate)
- `src/main/java/com/pm/backend/BackendApplication.java` - Spring Boot entry point
- `src/main/java/com/pm/backend/hello/HelloController.java` - `GET /api/hello`, returns a `HelloResponse` JSON record
- `src/main/resources/static/index.html` - placeholder "hello world" page, served automatically at `/` by Spring Boot's static resource handling (no controller needed); replaced by the built frontend in Part 3
- `src/test/java/.../hello/HelloControllerTest.java` - unit test, calls the controller directly
- `src/test/java/.../hello/HelloControllerIntegrationTest.java` - integration test, boots the full app on a random port and hits `/` and `/api/hello` over real HTTP via `TestRestTemplate`
- `Dockerfile` - multi-stage build: `maven:3.9-eclipse-temurin-21` builds the jar, `eclipse-temurin:21-jre` runs it

## Running locally

From the repo root: `scripts\start.ps1` builds the Docker image and starts the container (`docker compose up --build -d`), then the app is at http://localhost:8080. Stop it with `scripts\stop.ps1`.

To run tests directly: `cd backend; mvn test` (requires JDK 21 on PATH or JAVA_HOME set to a JDK 21 install).

## Working documentation

All documents for planning and executing this project will be in the docs/ directory.
Please review the docs/PLAN.md document before proceeding.