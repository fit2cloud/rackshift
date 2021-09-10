// Copyright 2017, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Dell WSMAN Export SCP',
    injectableName: 'Graph.Dell.Wsman.Export.SCP',
    options: {
        defaults: {
            serverIP: null,
            serverUsername: null,
            serverPassword: null,
            shareType: null,
            shareAddress: null,
            shareName: null,
            fileName: null
        }
    },
    tasks: [
        {
            label: 'dell-wsman-export-scp',
            taskName: 'Task.Dell.Wsman.Export.SCP'
        }
    ]
};
