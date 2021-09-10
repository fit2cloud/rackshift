// Copyright 2016, EMC, Inc.

'use strict';


module.exports = {
    friendlyName: 'Sftp',
    injectableName: 'Task.Base.Sftp',
    runJob: 'Job.Sftp',
    requiredOptions: [
        'fileSource',
        'fileDestination'
    ],
    requiredProperties: {},
    properties: {}
};
