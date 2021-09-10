// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Catalog bmc',
    injectableName: 'Task.Catalog.bmc',
    implementsTask: 'Task.Base.Linux.Catalog',
    options: {
        commands: [
            {
                command: 'sudo ipmitool lan print',
                acceptedResponseCodes: [1]
            },
            'sudo ipmitool sel',
            'sudo ipmitool sel list -c',
            'sudo ipmitool mc info',
            'sudo ipmitool fru'
        ]
    },
    properties: {
        catalog: {
            type: 'bmc'
        }
    }
};
