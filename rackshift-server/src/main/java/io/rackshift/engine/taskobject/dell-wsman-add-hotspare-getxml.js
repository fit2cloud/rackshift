// Copyright © 2017 Dell Inc. or its subsidiaries. All Rights Reserved.

'use strict';

module.exports = {
    friendlyName: "Dell Wsman GetXml for Add Hotspare",
    injectableName: "Task.Dell.Wsman.Add.Hotspare.GetXml",
    implementsTask: "Task.Base.Dell.Wsman.GetXml",
    optionsSchema: 'dell-wsman-add-hotspare-getxml.json',
    options: {
        shutdownType: 0
    },
    properties: {}
};
