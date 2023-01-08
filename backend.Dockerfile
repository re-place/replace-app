# Stage 1: Build
FROM gradle:latest AS BUILD
WORKDIR /usr/app/
COPY . . 
RUN gradle build

# Stage 2: Run
FROM openjdk:17-alpine3.14
ENV JAR_NAME=replace-app-all.jar
ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME
COPY --from=BUILD $APP_HOME/build/libs/ .
EXPOSE 8080
ENTRYPOINT java -jar $JAR_NAME