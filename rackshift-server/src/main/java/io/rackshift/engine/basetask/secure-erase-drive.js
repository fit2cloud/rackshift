// Copyright 2016, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Secure Erase Commands',
    injectableName: 'Task.Base.SecureErase',
    runJob: 'Job.Drive.SecureErase',
    requiredOptions: [
        'eraseSettings', 'baseUri'
    ],
    requiredProperties: {
        'os.linux.type': 'microkernel'
    },
    properties: {}
};
