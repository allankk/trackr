FROM maven:3.6.3-openjdk-17 as build

ADD . /trackr
WORKDIR /trackr

ARG SPRING_DATASOURCE_URL
ARG SPRING_DATASOURCE_USERNAME
ARG SPRING_DATASOURCE_PASSWORD

RUN mvn clean install

FROM openjdk:17.0.2-jdk

VOLUME /tmp

COPY --from=0 "/trackr/backend/target/backend-0.0.1-SNAPSHOT.jar" app.jar

ENV JAVA_OPTS=""

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]