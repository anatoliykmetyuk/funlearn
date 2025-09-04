# PowerShell script to run Flyway migrations via Docker
# Usage: .\migrate.ps1

# Pull the Flyway Docker image
docker pull flyway/flyway:latest

# Run Flyway migrate command
docker run --rm `
  -v "${PWD}/migrations:/flyway/sql" `
  -v "${PWD}/data:/flyway/data" `
  flyway/flyway:latest `
  -url="jdbc:sqlite:/flyway/data/database.db" `
  migrate
