# Project Management MVP

A simple Kanban board web app with an AI chat sidebar that can create, edit, and move cards. Sign in, manage a board with drag-and-drop columns and cards, and chat with an AI assistant that can update the board on your behalf.

See [AGENTS.md](AGENTS.md) for the full business requirements and technical decisions, and [docs/PLAN.md](docs/PLAN.md) for the build plan and progress.

## Status

This is an MVP under active development. Currently implemented:

- Backend scaffolding: a Spring Boot app at the repo root (`pom.xml`, `src/`), packaged in Docker.
- Frontend: a Kanban board demo (drag-and-drop columns and cards) in `frontend/`, statically exported and served by the backend at `/` - see [frontend/AGENTS.md](frontend/AGENTS.md).

The Kanban board has no persistence yet - it resets on refresh. Sign-in, a real database, and the AI chat sidebar are not built yet. See `docs/PLAN.md` for what's done and what's next.

## Running the app

Requires Docker Desktop running locally.

```
scripts\start.ps1
```

This builds the frontend and backend images and starts the container. Once it's up, visit:

- http://localhost:8080 - the Kanban board
- http://localhost:8080/api/hello - a sample JSON API endpoint

To stop it:

```
scripts\stop.ps1
```

## Development

- Backend (Java 21 / Spring Boot / Maven, project root): see [AGENTS.md](AGENTS.md). Run tests with `mvn test`.
- Frontend (Next.js): see [frontend/AGENTS.md](frontend/AGENTS.md). Run it standalone with `cd frontend; npm install; npm run dev`.
