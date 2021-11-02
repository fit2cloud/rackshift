// Copyright 2016, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: "EMC Compose System",
    injectableName: "Task.Emc.Compose.System",
    implementsTask: "Task.Base.Emc.Compose.System",
    options: { 
        endpoints: null,
        name: null,
        action: 'compose'
    },
    properties: {}
};
