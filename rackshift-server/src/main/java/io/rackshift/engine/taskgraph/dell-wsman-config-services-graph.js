// Copyright 2016, Dell EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Dell Wsman Config Services',
    injectableName: 'Graph.Dell.Wsman.ConfigServices',
    options: {
        defaults: {
        	configServer: 'http://127.0.0.1:9600'
        }
    },
    tasks: [
        {
            label: 'dell-wsman-config-services',
            taskName: 'Task.Dell.Wsman.Config.Services'
        }
    ]
};
