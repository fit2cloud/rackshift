// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Shell commands',
    injectableName: 'Task.Linux.Commands',
    implementsTask: 'Task.Base.Linux.Commands',
    options: {
        commands: [
            {
                command: 'sudo ls /var',
                catalog: { format: 'raw', source: 'ls var' }
            },
            {
                command: 'sudo lshw -json',
                catalog: { format: 'json', source: 'lshw user' }
            },
            {
                command: 'test',
                acceptedResponseCodes: [1]
            }
        ]
    },
    properties: {
        commands: {}
    }
};
