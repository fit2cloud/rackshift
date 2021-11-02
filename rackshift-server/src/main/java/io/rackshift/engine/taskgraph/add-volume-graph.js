'use strict';

module.exports = {
    friendlyName: 'Add Volume',
    injectableName: 'Graph.Add.Volume',
    options: {
        defaults: {
            username: null,
            password: null,
            drives: null,
            raidLevel: null,
            name: null,
            sizeInBytes: null,
            ipAddress: null
        }
    },
    tasks: [
        {
            label: 'add-volume',
            taskName: 'Task.Add.Volume'
        },
        {
            label: 'dell-wsman-get-inventory',
            taskName: 'Task.Dell.Wsman.GetInventory',
            waitOn: { 
                'add-volume': 'finished'
            },
            ignoreFailure: true
        }

    ]
};
