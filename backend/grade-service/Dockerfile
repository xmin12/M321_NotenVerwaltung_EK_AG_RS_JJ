# Stage 1: Build the application with Gradle
# WICHTIG: Wir benutzen openjdk:24, um zu deinem Projekt zu passen
FROM openjdk:24-jdk-slim as builder
WORKDIR /app

# Copy the gradle wrapper and build files
COPY gradlew .
COPY gradle ./gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .

# Download dependencies to leverage Docker layer caching
RUN ./gradlew dependencies --no-daemon

# Copy the source code
COPY src ./src

# Build the executable JAR, skipping tests
RUN ./gradlew bootJar -x test --no-daemon

# Stage 2: Create the final lightweight image
# WICHTIG: Wir benutzen auch hier die passende JRE Version 24
FROM openjdk:24-jre-slim
WORKDIR /app
# JARs are created in the build/libs directory by Gradle
COPY --from=builder /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]