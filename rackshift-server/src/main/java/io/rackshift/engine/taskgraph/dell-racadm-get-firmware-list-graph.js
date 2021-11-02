// Copyright 2017, Dell EMC, Inc
'use strict';

module.exports = {
    friendlyName: 'Dell Racadm Get Firmware List Catalog Graph',
    injectableName: 'Graph.Dell.Racadm.GetFirmwareListCatalog',
    options: {
    "dell-racadm-get-firmware-list-catalog": {
        "updateExistingCatalog": true
    }
},
    tasks: [
        {
            label: 'dell-racadm-get-firmware-list-catalog',
            taskName: 'Task.Dell.Racadm.GetFirmwareListCatalog'
        }
    ]
};
