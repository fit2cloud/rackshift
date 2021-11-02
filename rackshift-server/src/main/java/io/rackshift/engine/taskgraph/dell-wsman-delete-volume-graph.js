// Copyright 2017 Dell Inc. or its subsidiaries. All Rights Reserved.

'use strict';

module.exports = {
    friendlyName: 'Dell Wsman Delete Volume',
    injectableName: 'Graph.Dell.Wsman.Delete.Volume',
    options: {
        defaults: {
            enable: null,
            shutdownType: 0,
            verifySSL: false,
            _taskTimeout: 900000,
            domain: 'wsman',
            removeXmlFile: true,
            ipAddress: "",
            username: "",
            password: ""
        }
    },
    tasks: [
        {
            label: 'dell-wsman-delete-volume-getXml',
            taskName: 'Task.Dell.Wsman.Delete.Volume.GetXml'
        },
        {
            label: 'dell-wsman-delete-volume-updateXml',
            taskName: 'Task.Dell.Wsman.Delete.Volume.UpdateXml',
            waitOn: {
                'dell-wsman-delete-volume-getXml': 'finished'
            }
        },
        {
            label: 'dell-wsman-RAID',
            taskName: 'Task.Dell.Wsman.RAID',
            waitOn: {
                'dell-wsman-delete-volume-updateXml': 'finished'
            }
        },
        {
            label: 'dell-wsman-get-inventory',
            taskName: 'Task.Dell.Wsman.GetInventory',
            waitOn: {
                'dell-wsman-RAID': 'finished'
            },
            ignoreFailure: true
        }
    ]
};
