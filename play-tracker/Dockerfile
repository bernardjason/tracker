#
# Build stage
#
FROM picoded/ubuntu-openjdk-8-jdk

ENV SCALA_VERSION 2.13.0
ENV SBT_VERSION 1.2.8

RUN mkdir -p /home/app/play-tracker
WORKDIR /home/app/play-tracker
COPY  . /home/app/play-tracker

RUN \
  curl -L -o sbt-$SBT_VERSION.deb http://dl.bintray.com/sbt/debian/sbt-$SBT_VERSION.deb && \
  dpkg -i sbt-$SBT_VERSION.deb && \
  rm sbt-$SBT_VERSION.deb && \
  apt-get update && \
  apt-get install sbt

RUN  sbt dist

RUN unzip target/universal/play-tracker-0.1-SNAPSHOT.zip

EXPOSE 9000
ENTRYPOINT ["play-tracker-0.1-SNAPSHOT/bin/play-tracker" , "-Deureka=eureka" ,"-Dlogger.file=/home/app/play-tracker/play-tracker-0.1-SNAPSHOT/conf/prod-logger.xml" ,  "-Dplay.http.secret.key=ad31779d4ee49d5ad5162bf1429c32e2e9933f3b"]
