// Copyright © 2017 Dell Inc. or its subsidiaries. All Rights Reserved.

'use strict';

module.exports = {
    friendlyName: "Dell Wsman UpdateXml for add volume",
    injectableName: "Task.Dell.Wsman.Add.Volume.UpdateXml",
    implementsTask: "Task.Base.Dell.Wsman.Add.Volume.UpdateXml",
    optionsSchema: 'dell-wsman-add-volume-updateXml.json',
    options: {
        raidLevel: 0,
        stripeSize: 128,
        writePolicy: "WriteBack"
    },
    properties: {}
};
