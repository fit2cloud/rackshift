FROM openjdk:8-jdk-alpine

RUN mkdir -p /opt/rackshift

ADD ./target/rackshift-proxy-1.0.0.jar /opt

VOLUME /opt/rackshift

CMD ["java", "-jar", "/opt/rackshift-proxy-1.0.0.jar"]