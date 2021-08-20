FROM jeanblanchard/java:8

COPY target/rackshift-dhcp-proxy-1.0.0-jar-with-dependencies.jar /usr/src/myapp/app.jar

WORKDIR /usr/src/myapp

VOLUME /opt/rackshift/conf

CMD ["java", "-jar", "app.jar"]