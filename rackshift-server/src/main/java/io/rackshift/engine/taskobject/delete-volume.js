'use strict';

module.exports = {
    friendlyName: "Delete Volume",
    injectableName: "Task.Delete.Volume",
    implementsTask: "Task.Base.Delete.Volume",
    options: {
        username: null,
        password: null,
        volumeId: null,
        ipAddress: null
    },
    properties: {}
};
