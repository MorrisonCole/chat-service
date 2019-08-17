FROM openjdk:8-jre-alpine
RUN mkdir /run/secrets
COPY ./build/libs/login-1.0-SNAPSHOT-all.jar /bin/chat/login.jar
CMD ["java", "-jar", "/bin/chat/login.jar"]