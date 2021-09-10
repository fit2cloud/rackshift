// Copyright 2016, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Dell wsman Update Firmware Graph',
    injectableName: 'Graph.Dell.Wsman.Update.Firmware',
    options: {
        'dell-wsman-update-firmware': {
            shareFolderAddress: null
        }
    },
    tasks: [
        {
            label: 'dell-wsman-update-firmware',
            taskName: 'Task.Dell.Wsman.Update.Firmware'
        }
    ]
};




