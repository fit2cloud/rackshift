// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Create Default Pollers',
    injectableName: 'Task.Base.Pollers.CreateDefault',
    runJob: 'Job.Pollers.CreateDefault',
    requiredOptions: [
        'pollers'
    ],
    requiredProperties: {},
    properties: {}
};
