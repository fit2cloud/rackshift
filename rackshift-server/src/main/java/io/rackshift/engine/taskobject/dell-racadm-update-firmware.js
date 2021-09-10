// Copyright 2016, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'dell racadm update firmware image',
    injectableName: 'Task.Dell.Racadm.Update.Firmware',
    implementsTask: 'Task.Base.Dell.Racadm.Control',
    optionsSchema: 'dell-racadm-control.json',
    options: {
        action: 'updateFirmware',
        forceReboot: true
    },
    properties: {}
};
