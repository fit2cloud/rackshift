// Copyright 2016, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'EMC Compose System Job',
    injectableName: 'Task.Base.Emc.Compose.System',
    runJob: 'Job.Emc.Compose.System',
    requiredOptions: [ 
        'name',
        'action'
    ],
    requiredProperties: {},
    properties: {}
};
