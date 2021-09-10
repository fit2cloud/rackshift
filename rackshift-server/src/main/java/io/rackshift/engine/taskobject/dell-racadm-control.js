// Copyright 2016, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Dell Racadm Control',
    injectableName: 'Task.Dell.Racadm.Control',
    implementsTask: 'Task.Base.Dell.Racadm.Control',
    optionsSchema: 'dell-racadm-control.json',
    options: {
        forceReboot: true
    },
    properties: {}
};
