'use strict';

module.exports = {
    friendlyName: "Add Hotspare",
    injectableName: "Task.Add.Hotspare",
    implementsTask: "Task.Base.Add.Hotspare",
    options: {
        username: null,
        password: null,
        volumeId: null,
        driveId: null, 
        hotspareType: 'ghs',
        ipAddress: null
    },
    properties: {}
};
