FROM openjdk:8-jre-alpine

ENV APP_HOME /app

COPY ./build/libs/*.jar $APP_HOME/
COPY ./tokens/ /tokens

EXPOSE 8080

CMD ["java", "-jar", "/app/employees-check-in-1.0-SNAPSHOT.jar"]
