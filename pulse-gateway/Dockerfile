FROM openjdk:14.0.1
EXPOSE 9090
ADD ./target/pulse-gateway-0.0.1.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]