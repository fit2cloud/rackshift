// Copyright 2016, Dell, Inc.

'use strict';

module.exports = {
    friendlyName: "Wsman Client Trap Config",
    injectableName: "Task.Dell.Wsman.GetTrapConfig",
    implementsTask: "Task.Base.Dell.Wsman.GetTrapConfig",
    options: {
    	target: null,
    	verifySSL: false,
    	domain: 'wsman'
    },
    properties: {}
};
