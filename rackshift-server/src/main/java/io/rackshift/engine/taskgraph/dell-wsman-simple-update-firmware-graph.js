// Copyright 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.

'use strict';

module.exports = {
    friendlyName: 'Dell Wsman Simple Update Firmware Graph',
    injectableName: 'Graph.Dell.Wsman.Simple.Update.Firmware',
    options: {
        defaults: {
            imageURI: null
        }
    },
    tasks: [
        {
            label: 'dell-wsman-simple-update-firmware',
            taskName: 'Task.Dell.Wsman.Simple.Update.Firmware'
        }
    ]
};
