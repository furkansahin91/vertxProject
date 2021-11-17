# Vertx library usage challenge

- Java version: 11
- The incoming data flow from the rest calls is handled via Vert.x for non blocking operations
- Lombok for spaghetti code generation(make sure annotation processor is enabled in idea)

# Running the application

The application can be run on command line (in the directory where pom.xml exists) with the following command

```shell script
mvn spring-boot:run
```

There is also a Dockerfile to start the application in a container. To build the image "docker build . " inside project folder and to run the built image "docker run -p 8080:8080" commands can be used.

