'use strict';

module.exports = {
    friendlyName: 'Add Hot Spare',
    injectableName: 'Graph.Add.Hotspare',
    options: {
        defaults: {
            username: null,
            password: null,
            volumeId: null,
            driveId: null, 
            hotspareType: 'ghs',
            ipAddress: null
        }
    },
    tasks: [
        {
            label: 'add-hotspare',
            taskName: 'Task.Add.Hotspare'
        },
        {
            label: 'dell-wsman-get-inventory',
            taskName: 'Task.Dell.Wsman.GetInventory',
            waitOn: { 
                'add-hotspare': 'finished'
            },
            ignoreFailure: true
        }

    ]
};
