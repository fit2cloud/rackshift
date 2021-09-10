// Copyright 2016, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: "ucs Discovery",
    injectableName: "Task.Ucs.Discovery",
    implementsTask: "Task.Base.UCS.Discovery",
    options: {
        uri: null,
        ucs: null,
        username: null,
        password: null,
        verifySSL: false
    },
    properties: {}
};
