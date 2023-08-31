FROM openjdk:11-jdk
ARG JAR_PATH=./build/libs
COPY ${JAR_PATH}/backend-0.0.1-SNAPSHOT.jar application.jar
ENTRYPOINT ["java", "-jar"]
CMD ["/application.jar"]