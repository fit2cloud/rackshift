// Copyright 2016, EMC, Inc.

'use strict';
module.exports = {
    friendlyName: 'Sftp File',
    injectableName: 'Task.Sftp',
    implementsTask: 'Task.Base.Sftp',
    options: {
        isPDU: null,
        fileSource: null,
        fileDestination: null,
        timeout: null,
        keepaliveInterval: null,
        keepaliveCountMax: null
    },
    properties: {}
};
