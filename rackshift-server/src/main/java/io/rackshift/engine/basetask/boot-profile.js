// Copyright 2016, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Base Boot Profile',
    injectableName: 'Task.Base.BootProfile',
    runJob: 'Job.BootProfile',
    optionsSchema: {
        properties: {
            profile: {
                description: 'The pxe profile script filename',
                type: 'string',
                minLength: 1
            }
        },
        required: ['profile']
    },
    requiredProperties: {},
    properties: {}
};
