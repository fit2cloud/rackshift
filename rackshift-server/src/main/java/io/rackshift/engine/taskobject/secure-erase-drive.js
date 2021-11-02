// Copyright 2016, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Secure Erase Drive',
    injectableName: 'Task.Drive.SecureErase',
    implementsTask: 'Task.Base.SecureErase',
    options: {
        eraseSettings: [],
        baseUri: '{{ api.server }}'
    },
    properties: {}
};
