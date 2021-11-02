// Copyright 2017, Dell, Inc.

'use strict';

module.exports = {
    friendlyName: "Dell Wsman Export SCP",
    injectableName: "Task.Dell.Wsman.Export.SCP",
    implementsTask: "Task.Base.Dell.Wsman.Export.SCP",
    options: {
        serverIP:null,
        serverUsername:null,
        serverPassword:null,
        shareType:null,
        shareAddress:null,
        shareName:null,
        fileName:null
    },
    properties: {}
};