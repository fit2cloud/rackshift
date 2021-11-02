// Copyright 2016, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Dell Racadm Set BIOS Graph',
    injectableName: 'Graph.Dell.Racadm.SetBIOS',
    options: {
        defaults: {
            serverUsername: null,
            serverPassword: null,
            serverFilePath: null
        }
    },
    tasks: [
        {
            label: 'dell-racadm-set-bios',
            taskName: 'Task.Dell.Racadm.SetBIOS'
        }
    ]
};
