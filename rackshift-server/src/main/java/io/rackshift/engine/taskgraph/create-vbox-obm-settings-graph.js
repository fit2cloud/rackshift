// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Create VirtualBox OBM Settings',
    injectableName: 'Graph.Obm.Vbox.CreateSettings',
    tasks: [
        {
            label: 'create-vbox-obm-settings',
            taskDefinition: {
                friendlyName: 'Create VirtualBox OBM settings',
                injectableName: 'Task.Obm.Vbox.CreateSettings',
                implementsTask: 'Task.Base.Obm.CreateSettings',
                options: {
                    service: 'vbox-obm-service',
                    config: {}
                },
                properties: {
                    obm: {
                        type: 'virtualbox'
                    }
                }
            }
        }
    ]
};
