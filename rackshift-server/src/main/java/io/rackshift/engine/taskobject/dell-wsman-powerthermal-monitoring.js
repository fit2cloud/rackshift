// Copyright 2016, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'dell wsman powerthermal monitoring',
    injectableName: 'Task.Dell.Wsman.Powerthermal',
    implementsTask: 'Task.Base.Dell.Wsman.PowerThermal',
    options: {
        action: 'powercapping',
        "powerCap":null,
        "enableCapping":true 
         
     },
    properties: {}
};

