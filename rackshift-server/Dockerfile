FROM rackshift/rackshift-base:2.0

RUN mkdir -p /opt/rackshift

ADD ./target/rackshift-server-1.0.0.jar /opt

VOLUME /opt/rackshift

EXPOSE 5005

CMD java -jar /opt/rackshift-server-1.0.0.jar