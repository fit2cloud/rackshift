// Copyright 2017, Dell EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Persist Poller Data Base Task',
    injectableName: 'Task.Base.Persist.Poller.Data',
    runJob: 'Job.Persist.Poller.Data',
    requiredOptions: [
       	    "pollerId",
       	    "nodeId",
       	    "catalogName"
    ],
    requiredProperties: {},
    properties: {}
};
