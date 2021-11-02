'use strict';

module.exports = {
    friendlyName: 'Delete Volume',
    injectableName: 'Graph.Delete.Volume',
    options: {
        defaults: {
            username: null,
            password: null,
            volumeId: null,
            ipAddress: null
        }
    },
    tasks: [
        {
            label: 'delete-volume',
            taskName: 'Task.Delete.Volume'
        },
        {
            label: 'dell-wsman-get-inventory',
            taskName: 'Task.Dell.Wsman.GetInventory',
            waitOn: { 
                'delete-volume': 'finished'
            },
            ignoreFailure: true
        }
    ]
};
