// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Node shell reboot',
    injectableName: 'Task.Base.ShellReboot',
    runJob: 'Job.Linux.ShellReboot',
    requiredOptions: [
        'rebootCode'
    ],
    requiredProperties: {},
    properties: {}
};
