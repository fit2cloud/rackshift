// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Catalog mgmt bmc',
    injectableName: 'Task.Catalog.Mgmt.bmc',
    implementsTask: 'Task.Base.Ipmi.Catalog',
    options: {
        commands: [
            {
                command: 'lan print',
                acceptedResponseCodes: [1]
            },
            'sel',
            'sel list -c',
            'mc info',
            {
                command: 'user summary 1',
                acceptedResponseCodes: [1]
            },
            {
                command: '-c user list 1',
                acceptedResponseCodes: [1]
            },
            'fru',
            {
                command: 'lan print 2',
                acceptedResponseCodes: [1]
            },
            {
                command: 'user summary 2',
                acceptedResponseCodes: [1]
            },
            {
                command: '-c user list 2',
                acceptedResponseCodes: [1]
            },
            {
                command: 'lan print 3',
                acceptedResponseCodes: [1]
            },
            {
                command: 'user summary 3',
                acceptedResponseCodes: [1]
            },
            {
                command: '-c user list 3',
                acceptedResponseCodes: [1]
            },
            {
                command: 'lan print 4',
                acceptedResponseCodes: [1]
            },
            {
                command: 'user summary 4',
                acceptedResponseCodes: [1]
            },
            {
                command: '-c user list 4',
                acceptedResponseCodes: [1]
            },
            {
                command: 'lan print 5',
                acceptedResponseCodes: [1]
            },
            {
                command: 'user summary 5',
                acceptedResponseCodes: [1]
            },
            {
                command: '-c user list 5',
                acceptedResponseCodes: [1]
            },
            {
                command: 'lan print 6',
                acceptedResponseCodes: [1]
            },
            {
                command: 'user summary 6',
                acceptedResponseCodes: [1]
            },
            {
                command: '-c user list 6',
                acceptedResponseCodes: [1]
            },
            {
                command: 'lan print 7',
                acceptedResponseCodes: [1]
            },
            {
                command: 'user summary 7',
                acceptedResponseCodes: [1]
            },
            {
                command: '-c user list 7',
                acceptedResponseCodes: [1]
            },
            {
                command: 'lan print 8',
                acceptedResponseCodes: [1]
            },
            {
                command: 'user summary 8',
                acceptedResponseCodes: [1]
            },
            {
                command: '-c user list 8',
                acceptedResponseCodes: [1]
            },
            {
                command: 'lan print 9',
                acceptedResponseCodes: [1]
            },
            {
                command: 'user summary 9',
                acceptedResponseCodes: [1]
            },
            {
                command: '-c user list 9',
                acceptedResponseCodes: [1]
            },
            {
                command: 'lan print 10',
                acceptedResponseCodes: [1]
            },
            {
                command: 'user summary 10',
                acceptedResponseCodes: [1]
            },
            {
                command: '-c user list 10',
                acceptedResponseCodes: [1]
            },
            {
                command: 'lan print 11',
                acceptedResponseCodes: [1]
            },
            {
                command: 'user summary 11',
                acceptedResponseCodes: [1]
            },
            {
                command: '-c user list 11',
                acceptedResponseCodes: [1]
            },
            {
                command: 'lan print 12',
                acceptedResponseCodes: [1]
            },
            {
                command: 'user summary 12',
                acceptedResponseCodes: [1]
            },
            {
                command: '-c user list 12',
                acceptedResponseCodes: [1]
            },
            {
                command: 'lan print 13',
                acceptedResponseCodes: [1]
            },
            {
                command: 'user summary 13',
                acceptedResponseCodes: [1]
            },
            {
                command: '-c user list 13',
                acceptedResponseCodes: [1]
            },
            {
                command: 'lan print 14',
                acceptedResponseCodes: [1]
            },
            {
                command: 'user summary 14',
                acceptedResponseCodes: [1]
            },
            {
                command: '-c user list 14',
                acceptedResponseCodes: [1]
            },
            {
                command: 'lan print 15',
                acceptedResponseCodes: [1]
            },
            {
                command: 'user summary 15',
                acceptedResponseCodes: [1]
            },
            {
                command: '-c user list 15',
                acceptedResponseCodes: [1]
            }
         ]
    },
    properties: {
        catalog: {
            type: 'bmc'
        }
    }
};
