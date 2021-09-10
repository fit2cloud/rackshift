// Copyright © 2017 Dell Inc. or its subsidiaries. All Rights Reserved.

'use strict';

module.exports = {
    friendlyName: "Dell Wsman RAID",
    injectableName: "Task.Dell.Wsman.RAID",
    implementsTask: "Task.Base.Dell.Wsman.RAID",
    optionsSchema: 'dell-wsman-RAID.json',
    options: {
        shutdownType: 0,
        removeXmlFile: true,
        ipAddress: "",
        username: "",
        password: ""
    },
    properties: {}
};
