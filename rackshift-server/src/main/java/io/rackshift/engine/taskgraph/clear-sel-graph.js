// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Clear the System Event Log',
    injectableName: 'Graph.ClearSEL.Node',
    tasks: [
        {
            label: 'clearsel',
            taskName: 'Task.Obm.Node.ClearSEL'
        }
    ]
};
