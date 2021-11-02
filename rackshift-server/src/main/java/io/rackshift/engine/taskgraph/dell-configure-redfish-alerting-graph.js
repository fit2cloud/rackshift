// Copyright 2017, EMC, Inc.

"use strict";

module.exports = {
    "friendlyName": "Configure Dell Redfish Alerting",
    "injectableName": "Graph.Dell.Configure.Redfish.Alerting",
    "options": {
        "redfish-subscription": {
            redfishEndpointIp: null
        }
    },
    "tasks": [
        {
            "label": "enable-alerting",
            "taskDefinition": {
                "friendlyName": "Enable Dell Alerting",
                "injectableName": "Task.Dell.Enable.Alerts",
                implementsTask: 'Task.Base.Dell.Racadm.Control',
                optionsSchema: 'dell-racadm-control.json',
                options: {
                    action: 'enableAlert'
                },
                properties: {}
            },
            ignoreFailure: true
        },
        {
            "label": "enable-redfish-alerts",
            "taskDefinition": {
                "friendlyName": "Enable Redfish Alerting",
                "injectableName": "Task.Dell.Enable.Redfish.Alert",
                implementsTask: 'Task.Base.Dell.Racadm.Control',
                optionsSchema: 'dell-racadm-control.json',
                options: {
                    action: 'enableRedfish'
                },
                properties: {}
            },
            ignoreFailure: true,
            waitOn: {
                'enable-alerting': 'finished'
            }
        },
        {
            "label": "disable-redfish-alerts",
            "taskDefinition": {
                "friendlyName": "Disable Redfish Alerting",
                "injectableName": "Task.Dell.Disable.Redfish.Alert",
                implementsTask: 'Task.Base.Dell.Racadm.Control',
                optionsSchema: 'dell-racadm-control.json',
                options: {
                    action: 'disableRedfish'
                },
                properties: {},
            },
            ignoreFailure: true,
            waitOn: {
                'enable-redfish-alerts': 'finished'
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
                'disable-redfish-alerts': 'finished'
            }
        }
    ]
};
