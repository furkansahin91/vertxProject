FROM adoptopenjdk/openjdk11:x86_64-alpine-jdk-11.0.10_9
VOLUME /tmp
ARG JAR_FILE
ADD /target/challenge-0.0.1-SNAPSHOT.jar challenge-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","challenge-0.0.1-SNAPSHOT.jar"]
EXPOSE 8081