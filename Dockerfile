FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY target/teacher.jar app.jar
ENTRYPOINT ["java","-jar","teacher.jar"]
 
