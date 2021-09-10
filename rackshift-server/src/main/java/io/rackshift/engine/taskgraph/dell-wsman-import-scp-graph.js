// Copyright 2017, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Dell WSMAN Import SCP',
    injectableName: 'Graph.Dell.Wsman.Import.SCP',
    options: {
        defaults: {
            serverIP: null,
            serverUsername: null,
            serverPassword: null,
            shareType: null,
            shareAddress: null,
            shareName: null,
            fileName: null,
            shutdownType: null,
            componentNames: null
        }
    },
    tasks: [
        {
            label: 'dell-wsman-import-scp',
            taskName: 'Task.Dell.Wsman.Import.SCP'
        }
    ]
};
