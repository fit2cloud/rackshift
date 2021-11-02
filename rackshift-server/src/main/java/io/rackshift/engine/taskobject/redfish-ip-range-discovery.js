// Copyright 2017, Dell, Inc.

'use strict';

module.exports = {
    friendlyName: "Redfish Ip Range Discovery",
    injectableName: "Task.Redfish.Ip.Range.Discovery",
    implementsTask: "Task.Base.Redfish.Ip.Range.Discovery",
    options: {
        ranges:[{
            startIp: null,
            endIp: null,
            credentials:{
                userName: null,
                password: null
            }
        }]
    },
    properties: {}
};
