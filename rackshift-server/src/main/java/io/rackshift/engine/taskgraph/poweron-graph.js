// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'PowerOn Node',
    injectableName: 'Graph.PowerOn.Node',
    tasks: [
        {
            label: 'poweron',
            taskName: 'Task.Obm.Node.PowerOn'
        }
    ]
};
