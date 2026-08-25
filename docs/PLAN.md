# High level steps for project

This plan is a checklist. Each part must be completed, tested, and (where noted) approved by the user before moving to the next part. Check off substeps as they are completed.

---

## Part 1: Plan

Enrich this document to plan out each part in detail, with substeps listed as a checklist, and tests/success criteria for each. Also create an AGENTS.md file inside frontend/ describing the existing code there. Get user approval before proceeding.

- [x] Explore frontend/ codebase (structure, components, lib, tests, tooling)
- [x] Draft enriched docs/PLAN.md with checklists and success criteria for Parts 1-10
- [x] Draft frontend/AGENTS.md describing existing frontend code
- [x] Present both documents to the user for review
- [x] Get explicit user approval to proceed to Part 2

**Success criteria:** User has reviewed docs/PLAN.md and frontend/AGENTS.md and explicitly approved proceeding. (Done.)

---

## Part 2: Scaffolding

Set up Docker infrastructure, a Spring Boot backend skeleton at the repo root, and start/stop scripts in scripts/ (Windows only for now). Serve example static HTML confirming a "hello world" example works locally, and confirm a simple API call works.

- [x] Initialize Spring Boot project at the repo root (Java 21, Maven, per AGENTS.md decisions) - a conventional Maven layout (pom.xml + src/) alongside frontend/, docs/, scripts/
- [x] Add a minimal controller serving a static "hello world" HTML page at / (served via Spring Boot's default static resource handling, no controller needed)
- [x] Add a minimal REST endpoint (GET /api/hello) returning a JSON payload
- [x] Write a Dockerfile that builds the backend with mvn and runs it
- [x] Write docker-compose to run the container locally, mapping port 8080
- [x] Write scripts/start.ps1 and scripts/stop.ps1 (Windows) to build/run and stop the container
- [x] Document how to run locally in AGENTS.md and scripts/AGENTS.md

**Status: Done.** Verified end-to-end: `scripts/start.ps1` builds the Docker image and starts the container, `GET /` returns the static hello page, `GET /api/hello` returns `{"message":"Hello, World!"}`, and `scripts/stop.ps1` cleanly stops and removes the container. Backend unit test (`HelloControllerTest`) and integration tests (`HelloControllerIntegrationTest`, `BackendApplicationTests`) all pass via `mvn test`.

The Maven project was moved from a `backend/` subfolder to the repo root (`pom.xml`, `src/` alongside `frontend/`, `docs/`, `scripts/`) for a conventional Maven layout; `docker-compose.yml`'s build context and a new `.dockerignore` were updated accordingly.

**Tests / success criteria:**
- Running the start script builds and starts the container without manual steps
- Visiting the local URL in a browser shows the static "hello world" page
- Calling the sample API endpoint (curl or browser) returns the expected JSON
- Running the stop script cleanly stops the container
- A basic Spring Boot test (e.g. MockMvc test on the hello endpoint) passes

---

## Part 3: Add in Frontend

Statically build and serve the frontend so the app shows the demo Kanban board at /, replacing the temporary hello-world page.

- [ ] Configure Next.js for static export (`output: "export"`) so the build produces plain static HTML/JS/CSS
- [ ] Update backend to serve the built frontend static assets at /
- [ ] Update Docker build to run `npm run build` for frontend and copy the exported output into the backend image
- [ ] Remove/retire the Part 2 placeholder hello-world page
- [ ] Update start/stop scripts if the build process changes
- [ ] Keep and extend existing frontend unit tests (Vitest) and e2e tests (Playwright) to run against the served build

**Tests / success criteria:**
- `npm run test:all` (unit + e2e) passes against the frontend in isolation
- After the start script runs, visiting / in a browser shows the demo Kanban board (drag and drop, add/delete card, rename column all work) with no backend API wiring yet
- Existing Playwright suite passes when pointed at the Docker-served app (baseURL updated or parameterized)

---

## Part 4: Add in a fake user sign in experience

Require login with hardcoded credentials ("user"/"password") before the Kanban is visible, with a working logout. Implemented client-side only (no backend session yet - that arrives naturally once Part 6 adds the API).

- [ ] Design login page/route in frontend (simple form, error message on bad credentials)
- [ ] Add a client-side auth gate: a localStorage/cookie flag set on successful login, checked before rendering the Kanban
- [ ] Redirect unauthenticated users from / to the login page
- [ ] Add logout control (visible when signed in) that clears the flag and redirects to login
- [ ] Update frontend/AGENTS.md with the new auth-related files
- [ ] Add unit tests for login form validation and the auth gate logic
- [ ] Add Playwright e2e test: cannot see Kanban without logging in, can log in with correct credentials, cannot log in with wrong credentials, can log out and lose access again

**Tests / success criteria:**
- Visiting / while logged out redirects to login, not the Kanban
- Logging in with "user"/"password" grants access to the Kanban
- Logging in with wrong credentials shows an error and does not grant access
- Logging out removes access until logging in again
- All unit and e2e tests pass

---

## Part 5: Database modeling

Propose a database schema for the Kanban and get user sign-off before implementing.

- [ ] Design schema covering: users, board (1 per user for MVP), columns, cards, and ordering fields needed for card position within a column
- [ ] Map the schema to the existing frontend BoardData shape (columns, cards, cardIds ordering) so translation to/from the API is straightforward
- [ ] Document the schema (tables, columns, types, relationships) in docs/DATABASE.md
- [ ] Note SQLite-specific considerations (file location, auto-create behavior)
- [ ] Present schema to user for review

**Tests / success criteria:**
- docs/DATABASE.md exists and clearly documents the schema
- User explicitly signs off on the schema before Part 6 begins

---

## Part 6: Backend

Add API endpoints for the backend to read and change the Kanban board for a given (fake-authenticated) user, using granular endpoints matching the frontend's existing per-action handlers. Database is auto-created if missing.

- [ ] Add SQLite dependency and configure database file location
- [ ] Implement startup logic that creates the database/schema if it does not exist
- [ ] Implement entities/repositories per docs/DATABASE.md
- [ ] Implement API endpoints:
  - [ ] GET /api/board - returns the current user's board (columns + cards)
  - [ ] PATCH /api/columns/{id} - rename a column
  - [ ] POST /api/cards - add a card to a column
  - [ ] DELETE /api/cards/{id} - delete a card
  - [ ] PATCH /api/cards/{id}/move - move a card to a column/position (backing the drag-and-drop `moveCard` logic)
- [ ] Seed the database with the existing initialData mock content on first run
- [ ] Write backend unit tests for repository and service logic
- [ ] Write backend integration tests (MockMvc/Spring Boot Test) for each endpoint, covering success and basic error cases

**Tests / success criteria:**
- Deleting the SQLite file and restarting the backend recreates it automatically with seed data
- Unit tests cover board read/update logic in isolation
- Integration tests confirm each endpoint returns correct status codes and payloads
- All backend tests pass (`mvn test`)

---

## Part 7: Frontend + Backend

Wire the frontend to the real backend API so the board persists.

- [ ] Replace initialData/local state in KanbanBoard with data fetched from GET /api/board on load
- [ ] Call the Part 6 endpoints from handleAddCard, handleDeleteCard, handleRenameColumn, and the drag-end handler (moveCard) instead of only updating local state
- [ ] Handle loading and error states in the UI
- [ ] Update frontend unit tests (mock the API) and Playwright e2e tests to verify persistence (e.g. reload the page and see the same state)
- [ ] Update frontend/AGENTS.md to reflect the new API layer

**Tests / success criteria:**
- Adding, deleting, moving a card, or renaming a column persists across a full page reload
- Frontend unit tests pass with the API layer mocked
- Playwright e2e tests pass against the full Dockerized app (frontend + backend + real SQLite)

---

## Part 8: AI connectivity

Allow the backend to call the AI via OpenRouter using OPENROUTER_API_KEY from .env and model `openai/gpt-oss-120b`.

- [ ] Add an OpenRouter client/service in the backend, reading OPENROUTER_API_KEY from .env
- [ ] Implement a simple connectivity-check endpoint or service method that asks the AI "What is 2+2?" and returns the response
- [ ] Confirm .env is gitignored so the key is never committed
- [ ] Write a backend test that calls the connectivity check (mocking the HTTP call)

**Tests / success criteria:**
- Calling the connectivity check returns a response from the AI containing "4"
- Unit test with a mocked OpenRouter response verifies request formatting and response parsing
- No API key is hardcoded or logged

---

## Part 9: AI Kanban integration

Extend the backend AI call so it always sends the full Kanban JSON, the user's question, and conversation history. The AI responds with Structured Outputs containing a reply and an optional Kanban update.

- [ ] Define the Structured Output schema (e.g. JSON schema with `reply: string` and `boardUpdate: BoardData | null`)
- [ ] Implement a chat endpoint, e.g. POST /api/chat, accepting { message, history }
- [ ] Build the AI request: system prompt + full current board JSON + conversation history + user's message
- [ ] Parse the Structured Output response; if boardUpdate is present, apply it via the Part 6 board-update logic
- [ ] Keep conversation history in-memory per session (accepted MVP simplification - not persisted to the database)
- [ ] Write backend unit tests mocking the OpenRouter call: reply-only responses, and responses that include a board update
- [ ] Write an integration test verifying that a board-updating AI response is actually persisted to the database

**Tests / success criteria:**
- Given a mocked AI response with only a reply, the chat endpoint returns the reply and the board is unchanged
- Given a mocked AI response with a board update, the endpoint returns the reply and the board in the database reflects the update
- All tests pass without needing a live OpenRouter call

---

## Part 10: AI chat sidebar UI

Add a sidebar widget for full AI chat, letting the LLM update the Kanban per its Structured Output, with the UI auto-refreshing on update.

- [ ] Design and build a chat sidebar component (message list, input box, send button) matching the color scheme in AGENTS.md
- [ ] Wire the sidebar to POST /api/chat, sending the user's message and running conversation history
- [ ] Display the AI's reply in the chat log
- [ ] When the response includes a board update, refresh the Kanban board state in the UI immediately (no manual reload)
- [ ] Add loading/error states for the chat (e.g. while waiting for the AI)
- [ ] Update frontend/AGENTS.md with the new chat components
- [ ] Write frontend unit tests for the chat component (mocking the API)
- [ ] Write a Playwright e2e test: send a message that requests a card change, verify the reply appears and the board visibly updates

**Tests / success criteria:**
- Sending a chat message shows the AI's reply in the sidebar
- A chat message that results in a board update visibly updates the Kanban board without a manual page reload
- All frontend and e2e tests pass against the full Dockerized app
