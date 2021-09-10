// Copyright 2016, Dell, Inc.

'use strict';

module.exports = {
    friendlyName: "Wsman Client Bios",
    injectableName: "Task.Dell.Wsman.GetBios",
    implementsTask: "Task.Base.Dell.Wsman.GetBios",
    optionsSchema: 'dell-wsman-get-bios.json',
    options: {
        target: "",
        verifySSL: false,
        domain: 'wsman'
    },
    properties: {}
};
