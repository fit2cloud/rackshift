// Copyright 2016, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Reset Action Node',
    injectableName: 'Task.Obm.Redfish.Actions.Reset',
    implementsTask: 'Task.Base.Obm.Node',
    options: {
        action: 'powerOn',
        obmServiceName: 'redfish-obm-service',
        force: false,
        target: null
    },
    properties: {
        power: {}
    }
};
