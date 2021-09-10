// Copyright 2016, DELL, Inc.

'use strict';

module.exports = {
    friendlyName: 'Dell Wsman Get Inventory',
    injectableName: 'Graph.Dell.Wsman.GetInventory',
    options: {
        defaults: {
        	callback: true
        }
    },
    tasks: [
        {
            label: 'dell-wsman-get-inventory',
            taskName: 'Task.Dell.Wsman.GetInventory'
        }
    ]
};
