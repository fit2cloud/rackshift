// Copyright 2017, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Dell WSMAN Get System Configuration Components Catalog Graph',
    injectableName: 'Graph.Dell.Wsman.GetSystemComponentsCatalog',
    options: {
        defaults: {
            shutdownType: 0,
            componentNames: [],
        }
    },
    tasks: [
        {
            label: 'dell-wsman-get-systemcomponents',
            taskName: 'Task.Dell.Wsman.GetSystemConfigComponents'
        }
    ]
};
