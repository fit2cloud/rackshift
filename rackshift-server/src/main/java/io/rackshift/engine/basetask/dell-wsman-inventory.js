// Copyright 2016, Dell, Inc.

'use strict';

module.exports = {
    friendlyName: 'Dell Wsman Inventory Job',
    injectableName: 'Task.Base.Dell.Wsman.GetInventory',
    runJob: 'Job.Dell.Wsman.Inventory',
    requiredOptions: [
    ],
    requiredProperties: {
    },
    properties: {
        catalog: {}
    }
};
