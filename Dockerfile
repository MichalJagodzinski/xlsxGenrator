FROM openjdk:17-jdk-alpine
MAINTAINER experto.com
VOLUME /tmp
EXPOSE 8080
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/xlsxGenerator.war"]