// Copyright 2017, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Run rest command',
    injectableName: 'Graph.Run.Rest.Command',
    options: {
        defaults: {
            url: null,
            credential: null,
            method: null,
            headers: null,
            data: null,
            verifySSL: null,
            recvTimeoutMs: null
       }
    },
    tasks: [
        {
            label: 'run-rest-command',
            taskName: 'Task.run.rest.command'
        }
    ]
};

