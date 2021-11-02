// Copyright 2016, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Dell Racadm Disable VTx Graph',
    injectableName: 'Graph.Dell.Disable.VTx',
    options: {
        'dell-racadm-disable-VTx': {
            forceReboot: true,
            serverUsername: null,
            serverPassword: null,
            serverFilePath: null,
            action: 'disableVTx'
       },
        'when-require-refresh-catalog':{
            refresh: "true",
            when: '{{options.refresh}}'
        }
    },
    tasks: [
        {
            label: 'dell-racadm-disable-VTx',
            taskName: 'Task.Dell.Racadm.Control'
        },
        {
            label: 'when-require-refresh-catalog',
            taskName: 'Task.Evaluate.Condition',
            waitOn: {
                'dell-racadm-disable-VTx': 'succeeded'
            }
        }, 
        {
            label: 'refresh-racadm-catalog',
            taskName: 'Task.Dell.Racadm.GetConfigCatalog',
            waitOn: {
                'when-require-refresh-catalog': 'succeeded'
            }
        },
        {
            label: 'noop',
            taskName: 'Task.noop',
            waitOn: {
                'when-require-refresh-catalog': 'failed'
            }
        }
    ]
};
