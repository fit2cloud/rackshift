//Copyright 2017, Dell EMC, Inc.

'use strict';

module.exports = {
    friendlyName: "Ucs Service Profile Discovery",
    injectableName: "Task.Ucs.Service.Profile.Discovery",
    implementsTask: "Task.Base.UCS.Service.Profile.Discovery",
    options: {
        uri: null,
        ucs: null,
        username: null,
        password: null,
        verifySSL: false
    },
    properties: {}
};
