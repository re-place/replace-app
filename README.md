# Replace App

## Technologien

- Backend: [Ktor](https://ktor.io/)
- Datatabase: [PostgreSQL](https://www.postgresql.org/)
- Frontend:
  - [Angular](https://angular.io/)
  - [Angular Material](https://material.angular.io/)
  - [Tailwind CSS](https://tailwindcss.com/)

## Dev Setup

### Requirements

- Java 17
- Yarn 2
- Node
- Angular CLI

### Setup

- Clone Repo
- Settings are applied per default though `/infrastructure/src/jvmMain/resources/application.conf`, these include Database and OAuth configuration. A example is provided in `/infrastructure/src/jvmMain/resources/application-env.conf`, which is used for building the application
- If you want to contribute to the project run `./gradlew addKtlintCheckGitPreCommitHook` beforehand
- execute `yarn install` in `/web/src/angular` to install frontend packages

### Run

- run `./gradlew runShadow` to start backend
- run `yarn start` in `/web/src/angular` to start frontend

### DevOps

- The application is built using Jenkins and Docker. Traefik is used as reverse proxy to serve the application.

### Notes

- When running ktor in Dev Mode, it will attempt to seed a dev user. See Console output for more information.
- Frontend runs on port 4200, backend on port 8000. Frontend requests are getting proxied by angular to port 8000, configure in `/web/src/angular/proxy.conf.json`
- Frontend hot reloads, backends needs to get restarted.
- You can execute `./gradlew ktlintCheck` and `./gradlew ktlintFormat` to check for backend formatting (alternatively ctrl + ctrl in intellij ot execute gradle commands)
