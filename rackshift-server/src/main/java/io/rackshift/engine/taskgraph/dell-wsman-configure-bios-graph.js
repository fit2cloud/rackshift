// Copyright 2017, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Dell WSMAN Configure BIOS',
    injectableName: 'Graph.Dell.Wsman.ConfigureBios',
    options: {
        defaults: {
            attributes: null,
            biosBootSequenceOrder: [''],
            hddSequenceOrder: [''],
            enableBootDevices: [''],
            disableBootDevices: [''],
            rebootJobType: 1
        }
    },
    tasks: [
        {
            label: 'dell-wsman-configure-bios',
            taskName: 'Task.Dell.Wsman.ConfigureBios'
        },
        {
            label: 'dell-wsman-get-bios',
            taskName: 'Task.Dell.Wsman.GetBios',
            waitOn: {
                'dell-wsman-configure-bios': 'succeeded'
            }
        }
    ]
};
