FROM gradle:jdk8 as builder

COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build

#FROM openjdk:8-jre-alpine
#EXPOSE 8080
#COPY --from=builder /home/gradle/src/employees.check.in/build/libs/employees-check-in-1.0-SNAPSHOT.jar /app/
#COPY --from=builder /home/gradle/src/employees.check.in/tokens/ /tokens
#
#WORKDIR /app
#CMD ["java", "-jar", "/app/employees-check-in-1.0-SNAPSHOT.jar"]
