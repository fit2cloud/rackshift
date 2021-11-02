// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Cold reset BMC',
    injectableName: 'Task.Obm.Node.McResetCold',
    implementsTask: 'Task.Base.Obm.Node',
    options: {
        action: 'mcResetCold'
    },
    properties: { }
};
