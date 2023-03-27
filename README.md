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

### Notes

- When running ktor in Dev Mode, it will attempt to seed a dev user. See Console output for more information.
- Frontend runs on port 4200, backend on port 8000. Frontend requests are getting proxied by angular to port 8000, configure in `/web/src/angular/proxy.conf.json`
- Frontend hot reloads, backends needs to get restarted.
- You can execute `./gradlew ktlintCheck` and `./gradlew ktlintFormat` to check for backend formatting (alternatively ctrl + ctrl in intellij ot execute gradle commands)
- You can execute `./gradlew dependencyCheckAnalyze` to check for vulnerabilities with a CVSS score higher than 5 (at least medium criticality). This threshold can be configured in build.gradle of the root project.

## DevOps

The application is built using Jenkins and Docker. Traefik is used as reverse proxy to serve the application.
For the build of the application several tools are used. The Application is built through using Jenkins and Docker. The Frontend is built using a multi-layered Dockerfile in `/web/Dockerfile`
The Backend is built through a ktor Plugin, which produces a Dockerfile.
The Jenkinsfile is configured in a way that it is able to distribute builds to two different environments. A Staging and Test environment.
The **dev-Branch** is deployed to **Test**, while **master-Branch** is deployed to **Staging**.
Some steps inside the build process are only done when those branches are built.
During build, some configuration has to be done inside Jenkins to make builds and Deployment work. They are divided into several parts:

*Environment*
- `STAGING_URL` or `TEST_URL`: The respective URL (including protocol) to your project
- `REPLACE_ECR_FRONTEND`: Name of the frontend Build
- `REPLACE_ECR_BACKEND`: Name of the backend Build
- `IMAGE_TAG`: The Tag that gets used on your image. This is set directly in the build based on the branch you are on
- `DOCKER_ENV`: The docker environment for Production is set to either `ssh://staging.local` or `ssh://test.local` since deployment is done through a ssh tunnel

*Database*
- `STAGING_DATABASE_URL` or `TEST_DATABASE_URL`: Connection url for the respective database in the format `jdbc:postgresql://<name>:<port>/<database>`
- `STAGING_CREDENTIALS` or `TEST_CREDENTIALS`: Database credentials stored in Jenkins' credential store

*Oauth*
- `STAGING_OAUTH_AUTHORIZE` or `TEST_OAUTH_AUTHORIZE`: The Authorize-Url used for the respective Oauth configuration
- `STAGING_OAUTH_ACCESSTOKEN` or `TEST_OAUTH_ACCESSTOKEN`: The Accesstoken-Url used for the respective Oauth configuration
- `STAGING_OAUTH_CLIENTID` or `TEST_OAUTH_CLIENTID`: The Clientid for the respective Oauth configuration
- `STAGING_OAUTH_SECRET` or `TEST_OAUTH_SECRET`: The Client Secret for the respective Oauth configuration
