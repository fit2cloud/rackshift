// Copyright © 2017 Dell Inc. or its subsidiaries. All Rights Reserved.

'use strict';

module.exports = {
    friendlyName: 'Dell Wsman Add Volume',
    injectableName: 'Graph.Dell.Wsman.Add.Volume',
    options: {
        defaults: {
            enable: null,
            raidLevel: 0,
            stripeSize: 128,
            writePolicy: "WriteBack",
            shutdownType: 0,
            verifySSL: false,
            _taskTimeout: 900000,
            domain: 'wsman',
            removeXmlFile: true,
            ipAddress: "",
            username: "",
            password: "",
        }
    },
    tasks: [
        {
            label: 'dell-wsman-add-volume-getXml',
            taskName: 'Task.Dell.Wsman.Add.Volume.getXml'
        },
        {
            label: 'dell-wsman-add-volume-updateXml',
            taskName: 'Task.Dell.Wsman.Add.Volume.UpdateXml',
            waitOn: {
                'dell-wsman-add-volume-getXml': 'finished'
            }
        },
        {
            label: 'dell-wsman-RAID',
            taskName: 'Task.Dell.Wsman.RAID',
            waitOn: {
                'dell-wsman-add-volume-updateXml': 'finished'
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
