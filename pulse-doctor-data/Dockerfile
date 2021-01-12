FROM openjdk:14.0.1
EXPOSE 8080
ADD ./target/pulse-doctor-data-0.0.1.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]