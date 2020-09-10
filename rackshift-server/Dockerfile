FROM openjdk:8-jdk-alpine

RUN mkdir -p /opt/rackshift

ADD ./target/rackshift-server-1.0.0.jar /opt

ADD entrypoint.sh /

VOLUME /opt/rackshift

EXPOSE "5005"

ENTRYPOINT ['/entrypoint.sh']