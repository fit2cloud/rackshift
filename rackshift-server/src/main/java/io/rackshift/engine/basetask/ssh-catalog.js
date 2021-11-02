// Copyright 2018, Dell EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Base Ssh Catalog Task',
    injectableName: 'Task.Base.Ssh.Catalog',
    runJob: 'Job.Ssh.Catalog',
    optionsSchema: 'linux-command.json',
    requiredOptions: [
        'commands'
    ],
    properties: {},
    requiredProperties: []
};
