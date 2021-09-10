// Copyright 2016, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'dell racadm get config catalog',
    injectableName: 'Task.Dell.Racadm.GetConfigCatalog',
    implementsTask: 'Task.Base.Dell.Racadm.Catalog',
    optionsSchema: 'dell-racadm-catalog.json',
    options: {
        action: 'getConfigCatalog'
    },
    properties: {}
};
