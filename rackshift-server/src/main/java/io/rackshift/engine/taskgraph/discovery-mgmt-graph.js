// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Mgmt Discovery',
    injectableName: 'Graph.Mgmt.Discovery',
    tasks: [
        {
            label: 'catalog-mgmt-bmc',
            taskName: 'Task.Catalog.Mgmt.bmc',
            ignoreFailure: true
        },
        {
            label: 'catalog-mgmt-lldp',
            taskName: 'Task.Catalog.Local.LLDP',
            waitOn: {
                'catalog-mgmt-bmc': 'finished'
            },
            ignoreFailure: true
        },
        {
            label: 'catalog-mgmt-dmi',
            taskName: 'Task.Catalog.Local.DMI',
            waitOn: {
                'catalog-mgmt-lldp': 'finished'
            },
            ignoreFailure: true
        },
        {
            label: 'node-discovered-alert',
            taskName: 'Task.Alert.Node.Discovered',
            waitOn: {
                'catalog-mgmt-dmi': 'finished'
            }
        }

    ]
};
