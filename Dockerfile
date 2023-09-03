FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/booking-users-0.0.1-SNAPSHOT.jar booking-users-0.0.1-SNAPSHOT.jar
EXPOSE 8081
CMD ["java", "-jar", "booking-users-0.0.1-SNAPSHOT.jar"]