# The Project Management MVP web app

## Business Requirements

This project is building a Project Management App. Key features:
- A user can sign in
- When signed in, the user sees a Kanban board representing their project
- The Kanban board has fixed columns that can be renamed
- The cards on the Kanban board can be moved with drag and drop, and edited
- There is an AI chat feature in a sidebar; the AI is able to create / edit / move one or more cards

## Limitations

For the MVP, there will only be a user sign in (hardcoded to 'user' and 'password') but the database will support multiple users for future.

For the MVP, there will only be 1 Kanban board per signed in user.

For the MVP, this will run locally (in a docker container)

## Technical Decisions

- NextJS frontend, in frontend/
- Java Spring Boot backend, including serving the static NextJS site at /. The Maven project lives at the repo root (pom.xml, src/) rather than in its own subfolder
- Use JDK 21 for the backend
- Clean code with clean architecture principles, including separation of concerns and dependency injection
- Use SOLID principles for maintainable and extensible code with clean unit tests
- Test behaviour of the system, not implementation details, with unit tests and integration tests
- Everything packaged into a Docker container
- Use "mvn" as the package manager for java in the Docker container
- Use OpenRouter for the AI calls. An OPENROUTER_API_KEY is in .env in the project root
- Use `openai/gpt-oss-120b` as the model
- Use SQLLite local database for the database, creating a new db if it doesn't exist
- Start and Stop server scripts for Mac, PC, Linux in scripts/

## Starting Point

A working MVP of the frontend has been built and is already in frontend. As of Part 3 it's statically exported and served by the backend in Docker; it's still a pure frontend-only demo with no persistence or backend API wiring.

## Backend - Existing Code

Generated with Spring Boot 4.1.1 / Java 21 via start.spring.io (dependency: Spring Web).

- `pom.xml` - Maven project, `spring-boot-starter-webmvc` for the web app, `spring-boot-starter-webmvc-test` + `spring-boot-restclient` for tests (JUnit 5, AssertJ, MockMvc, TestRestTemplate)
- `src/main/java/com/pm/backend/BackendApplication.java` - Spring Boot entry point
- `src/main/java/com/pm/backend/hello/HelloController.java` - `GET /api/hello`, returns a `HelloResponse` JSON record
- `src/main/resources/static/` - empty in source control; the frontend's static export is copied in here at Docker build time (see Dockerfile below) and served automatically at `/` by Spring Boot's static resource handling - no controller needed. Without a Docker build (e.g. plain `mvn test`), `/` returns 404, which `HelloControllerIntegrationTest` asserts explicitly
- `src/test/java/.../hello/HelloControllerTest.java` - unit test, calls the controller directly
- `src/test/java/.../hello/HelloControllerIntegrationTest.java` - integration test, boots the full app on a random port and hits `/api/hello` over real HTTP via `TestRestTemplate`
- `Dockerfile` - three-stage build: `node:22-alpine` builds the frontend's static export (`frontend/out`), `maven:3.9-eclipse-temurin-21` copies that into `src/main/resources/static` and builds the jar, `eclipse-temurin:21-jre` runs it

## Running locally

From the repo root: `scripts\start.ps1` builds the Docker image and starts the container (`docker compose up --build -d`), then the app is at http://localhost:8080. Stop it with `scripts\stop.ps1`.

To run backend tests directly: `mvn test` from the repo root (requires JDK 21 on PATH or JAVA_HOME set to a JDK 21 install).

## Color Scheme

- Accent Yellow: `#ecad0a` - accent lines, highlights
- Blue Primary: `#209dd7` - links, key sections
- Purple Secondary: `#753991` - submit buttons, important actions
- Dark Navy: `#032147` - main headings
- Gray Text: `#888888` - supporting text, labels

## Coding standards

1. Use latest versions of libraries and idiomatic approaches as of today
2. Keep it simple - NEVER over-engineer, ALWAYS simplify, NO unnecessary defensive programming. No extra features - focus on simplicity.
3. Be concise. Keep README minimal. IMPORTANT: no emojis ever
4. When hitting issues, always identify root cause before trying a fix. Do not guess. Prove with evidence, then fix the root cause.

## Working documentation

All documents for planning and executing this project will be in the docs/ directory.
Please review the docs/PLAN.md document before proceeding.