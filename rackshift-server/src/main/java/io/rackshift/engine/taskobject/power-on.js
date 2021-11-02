// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Power On Node',
    injectableName: 'Task.Obm.Node.PowerOn',
    implementsTask: 'Task.Base.Obm.Node',
    options: {
        action: 'powerOn'
    },
    properties: {
        power: {}
    }
};
