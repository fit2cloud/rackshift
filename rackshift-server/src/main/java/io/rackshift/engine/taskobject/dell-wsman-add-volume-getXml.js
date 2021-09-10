// Copyright © 2017 Dell Inc. or its subsidiaries. All Rights Reserved.

'use strict';

module.exports = {
    friendlyName: "Dell Wsman GetXml for add volume",
    injectableName: "Task.Dell.Wsman.Add.Volume.GetXml",
    implementsTask: "Task.Base.Dell.Wsman.GetXml",
    optionsSchema: 'dell-wsman-add-volume-getXml.json',
    options: {
        shutdownType: 0,
        ipAddress: "",
        username: "",
        password: ""
    },
    properties: {}
};
