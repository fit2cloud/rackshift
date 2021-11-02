// Copyright 2017, Dell EMC, Inc.

"use strict";

module.exports = {
    "friendlyName": "Configure Dell Redfish Alert using WSMAN",
    "injectableName": "Graph.Dell.Wsman.Configure.Redfish.Alert",
    "options": {
        "dell-wsman-configure-redfish-alert": {
            shutdownType: 0,
            forceUpdate: true
        },
        "redfish-subscription": {
            redfishEndpointIp: null
        }
    },
    "tasks": [
        {
            label: 'dell-wsman-get-systemcomponents',
            taskName: 'Task.Dell.Wsman.GetSystemConfigComponents'
        },
        {
            label: 'dell-wsman-configure-redfish-alert',
            taskName: 'Task.Dell.Wsman.Configure.Redfish.Alert',
            waitOn: {
                'dell-wsman-get-systemcomponents': 'succeeded'
            }
        },
        {
            "label": "redfish-subscription",
            "taskDefinition": {
                "friendlyName": "Subscribe  To Redfish Alerting",
                "injectableName": "Task.Dell.Subscribe.Redfish.Alert",
                implementsTask: 'Task.Base.Redfish.Alert.Enable',
                properties: {},
                options: {
                    url:null,
                    credential: {
                        username: null,
                        password: null
                    },
                    data: {}
                }
            },
            ignoreFailure: true,
            waitOn: {
                'dell-wsman-configure-redfish-alert': 'succeeded'
            }
        }
    ]
};
