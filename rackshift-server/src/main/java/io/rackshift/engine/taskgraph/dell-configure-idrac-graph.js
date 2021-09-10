// Copyright 2017, EMC, Inc.

"use strict";

module.exports = {
    "friendlyName": "Configure Dell Idrac",
    "injectableName": "Graph.Dell.Configure.Idrac",
    "options": {
        "set-iDRAC-ip": {
            action: 'setIdracIP',
            ip :  null,
            netMask: null,
            gateway: null
        }
    },
    "tasks": [
        {
            "label": "set-iDRAC-ip",
            taskName: 'Task.Dell.Racadm.Control'
        }
    ]
};

