// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    "friendlyName": "Snmp Ping",
    "injectableName": "Task.Base.Snmp.Ping",
    "runJob": "Job.Snmp.Ping",
    "requiredOptions": [
        "host",
        "community"
    ],
    "requiredProperties": {},
    "properties": {}
};
