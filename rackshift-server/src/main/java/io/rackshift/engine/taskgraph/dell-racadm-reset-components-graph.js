// Copyright 2017, DELL EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Dell Racadm Reset Components Graph',
    injectableName: 'Graph.Dell.Racadm.ResetComponents',
    options: {
        defaults: {
            components: []
        }
    },
    tasks: [{
        label: 'dell-racadm-Reset-Components',
        taskDefinition: {
             friendlyName: 'dell racadm reset components',
             injectableName: 'Task.Dell.Racadm.ResetComponents',
             implementsTask: 'Task.Base.Dell.Racadm.Control',
             optionsSchema: 'dell-racadm-control.json',
             options: {
                action: 'resetComponents'
             },
             properties: {}
        }
    }]
};

