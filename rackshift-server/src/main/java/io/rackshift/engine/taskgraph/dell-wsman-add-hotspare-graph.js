// Copyright © 2017 Dell Inc. or its subsidiaries. All Rights Reserved.

'use strict';

module.exports = {
    friendlyName: 'Dell Wsman Add Hotspare',
    injectableName: 'Graph.Dell.Wsman.Add.Hotspare',
    options: {
        defaults: {
            username: null,
            password: null,
            volumeId: null,
            hotspareType: 'ghs',
            ipAddress: null
        }
    },
    tasks: [
        {
            label: 'dell-wsman-add-hotspare-getXml',
            taskName: 'Task.Dell.Wsman.Add.Hotspare.GetXml'
        },
        {
            label: 'dell-wsman-add-hotspare-updateXml',
            taskName: 'Task.Dell.Wsman.Add.Hotspare.UpdateXml',
            waitOn: {
                'dell-wsman-add-hotspare-getXml': 'succeeded'
            }
        },
        {
            label: 'dell-wsman-RAID',
            taskName: 'Task.Dell.Wsman.RAID',
            waitOn: {
                'dell-wsman-add-hotspare-updateXml': 'succeeded'
            }
        },
        {
            label: 'dell-wsman-get-inventory',
            taskName: 'Task.Dell.Wsman.GetInventory',
            waitOn: {
                'dell-wsman-RAID': 'succeeded'
            },
            ignoreFailure: true
        }
    ]
};
