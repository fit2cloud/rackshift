// Copyright 2017, Dell EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'get driveId catalog',
    injectableName: 'Task.Get.DriveId.Catalog',
    implementsTask: 'Task.Base.Get.Catalog.Values',
    options: {
        requestedData: [{
            source: 'driveId',
            keys: { driveId: 'data' }
        }]
    },
    properties: {}
};
