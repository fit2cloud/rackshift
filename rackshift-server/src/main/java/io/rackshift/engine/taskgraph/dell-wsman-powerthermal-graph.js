// Copyright 2016, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Dell wsman PowerThermal Graph',
    injectableName: 'Graph.Dell.Wsman.Powerthermal',
    options: {
        defaults: {
        	        	           
            powerCap : null,
            enableCapping : null

        }
    },
    tasks: [
        {
            label: 'dell-wsman-powerthermal-capping',
            taskName: 'Task.Dell.Wsman.Powerthermal'
        }
    ]
};

