// Copyright 2016, Dell EMC, Inc.

'use strict';

module.exports = {
    friendlyName: "Dell Wsman PostDiscovery",
    injectableName: "Task.Dell.Wsman.PostDiscovery",
    implementsTask: "Task.Base.Dell.Wsman.PostDiscovery",
    options: {
		data: null,
		credentials:{
			user: null,
			password: null
		}
    },
    properties: {}
};
