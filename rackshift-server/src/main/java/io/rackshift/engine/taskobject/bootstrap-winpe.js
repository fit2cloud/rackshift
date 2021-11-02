// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Bootstrap WinPE',
    injectableName: 'Task.WinPE.Bootstrap',
    implementsTask: 'Task.Base.WinPE.Bootstrap',
    options: {
        profile: 'winpe.ipxe'
    },
    properties: {}
};

