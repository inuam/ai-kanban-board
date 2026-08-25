# Builds and starts the app in Docker. Run from anywhere; visit http://localhost:8080 once it's up.
$repoRoot = Split-Path -Parent $PSScriptRoot
Push-Location $repoRoot
try {
    docker compose up --build -d
} finally {
    Pop-Location
}
