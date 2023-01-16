# Replace App

## Technologien

- Backend: [Ktor](https://ktor.io/)
- Datatabase: [MongoDB](https://www.mongodb.com)
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
- Database settings are Applied through environment variables. Depending on which setup you are using, you have to set following variables specified in `/infrastructure/src/jvmMain/resources/application.conf`
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
- Frontend hot reloads, backends needs to get restarted.
- You can execute `./gradlew ktlintCheck` and `./gradlew ktlintFormat` to check for backend formatting (alternatively ctrl + ctrl in intellij ot execute gradle commands)
