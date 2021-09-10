// Copyright 2016, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'base-uefi',
    injectableName: 'Task.Base.uefi',
    runJob: 'Job.Uefi',
    requiredOptions: [
        'profile',
        'repo',
        'uefitool'
    ],
    requiredProperties: {},
    properties: {}
};
