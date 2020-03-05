# Dockerfile

FROM phusion/baseimage:0.11

MAINTAINER Dmitry Raihert <raihert@gmail.com>

ENV BUILD_NAME test-0.0.1-SNAPSHOT.jar

RUN add-apt-repository ppa:openjdk-r/ppa
RUN apt -y update
RUN apt -y install openjdk-8-jdk software-properties-common
RUN apt-get clean && rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*

RUN java -version

RUN mkdir -p /var/dev/test

WORKDIR /var/dev/test

COPY ./target/$BUILD_NAME /var/dev/test

ENTRYPOINT java -jar $BUILD_NAME
