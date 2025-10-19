FROM gradle:8.14.3-jdk21 AS builder

WORKDIR /app

COPY . .

RUN gradle bootJar --no-daemon


FROM amazoncorretto:21-alpine3.22-jdk

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

# Run the app
ENTRYPOINT ["java","-jar","app.jar"]
