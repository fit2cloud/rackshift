// Copyright © 2017 Dell Inc. or its subsidiaries. All Rights Reserved.

'use strict';

module.exports = {
    friendlyName: "Dell Wsman Update Xml For Delete Volume",
    injectableName: "Task.Dell.Wsman.Delete.Volume.UpdateXml",
    implementsTask: "Task.Base.Dell.Wsman.Delete.Volume.UpdateXml",
    optionsSchema: 'dell-wsman-delete-volume.json',
    options: {
        shutdownType:0
    },
    properties: {}
};
