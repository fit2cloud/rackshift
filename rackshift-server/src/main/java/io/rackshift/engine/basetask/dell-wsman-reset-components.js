// Copyright 2017, Dell EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Dell Wsman Reset Components Base',
    injectableName: 'Task.Base.Dell.Wsman.Reset.Components',
    runJob: 'Job.Dell.Wsman.Reset.Components',
    optionsSchema: 'dell-wsman-reset-components.json',
    requiredOptions: [
        "components"
    ],
    requiredProperties: {},
    properties:{}
};
