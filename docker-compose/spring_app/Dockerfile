FROM openjdk:14
COPY e2ee_chat-1.0.jar /
COPY wait-for-it.sh /
ENTRYPOINT ["./wait-for-it.sh", "mongodb:27017","--timeout=60", "--", "./wait-for-it.sh", "keycloak:8081","--timeout=60", "--", "java", "-jar","e2ee_chat-1.0.jar"]