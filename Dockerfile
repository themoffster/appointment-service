FROM openjdk:17
ARG JAR_FILE=/build/libs/appointment-service-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} appointment-service.jar
CMD ["java", "-jar", "-Dspring.profiles.active=local,local-patient-service", "/appointment-service.jar"]
