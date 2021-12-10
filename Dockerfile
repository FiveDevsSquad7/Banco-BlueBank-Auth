FROM openjdk:18-slim
WORKDIR /app
ARG JAR_FILE
COPY target/${JAR_FILE} /app/bluebank_auth.jar
EXPOSE 8080
EXPOSE 8443
CMD ["java", "-jar", "bluebank_auth.jar"]