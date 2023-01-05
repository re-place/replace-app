FROM gradle:latest AS BUILD
WORKDIR /usr/app/
COPY . . 
RUN gradle build

FROM eclipse-temurin:17-jdk-jammy
ENV JAR_NAME=replace-app-all.jar
ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME
COPY --from=BUILD $APP_HOME .
EXPOSE 8080
ENTRYPOINT exec java -jar $APP_HOME/build/libs/$JAR_NAME 
