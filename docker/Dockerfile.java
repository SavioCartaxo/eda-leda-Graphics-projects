FROM maven:3.9-eclipse-temurin-21
WORKDIR /app
COPY src/ src/
COPY pom.xml .
RUN mvn package -DskipTests
ENTRYPOINT ["java", "-Xss512m", "-cp", "target/classes", "Main"]