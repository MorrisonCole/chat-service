FROM openjdk:8-jre-alpine
RUN mkdir /run/secrets
COPY ./build/libs/presence-1.0-SNAPSHOT-all.jar /bin/chat/presence.jar
CMD ["java", "-jar", "/bin/chat/presence.jar"]
