// Copyright 2017, Dell EMC, Inc.

"use strict";

module.exports = {
    "friendlyName": "Configure Dell Idrac using WSMAN",
    "injectableName": "Graph.Dell.Wsman.Configure.Idrac",
    "options": {
        "defaults": {
            address:  null,
            netmask: null,
            gateway: null
        },
        "dell-wsman-update-systemcomponents": {
            serverComponents: [
               {
                   "fqdd": '{{ context.outputs["dell-wsman-configure-idrac"].fqdd }}',
                   "attributes": [
                       {
                           "name": "IPv4Static.1#Netmask",
                           "value": '{{ context.outputs["dell-wsman-configure-idrac"].netmask }}'
                       },
                       {
                           "name": "IPv4Static.1#Gateway",
                           "value": '{{ context.outputs["dell-wsman-configure-idrac"].gateway }}'
                       },
                       {
                           "name": "IPv4Static.1#Address",
                           "value": '{{ context.outputs["dell-wsman-configure-idrac"].address }}'
                       }
                   ]
               }
            ],
            shutdownType: 0,
            forceUpdate: true
        }
    },
    "tasks": [
        {
            label: 'dell-wsman-get-inventory',
            taskName: 'Task.Dell.Wsman.GetInventory'
        },
        {
            label: 'dell-wsman-configure-idrac',
            taskName: 'Task.Dell.Wsman.configure.Idrac',
            waitOn: {
                'dell-wsman-get-inventory': 'succeeded'
            }
        },
        {
            label: 'dell-wsman-update-systemcomponents',
            taskName: 'Task.Dell.Wsman.UpdateSystemConfigComponents',
            waitOn: {
                'dell-wsman-configure-idrac': 'succeeded'
            }
        }
    ]
};
