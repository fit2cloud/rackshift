// Copyright 2016, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Ssh Exec',
    injectableName: 'Task.Ssh.Exec',
    implementsTask: 'Task.Base.Ssh.Command',
    options: {
        commands: null
    },
    properties: {}
};
