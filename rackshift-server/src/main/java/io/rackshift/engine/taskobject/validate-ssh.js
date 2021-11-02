// Copyright 2016, EMC, Inc.

'use strict';


module.exports = {
    friendlyName: 'Validate Ssh',
    injectableName: 'Task.Ssh.Validation',
    implementsTask: 'Task.Base.Ssh.Validation',
    options: {
        timeout: null,
        retries: null,
        backoffDelay: null,
        users: null
    },
    properties: {}
};

