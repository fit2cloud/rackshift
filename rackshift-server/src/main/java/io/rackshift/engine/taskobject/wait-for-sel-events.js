// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Wait for SEL events',
    injectableName: 'Task.Wait.Sel.Events',
    implementsTask: 'Task.Base.Wait.Sel.Events',
    optionsSchema: 'wait-sel-events.json',
    options: {
        alertFilters: null,
        pollInterval: null
    },
    properties: {}
};
