// Copyright 2016, EMC, Inc.

'use strict';

// NOTE: Due to the way our boilerplate.ipxe script works, this job
// will not work if run as the final task within a workflow (boilerplate.ipxe
// causes ipxe to make additional DHCP requests prior to actually
// running the ipxe script we've served down, and those requests will get
// ignored if there is no currently active workflow for the node.

module.exports = {
    friendlyName: 'Boot Profile',
    injectableName: 'Task.BootProfile',
    implementsTask: 'Task.Base.BootProfile',
    options: {},
    properties: {}
};
