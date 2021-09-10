// Copyright 2017,Dell EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'dell racadm get firmware list catalog',
    injectableName: 'Task.Dell.Racadm.GetFirmwareListCatalog',
    implementsTask: 'Task.Base.Dell.Racadm.Catalog',
    optionsSchema: 'dell-racadm-catalog.json',
    options: {
        action: 'getSoftwareList'
    },
    properties: {}
};



