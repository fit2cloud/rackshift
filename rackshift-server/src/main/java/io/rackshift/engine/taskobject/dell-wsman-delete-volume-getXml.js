// Copyright © 2017 Dell Inc. or its subsidiaries. All Rights Reserved.

'use strict';

module.exports = {
    friendlyName: "Dell Wsman GetXml for delete volume",
    injectableName: "Task.Dell.Wsman.Delete.Volume.GetXml",
    implementsTask: "Task.Base.Dell.Wsman.GetXml",
    optionsSchema: 'dell-wsman-delete-volume.json',
    options: {
        shutdownType:0,
        ipAddress: "",
        username: "",
        password: ""
    },
    properties: {}
};
