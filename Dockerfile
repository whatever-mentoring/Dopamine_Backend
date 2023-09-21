FROM openjdk:11-jdk
ARG JAR_PATH=./build/libs
COPY ${JAR_PATH}/backend-0.0.1-SNAPSHOT.jar application.jar
COPY ./src/main/resources/keystore.p12 keystore.p12
ENTRYPOINT ["java", "-jar"]
CMD ["/application.jar"]