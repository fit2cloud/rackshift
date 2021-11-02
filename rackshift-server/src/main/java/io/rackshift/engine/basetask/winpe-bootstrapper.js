// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'WinPE.Bootstrapper',
    injectableName: 'Task.Base.WinPE.Bootstrap',
    runJob: 'Job.WinPE.Bootstrap',
    requiredOptions: [
        'profile'
    ],
    requiredProperties: {},
    properties: {}
};

