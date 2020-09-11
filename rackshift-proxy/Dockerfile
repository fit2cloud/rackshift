FROM openjdk:8-jdk-alpine

RUN mkdir -p /opt/rackshift

ADD ./target/rackshift-proxy-1.0.0.jar /opt

VOLUME /opt/rackshift

CMD java -jar -Xdebug -Xrunjdwp:transport=dt_socket,address=5004,server=y,suspend=n /opt/rackshift-proxy-1.0.0.jar