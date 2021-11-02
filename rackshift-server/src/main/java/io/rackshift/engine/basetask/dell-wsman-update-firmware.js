// Copyright 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.

'use strict';

module.exports = {
    friendlyName: 'Dell Wsman Control',
    injectableName: 'Task.Base.Dell.Wsman.Update.Firmware',
    runJob: 'Job.Dell.Wsman.Firmware.Update',
    requiredOptions: [
        "shareFolderAddress",
        "shareFolderType",
        "shareFolderName"
    ],
    requiredProperties: {},
    properties:{}
};
