// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Catalog LLDP',
    injectableName: 'Task.Catalog.LLDP',
    implementsTask: 'Task.Base.Linux.Catalog',
    options: {
        commands: [
            'sudo /usr/sbin/lldpcli show neighbor -f keyvalue'
        ]
    },
    properties: {
        catalog: {
            type: 'lldp'
        }
    }
};
