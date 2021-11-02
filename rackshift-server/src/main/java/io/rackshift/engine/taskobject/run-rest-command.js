//Copyright 2017, Dell EMC, Inc.

'use strict';

module.exports = {

    friendlyName: 'Run Rest Command',
    injectableName: 'Task.Run.Rest.Command',
    implementsTask: 'Task.Base.Rest',
    options: {
        url: null,
        credential: null,
        method: null,
        headers: null,
        data: null,
        verifySSL: null,
        recvTimeoutMs: null
    },
    properties: {}

};

