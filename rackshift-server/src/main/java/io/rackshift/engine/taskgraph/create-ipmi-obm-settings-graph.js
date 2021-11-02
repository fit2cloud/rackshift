// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Create IPMI OBM Settings',
    injectableName: 'Graph.Obm.Ipmi.CreateSettings',
    tasks: [
        {
            label: 'create-ipmi-obm-settings',
            taskDefinition: {
                friendlyName: 'Create IPMI OBM settings',
                injectableName: 'Task.Obm.Ipmi.CreateSettings',
                implementsTask: 'Task.Base.Obm.Ipmi.CreateSettings',
                options: {
                    ipmichannel: null,
                    user: null,
                    password: null
                },
                properties: { }
            }
        }
    ]
};
