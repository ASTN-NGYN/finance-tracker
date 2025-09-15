# backend.Dockerfile
FROM maven:3.9.6-eclipse-temurin-21

WORKDIR /app

# Copy only pom.xml first to leverage Docker cache
COPY pom.xml .

# Download dependencies offline
RUN mvn dependency:go-offline

# Mount src via docker-compose for hot reload (no need to copy src)
CMD ["mvn", "spring-boot:run"]
