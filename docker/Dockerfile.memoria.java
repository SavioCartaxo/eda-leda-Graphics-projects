FROM maven:3.9-eclipse-temurin-21
WORKDIR /app
COPY src/ src/
COPY pom.xml .
RUN mvn package -DskipTests -Dmaven.test.skip=true
ENTRYPOINT ["java", "-Xss512m", "-cp", "target/classes", "main.java.MainMemoria"]