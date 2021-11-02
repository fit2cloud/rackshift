// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Turn Off Node Identify LED',
    injectableName: 'Task.Obm.Node.IdentifyOff',
    implementsTask: 'Task.Base.Obm.Node',
    options: {
        action: 'identifyOff'
    },
    properties: {}
};
