// Copyright 2016, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'EMC Redfish Catalog',
    injectableName: 'Graph.Emc.Redfish.Catalog',
    options: {},
    tasks: [
        {
            label: 'emc-redfish-catalog',
            taskName: 'Task.Emc.Redfish.Catalog'
        }
    ]
};
