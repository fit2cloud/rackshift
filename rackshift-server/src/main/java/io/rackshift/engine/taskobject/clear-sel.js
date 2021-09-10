// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Clear the System Event Log',
    injectableName: 'Task.Obm.Node.ClearSEL',
    implementsTask: 'Task.Base.Obm.Node',
    options: {
        action: 'clearSEL'
    },
    properties: {}
};
