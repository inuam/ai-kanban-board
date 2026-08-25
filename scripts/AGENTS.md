Windows PowerShell scripts to build and run the app locally in Docker (Mac/Linux scripts are not needed for this project).

- `start.ps1` - runs `docker compose up --build -d` from the repo root, then the app is available at http://localhost:8080
- `stop.ps1` - runs `docker compose down` to stop and remove the container