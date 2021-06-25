FROM openjdk:14
COPY backend-1.0.jar /
COPY wait-for-it.sh /
RUN chmod +x /wait-for-it.sh
ENTRYPOINT ["./wait-for-it.sh", "mongodb:27017","--timeout=60", "--", "./wait-for-it.sh", "keycloak:8081","--timeout=60", "--", "java", "-jar","backend-1.0.jar"]