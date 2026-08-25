# The Project Management MVP web app - Frontend

## Business Requirements

See the top-level AGENTS.md for full business requirements. This document describes the existing frontend code and frontend-specific technical decisions.

The frontend currently implements only the Kanban board demo (no auth, no backend, no AI chat). Those are added in later parts of docs/PLAN.md.

## Technical Decisions

- Next.js (App Router), React 19, TypeScript
- Static export (`output: "export"` in next.config.ts) - the backend serves the build output (`out/`) as plain static files at /, no Node server involved in production
- Tailwind CSS v4, styled with CSS variables matching the top-level color scheme
- @dnd-kit/core and @dnd-kit/sortable for drag and drop
- Vitest + React Testing Library for unit tests
- Playwright for end-to-end tests
- Keep it simple - no over-engineering, no unnecessary defensive programming

## Existing Code

### Structure

- `src/app/layout.tsx` - root layout, loads fonts and sets page metadata
- `src/app/page.tsx` - single route `/`, renders `<KanbanBoard />`
- `src/app/globals.css` - Tailwind import plus the color scheme as CSS variables (`--accent-yellow`, `--primary-blue`, `--secondary-purple`, `--navy-dark`, `--gray-text`, matching the top-level AGENTS.md exactly) and structural tokens (`--surface`, `--surface-strong`, `--stroke`, `--shadow`)
- `src/components/KanbanBoard.tsx` - owns all board state via `useState`, sets up the dnd-kit `DndContext`, and holds the mutation handlers: `handleAddCard`, `handleDeleteCard`, `handleRenameColumn`, plus drag handling via `moveCard`. This component is the natural integration point for wiring in a real backend API (Part 7) and for applying AI-driven board updates (Part 10).
- `src/components/KanbanColumn.tsx` - renders a column and its cards, sortable drop target, rename input, add-card form
- `src/components/KanbanCard.tsx` - renders a single draggable/sortable card with a delete button
- `src/components/KanbanCardPreview.tsx` - non-interactive card visual used inside `DragOverlay` while dragging
- `src/components/NewCardForm.tsx` - inline toggle form (title + details) for adding a card to a column

### Data model (`src/lib/kanban.ts`)

- `Card = { id, title, details }`
- `Column = { id, title, cardIds }`
- `BoardData = { columns: Column[], cards: Record<id, Card> }` - normalized shape (columns hold ordered card id lists, cards are looked up by id), chosen to map cleanly onto future database tables
- `initialData` - hardcoded mock seed data: 5 columns, 8 cards. There is no persistence; the board resets to `initialData` on every page refresh.
- `moveCard(columns, activeId, overId)` - pure function implementing reorder-within-column and move-across-column logic, used by `KanbanBoard`'s drag end handler
- `createId(prefix)` - generates a client-side id, used when creating new cards

### Drag and drop

Built with `@dnd-kit/core` and `@dnd-kit/sortable`. Uses `PointerSensor` with a 6px activation distance (avoids accidental drags on click) and `closestCorners` for collision detection.

### Tests

- Unit tests (Vitest + Testing Library): `src/lib/kanban.test.ts` (covers `moveCard`), `src/components/KanbanBoard.test.tsx` (renders columns, renames a column, adds/removes a card through the UI)
- Test setup: `src/test/setup.ts` (imports `jest-dom`), `src/test/vitest.d.ts`
- End-to-end tests (Playwright): `tests/kanban.spec.ts` - board loads with 5 columns, add a card via UI, drag a card between columns. By default baseURL is `http://127.0.0.1:3000` and Playwright auto-starts `npm run dev`. Set `PLAYWRIGHT_BASE_URL` (e.g. `http://127.0.0.1:8080` once the Docker container is running) to run the same suite against an already-running server instead - Playwright skips starting its own dev server whenever that variable is set.

### npm scripts

- `dev`, `build`, `start` - Next.js dev/build/start
- `lint` - ESLint
- `test` / `test:unit` - Vitest run
- `test:unit:watch` - Vitest watch mode
- `test:e2e` - Playwright
- `test:all` - unit then e2e

### Not yet implemented

There is no auth, no API/data-fetching layer, and no AI chat code in the frontend today. These are built from scratch in Parts 4, 7, and 10 of docs/PLAN.md.
