// Copyright 2016-2018, Dell EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Base Ssh Command Task',
    injectableName: 'Task.Base.Ssh.Command',
    runJob: 'Job.Ssh.Command',
    optionsSchema: 'linux-command.json',
    requiredOptions: [
        'commands'
    ],
    properties: {},
    requiredProperties: []
};
