#!/bin/sh

mvn clean package

docker build -f Dockerfile -t test-jdk-8 .

docker run -i test-jdk-8
