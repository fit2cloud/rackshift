// Copyright 2017, Dell EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Persist Poller Data Task',
    injectableName: 'Task.Persist.Poller.Data',
    implementsTask: 'Task.Base.Persist.Poller.Data',
    options: {
       	    pollerId: null,
       	    nodeId: null,
       	    interval: null,
       	    duration: null,
            catalogName: null,
            path: null
    },
    properties: {}
};
