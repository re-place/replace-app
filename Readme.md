# Replace App

## Technologien

- Backend: [Ktor](https://ktor.io/)
- Datatabase: [MongoDB](https://www.mongodb.com)
- Frontend: [Angular](https://angular.io/)
  - [Angular Material](https://material.angular.io/)
  - [Tailwind CSS](https://tailwindcss.com/)

## Dev Setup

### Requirements

- Java 17
- Yarn 2
- Node

### Setup

- Clone Repo
- Copy `/infrastructure/src/jvmMain/resources/application.conf.example` to `/infrastructure/src/jvmMain/resources/application.conf` (Same Path, without .example)
- In `/infrastructure/src/jvmMain/resources/application.conf` configure your database settings
- If you want to contribute to the project run `./gradlew addKtlintCheckGitPreCommitHook` beforehand
- execute `yarn install` in `/web/src/angular` to install frontend packages
- add a `user` collection in your database and add an initial user. Copy-Paste Example:

```json
{
  "_id": {
    "$oid": "6391c928a8d80e8729177c7f"
  },
  "username": "user",
  "password": "password",
  "firstName": "Max",
  "lastName": "Mustermann"
}
````

### Run

- run `./gradlew runShadow` to start backend
- run `yarn start` in `/web/src/angular` to start frontend

### Notes

- Frontend runs on port 4200, backend on port 8000. Frontend requests are getting proxied by angular to port 8000, configure in `/web/src/angular/proxy.conf.json`
- Frontend hot reloads, backends needs to get restartet.
- For faster starting time of the backend, comment out `commonMainRuntimeOnly(project(":replace-web"))` in `/build.gradle.kts`.
  - Should look like this:

  ```kotlin

  dependencies {
    jvmMainImplementation(project(":replace-application"))
    jvmMainImplementation(project(":replace-infrastructure"))
    //commonMainRuntimeOnly(project(":replace-web"))
  }

  ```

  - frontend won't be compiled and server anymore by Ktor, which is irrelevant in Dev, since we are using the angular dev server
