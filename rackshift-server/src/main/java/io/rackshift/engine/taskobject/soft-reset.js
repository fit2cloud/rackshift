// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Soft Reset Node',
    injectableName: 'Task.Obm.Node.Reset.Soft',
    implementsTask: 'Task.Base.Obm.Node',
    options: {
        action: 'softReset'
    },
    properties: {
        power: {
            state: "softReset"
        }
    }
};
