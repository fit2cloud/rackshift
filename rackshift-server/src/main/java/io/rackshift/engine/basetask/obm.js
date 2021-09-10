// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Node Obm',
    injectableName: 'Task.Base.Obm.Node',
    runJob: 'Job.Obm.Node',
    optionsSchema: {
        "properties": {
            "action": {
                "enum": [
                    "clearSEL",
                    "identifyOff",
                    "identifyOn",
                    "identifyBlink",
                    "mcResetCold",
                    "NMI",
                    "powerButton",
                    "powerOff",
                    "powerOn",
                    "powerStatus",
                    "reboot",
                    "reset",
                    "setBootPxe",
                    "softReset",
                    "forceBootPxe",
                    "clearWatchDog",
                    "setBootDisk"
                ]
            },
            "obmService": {
                "enum": [
                    "amt-obm-service",
                    "apc-obm-service",
                    "ipmi-obm-service",
                    "noop-obm-service",
                    "panduit-obm-service",
                    "raritan-obm-service",
                    "redfish-obm-service",
                    "servertech-obm-service",
                    "vbox-obm-service",
                    "vmrun-obm-service",
                    "ucs-obm-service",
                    "dell-wsman-obm-service"
                ]
            },
        },
        "required": ["action"]
    },
    requiredProperties: {},
    properties: {
        power: {}
    }
};
