// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Reboot Node via proc',
    injectableName: 'Task.ProcShellReboot',
    implementsTask: 'Task.Base.ShellReboot',
    options: {
        rebootCode: 1
    },
    properties: {
        power: {}
    }
};
