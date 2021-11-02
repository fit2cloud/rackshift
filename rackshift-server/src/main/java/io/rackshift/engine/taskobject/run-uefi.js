// Copyright 2016, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Run UEFI Application',
    injectableName: 'Task.Run.Uefi',
    implementsTask: 'Task.Base.uefi',
    options: {
        profile: 'run-uefi.ipxe',
        repo: null,
        uefitool: null,
        args: ''
    },
    properties: {}
};
