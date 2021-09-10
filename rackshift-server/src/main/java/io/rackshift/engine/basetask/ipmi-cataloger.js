// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'IPMI Cataloger',
    injectableName: 'Task.Base.Ipmi.Catalog',
    runJob: 'Job.LocalIpmi.Catalog',
    optionsSchema: 'linux-command.json',
    requiredProperties: {
    },
    properties: {
        catalog: {}
    }
};
