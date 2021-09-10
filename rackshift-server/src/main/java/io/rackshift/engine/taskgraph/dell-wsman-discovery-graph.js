// Copyright 2016, DELL, Inc.

'use strict';

module.exports = {
    friendlyName: 'Dell Wsman Discovery',
    injectableName: 'Graph.Dell.Wsman.Discovery',
    options: {
    	defaults: {
    		ranges:[],
			inventory: false,
			deviceTypesToDiscover: []
    	}
    },
    tasks: [
        {
            label: 'dell-wsman-discovery',
            taskName: 'Task.Dell.Wsman.Discovery'
        }
    ]
};
