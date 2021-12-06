FROM openjdk:8-jre-alpine

COPY target/rackshift-dhcp-proxy-1.0.0-jar-with-dependencies.jar /usr/src/myapp/app.jar

WORKDIR /usr/src/myapp

VOLUME /opt/rackshift/conf

CMD java -jar -Xdebug -Xrunjdwp:transport=dt_socket,address=5003,server=y,suspend=n app.jar