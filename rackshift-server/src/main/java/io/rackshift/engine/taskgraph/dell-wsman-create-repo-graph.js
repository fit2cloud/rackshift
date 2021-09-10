// Copyright 2016, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Dell WSMAN Create Firmware Repo Graph',
    injectableName: 'Graph.Dell.Wsman.Create.Repo',
    options: {
        defaults: {
        	catalogFilePath : null,
        	targetFilePath : null,
        	updates: null
        }
    },
    tasks: [
        {
            label: 'dell-wsman-create-repo',
            taskName: 'Task.Dell.Wsman.Create.Repo'
        }
    ]
};

