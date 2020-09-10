#!/bin/bash
java -jar -Xdebug -Xrunjdwp:transport=dt_socket,address=5005,server=y,suspend=y /opt/rackshift-server-1.0.0.jar