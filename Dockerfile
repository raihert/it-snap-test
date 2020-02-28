# Dockerfile

FROM phusion/baseimage:0.11

MAINTAINER Dmitry Raihert <raihert@gmail.com>

ENV MAVEN_VERSION 3.3.9
ENV MAVEN_HOME /usr/share/maven

RUN add-apt-repository ppa:openjdk-r/ppa
RUN apt -y update
RUN apt -y install openjdk-8-jdk software-properties-common
RUN apt-get clean && rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*

RUN java -version

RUN mkdir -p /usr/share/maven && \
    curl -fsSL http://apache.osuosl.org/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz | \
    tar -xzC /usr/share/maven --strip-components=1 && \
    ln -s /usr/share/maven/bin/mvn /usr/bin/mvn

VOLUME /root/.m2

RUN mkdir -p /var/dev
COPY . /var/dev/test

WORKDIR /var/dev/test

RUN mvn clean package
