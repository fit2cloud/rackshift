// Copyright 2017, DELL EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'dell racadm reset components',
    injectableName: 'Task.Dell.Racadm.ResetComponents',
    implementsTask: 'Task.Base.Dell.Racadm.Control',
    optionsSchema: 'dell-racadm-control.json',
    options: {
        action: 'resetComponents'
    },
    properties: {}
};
