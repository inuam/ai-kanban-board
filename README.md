# Project Management MVP

A simple Kanban board web app with an AI chat sidebar that can create, edit, and move cards. Sign in, manage a board with drag-and-drop columns and cards, and chat with an AI assistant that can update the board on your behalf.

See [AGENTS.md](AGENTS.md) for the full business requirements and technical decisions, and [docs/PLAN.md](docs/PLAN.md) for the build plan and progress.

## Status

This is an MVP under active development. Currently implemented:

- Backend scaffolding: a Spring Boot app in `backend/`, packaged in Docker, serving a placeholder page and a sample API endpoint.
- Frontend: a standalone Kanban board demo in `frontend/` (not yet wired into the backend - see [frontend/AGENTS.md](frontend/AGENTS.md)).

Sign-in, persistence, and the AI chat sidebar are not built yet. See `docs/PLAN.md` for what's done and what's next.

## Running the app

Requires Docker Desktop running locally.

```
scripts\start.ps1
```

This builds the backend image and starts it. Once it's up, visit:

- http://localhost:8080 - the app (currently a placeholder page)
- http://localhost:8080/api/hello - a sample JSON API endpoint

To stop it:

```
scripts\stop.ps1
```

## Development

- Backend (Java 21 / Spring Boot / Maven): see [backend/AGENTS.md](backend/AGENTS.md). Run tests with `cd backend; mvn test`.
- Frontend (Next.js): see [frontend/AGENTS.md](frontend/AGENTS.md). Run it standalone with `cd frontend; npm install; npm run dev`.
