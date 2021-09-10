// Copyright 2017, Dell, Inc.

'use strict';

module.exports = {
    friendlyName: "Dell Wsman Update System Configuration Components",
    injectableName: "Task.Dell.Wsman.UpdateSystemConfigComponents",
    implementsTask: "Task.Base.Dell.Wsman.UpdateSystemConfigComponents",
    options: {
        serverIP:null,
        serverUsername:null,
        serverPassword:null,
        shareType:null,
        shareAddress:null,
        shareName:null,
        fileName:null,
        shutdownType: null,
        serverComponents: null,
        cleanup: null,
        forceUpdate: null
    },
    properties: {}
};
