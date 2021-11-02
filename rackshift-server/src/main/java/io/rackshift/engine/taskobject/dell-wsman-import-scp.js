// Copyright 2017, Dell, Inc.

'use strict';

module.exports = {
    friendlyName: "Dell Wsman Import SCP",
    injectableName: "Task.Dell.Wsman.Import.SCP",
    implementsTask: "Task.Base.Dell.Wsman.Import.SCP",
    options: {
        serverIP:null,
        serverUsername:null,
        serverPassword:null,
        shareType:null,
        shareAddress:null,
        shareName:null,
        fileName:null,
        shutdownType: null,
        componentNames: null
    },
    properties: {}
};