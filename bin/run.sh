#!/bin/sh

docker build -f Dockerfile -t test/maven:test-jdk-8 .

docker run -it test/maven:test-jdk-8 mvn exec:java -Dexec.mainClass=com.raihert.it.Application
