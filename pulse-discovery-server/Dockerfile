FROM openjdk:14.0.1
EXPOSE 8671
ADD ./target/pulse-discovery-server-0.0.1.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]