// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Trigger Task',
    injectableName: 'Task.Base.Trigger',
    runJob: 'Job.Trigger',
    requiredOptions: [
        'triggerMode',
        'triggerType',
        'triggerGroup'
    ],
    requiredProperties: {},
    properties: {}
};
