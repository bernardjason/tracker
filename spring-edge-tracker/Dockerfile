#
# Build stage
#
FROM maven:3.6.0-jdk-8-slim AS build
COPY src /home/app/src
COPY start /home/app
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

#
# Package stage
#
FROM openjdk:8-jre-slim
#COPY --from=build /home/app/target/demo-0.0.1-SNAPSHOT.jar /usr/local/lib/demo.jar
#EXPOSE 8080
#ENTRYPOINT ["java","-jar","/usr/local/lib/demo.jar"]
COPY --from=build /home/app/target/spring-edge-tracker-0.1-SNAPSHOT.jar /usr/local/lib/spring-edge-tracker-0.1-SNAPSHOT.jar
COPY --from=build /home/app/target/spring-edge-tracker-0.1-SNAPSHOT.jar /usr/local/lib/spring-edge-tracker-0.1-SNAPSHOT.jar
COPY --from=build /home/app/start /usr/local/bin/start
RUN ["chmod", "+x", "/usr/local/bin/start"]
EXPOSE 3333 3344
ENTRYPOINT ["start" ,"http://spring-eureka:1111/eureka" , "redis" ,"6379" ]
