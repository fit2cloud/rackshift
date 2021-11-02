// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    "friendlyName": "Collect Snmp",
    "injectableName": "Task.Base.Snmp.Collect",
    "runJob": "Job.Snmp.Collect",
    "requiredOptions": [
        "oids"
    ],
    "requiredProperties": {},
    "properties": {}
};
