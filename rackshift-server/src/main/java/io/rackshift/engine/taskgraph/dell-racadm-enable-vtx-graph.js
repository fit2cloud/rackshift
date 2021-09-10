// Copyright 2016, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Dell Racadm Enable VTx Graph',
    injectableName: 'Graph.Dell.Enable.VTx',
    options: {
        'dell-racadm-enable-VTx': {
            forceReboot: true,
            serverUsername: null,
            serverPassword: null,
            serverFilePath: null,
            action: 'enableVTx'
        },
        'when-require-refresh-catalog':{
            refresh: "true",
            when: '{{options.refresh}}'
        }
    },
    tasks: [
        {
            label: 'dell-racadm-enable-VTx',
            taskName: 'Task.Dell.Racadm.Control'
        },
        {
            label: 'when-require-refresh-catalog',
            taskName: 'Task.Evaluate.Condition',
            waitOn: {
                'dell-racadm-enable-VTx': 'succeeded'
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
