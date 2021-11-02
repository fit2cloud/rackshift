// Copyright © 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.

'use strict';

module.exports = {
    friendlyName: 'download http file',
    injectableName: 'Task.Download.Http.File',
    implementsTask: 'Task.Base.Download.Http.File',
    options: {
        filePath: null,
        serverFilePath: null
    },
    properties: {}
};
