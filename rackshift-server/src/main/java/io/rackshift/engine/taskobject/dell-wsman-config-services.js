// Copyright 2016, Dell, Inc.

'use strict';

module.exports = {
    friendlyName: "Wsman Config Services",
    injectableName: "Task.Dell.Wsman.Config.Services",
    implementsTask: "Task.Base.Dell.Wsman.Config.Services",
    options: {
    	configServer: '127.0.0.1:9600',
    	verifySSL: false,
    	_taskTimeout: 900000,
    	domain: 'wsman'
    },
    properties: {}
};
