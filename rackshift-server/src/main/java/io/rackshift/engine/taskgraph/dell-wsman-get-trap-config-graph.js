// Copyright 2016, DELL, Inc.

'use strict';

module.exports = {
    friendlyName: 'Dell Wsman Get Trap Config',
    injectableName: 'Graph.Dell.Wsman.GetTrapConfig',
    options: {
        defaults: {
        	target: null
        }
    },
    tasks: [
        {
            label: 'dell-wsman-get-trap-config',
            taskName: 'Task.Dell.Wsman.GetTrapConfig'
        }
    ]
};
