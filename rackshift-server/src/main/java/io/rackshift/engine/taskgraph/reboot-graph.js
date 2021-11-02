// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Reboot Node',
    injectableName: 'Graph.Reboot.Node',
    tasks: [
        {
            label: 'reboot',
            taskName: 'Task.Obm.Node.Reboot'
        }
    ]
};
