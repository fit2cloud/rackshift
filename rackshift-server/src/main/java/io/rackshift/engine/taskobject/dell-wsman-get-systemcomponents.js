// Copyright 2017, Dell, Inc.

'use strict';

module.exports = {
    friendlyName: "Dell Wsman Get System Configuration Components",
    injectableName: "Task.Dell.Wsman.GetSystemConfigComponents",
    implementsTask: "Task.Base.Dell.Wsman.GetSystemConfigComponents",
    optionsSchema: "dell-wsman-get-config-catalog.json",
    options: {
        shutdownType: 0,
        componentNames: []
    },
    properties: {}
};
