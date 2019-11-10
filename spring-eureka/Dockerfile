#
# Build stage
#
FROM maven:3.6.0-jdk-8-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
COPY start /home/app
RUN mvn -f /home/app/pom.xml clean package

#
# Package stage
#
FROM openjdk:8-jre-slim
COPY --from=build /home/app/target/spring-eureka-0.1-SNAPSHOT.jar /usr/local/lib/spring-eureka-0.1-SNAPSHOT.jar
COPY --from=build /home/app/start /usr/local/bin/start
RUN ["chmod", "+x", "/usr/local/bin/start"]

EXPOSE 1111
ENTRYPOINT ["java","-jar","/usr/local/lib/spring-eureka-0.1-SNAPSHOT.jar","spring-eureka","1111" ]
