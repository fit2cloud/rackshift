// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Zerotouch vEOS Graph',
    injectableName: 'Graph.Arista.Zerotouch.vEOS',
    tasks: [
        {
            label: 'zerotouch-veos',
            taskDefinition: {
                friendlyName: 'Arista Zerotouch vEOS',
                injectableName: 'Task.Inline.Arista.Zerotouch.vEOS',
                implementsTask: 'Task.Base.Arista.Zerotouch',
                options: {
                    profile: 'zerotouch-configure.zt',
                    bootConfig: 'arista-boot-config',
                    startupConfig: 'arista-startup-config',
                    eosImage: 'common/zerotouch-vEOS.swi',
                    bootfile: 'zerotouch-vEOS.swi',
                    hostname: 'RenasarVEOS'
                },
                properties: {
                    os: {
                        switch: {
                            type: 'vEOS',
                            virtual: true
                        }
                    }
                }
            }
        }
    ]
};
