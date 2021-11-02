// Copyright 2017, DELL EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Persist Poller Data',
    injectableName: 'Graph.Persist.Poller.Data',
    options: {
    	defaults: {
    	    pollerId: null,
    	    nodeId: null,
    	    interval: null,
    	    duration: null,
    	    catalogName: null,
    	    path: null
    	}
    },
    tasks: [
        {
            label: 'persist-poller-data',
            taskName: 'Task.Persist.Poller.Data'
        }
    ]
};
