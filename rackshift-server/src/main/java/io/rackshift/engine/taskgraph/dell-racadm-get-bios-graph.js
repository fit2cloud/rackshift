// Copyright 2016, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Dell Racadm Get BIOS Graph',
    injectableName: 'Graph.Dell.Racadm.GetBIOS',
    options: {
        defaults: {
            serverUsername: null,
            serverPassword: null,
            serverFilePath: null
        }
    },
    tasks: [
        {
            label: 'dell-racadm-get-bios',
            taskName: 'Task.Dell.Racadm.GetBIOS'
        }
    ]
};
