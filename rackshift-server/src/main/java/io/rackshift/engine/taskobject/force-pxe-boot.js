// Copyright 2017, Dell EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Set Node Force Pxeboot',
    injectableName: 'Task.Obm.Force.Pxe.Boot',
    implementsTask: 'Task.Base.Obm.Node',
    options: {
        action: 'forceBootPxe'
    },
    properties: {
        power: {}
    }
};
