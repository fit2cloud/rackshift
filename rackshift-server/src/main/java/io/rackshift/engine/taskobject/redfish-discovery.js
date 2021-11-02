// Copyright 2016, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: "Redfish Client Discovery",
    injectableName: "Task.Redfish.Discovery",
    implementsTask: "Task.Base.Redfish.Discovery",
    options: {
        uri: null,
        username: null,
        password: null,
        verifySSL: false
    },
    properties: {}
};
