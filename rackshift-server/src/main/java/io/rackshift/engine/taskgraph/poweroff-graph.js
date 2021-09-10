// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'PowerOff Node',
    injectableName: 'Graph.PowerOff.Node',
    tasks: [
        {
            label: 'poweroff',
            taskName: 'Task.Obm.Node.PowerOff'
        }
    ]
};
