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
- Copy `/infrastructure/src/jvmMain/resources/application.conf.example` to `/infrastructure/src/jvmMain/resources/application.conf` (Same Path, without .example)
- In `/infrastructure/src/jvmMain/resources/application.conf` configure your database settings
- If you want to contribute to the project run `./gradlew addKtlintCheckGitPreCommitHook` beforehand
- execute `yarn install` in `/web/src/angular` to install frontend packages

### Run

- run `./gradlew runShadow` to start backend
- run `yarn start` in `/web/src/angular` to start frontend

### Notes

- When running ktor in Dev Mode, it will attempt to seed a dev user. See Console output for more information.
- Frontend runs on port 4200, backend on port 8000. Frontend requests are getting proxied by angular to port 8000, configure in `/web/src/angular/proxy.conf.json`
- Frontend hot reloads, backends needs to get restarted.
- For faster starting time of the backend, comment out `commonMainRuntimeOnly(project(":replace-web"))` in `/build.gradle.kts`.
  - Should look like this:

  ```kotlin

  dependencies {
    jvmMainImplementation(project(":replace-application"))
    jvmMainImplementation(project(":replace-infrastructure"))
    //commonMainRuntimeOnly(project(":replace-web"))
  }

  ```

- frontend won't be compiled and served anymore by Ktor, which is irrelevant in Dev, since we are using the angular dev server
- You can execute `./gradlew ktlintCheck` and `./gradlew ktlintFormat` to check for backend formatting (alternatively ctrl + ctrl in intellij ot execute gradle commands)
