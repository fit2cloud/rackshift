// Copyright 2016, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Create IPMI OBM settings',
    injectableName: 'Task.Obm.Ipmi.CreateSettings',
    implementsTask: 'Task.Base.Obm.Ipmi.CreateSettings',
    options: {
        ipmichannel: null,
        user: '{{ context.user || monorail }}',
        password: '{{ context.password  }}',
    },
    properties: {}
};

