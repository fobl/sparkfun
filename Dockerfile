M anapsix/alpine-java
LABEL maintainer="frode.bjerkenes@gmail.com"
COPY /target/sparkfun-1.0-SNAPSHOT.jar /home/sparkfun.jar
CMD ["java","-jar","/home/sparkfun.jar"]

