// Copyright 2017, Dell EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Dell Wsman Reset Components Graph',
    injectableName: 'Graph.Dell.Wsman.Reset.Components',
    options: {
        defaults: {
            components: null
        }
    },
    tasks: [
        {
            label: 'dell-wsman-reset-components',
            taskName: 'Task.Dell.Wsman.Reset.Components'
        }
    ]
};

