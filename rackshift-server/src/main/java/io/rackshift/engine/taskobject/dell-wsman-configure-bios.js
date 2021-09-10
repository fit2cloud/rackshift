// Copyright 2017, Dell, Inc.

'use strict';

module.exports = {
    friendlyName: "Dell Wsman Configure Bios",
    injectableName: "Task.Dell.Wsman.ConfigureBios",
    implementsTask: "Task.Base.Dell.Wsman.ConfigureBios",
    optionsSchema: 'dell-wsman-set-bios.json',
    options: {
        attributes: null,
        biosBootSequenceOrder: [''],
        hddSequenceOrder: [''],
        enableBootDevices: [''],
        disableBootDevices: [''],
        rebootJobType: 1
    },
    properties: {}
};
