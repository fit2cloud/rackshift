//Copyright 2016, EMC, Inc.

'use strict';

module.exports = {
		friendlyName: 'dell wsman create repository ',
		injectableName: 'Task.Dell.Wsman.Create.Repo',
		implementsTask: 'Task.Base.Dell.Wsman.Create.Repo',
		options: {
		   	action: 'createRepository',
		   	catalogFilePath: null,
		   	targetFilePath: null,
		   	updates: null
		},
		properties: {}
};

