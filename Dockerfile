FROM openjdk:17-alpine

ADD build/libs/user-service-0.0.1-SNAPSHOT.jar /app/user-service-0.0.1.jar

ENTRYPOINT ["java", "-jar", "user-service-0.0.1.jar"]