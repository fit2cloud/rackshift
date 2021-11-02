// Copyright © 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.

'use strict';

module.exports = {
    friendlyName: 'Clear Ipmi Watchdog',
    injectableName: 'Task.Clear.Ipmi.Watchdog',
    implementsTask: 'Task.Base.Obm.Node',
    options: {
       action: 'clearWatchDog'
    },
    properties: {
        power: {}
    }
};
