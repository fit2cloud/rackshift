// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Set Node Diskboot',
    injectableName: 'Task.Obm.Node.DiskBoot',
    implementsTask: 'Task.Base.Obm.Node',
    options: {
        action: 'setBootDisk',
        obmServiceName: 'ipmi-obm-service'
    },
    properties: {
        power: {}
    }
};
