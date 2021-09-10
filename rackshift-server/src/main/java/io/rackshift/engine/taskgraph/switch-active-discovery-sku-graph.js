// Copyright 2016, EMC, Inc.

"use strict";

module.exports = {
    "friendlyName": "Active Switch SKU Discovery",
    "injectableName": "Graph.SKU.Switch.Discovery.Active",
    "options": {
        "defaults": {
            "nodeId": null,
            "nodeIds": [ "{{ options.nodeId }}" ],
            "graphOptions": {
                "target": null
            },
        },
        "vendor-discovery-graph": {
            "graphName": null
        }
    },
    "tasks": [
        {
            "label": "vendor-discovery-graph",
            "taskDefinition": {
                "friendlyName": "Run Vendor-specific Switch Discovery",
                "injectableName": "Task.Graph.Run.Switch.Discovery.VendorSpecific",
                "implementsTask": "Task.Base.Graph.Run",
                "options": {
                    "graphName": null,
                    "graphOptions": {}
                },
                "properties": {}
            },
        },
        {
            "label": "generate-sku",
            "waitOn": {
                "vendor-discovery-graph": "succeeded"
            },
            "taskName": "Task.Catalog.GenerateSku"
        },
        {
            "label": "generate-tag",
            "waitOn": {
                "vendor-discovery-graph": "succeeded"
            },
            "taskName": "Task.Catalog.GenerateTag"
        },
        {
            "label": "run-sku-graph",
            "taskDefinition": {
                "friendlyName": "Run SKU-specific graph",
                "injectableName": "Task.Graph.Run.SkuSpecific",
                "implementsTask": "Task.Base.Graph.RunSku",
                "options": {
                    "nodeId": null
                },
                "properties": {}
            },
            "waitOn": {
                "generate-sku": "succeeded"
            }

        },
        // NOTE: we have to run any extra commands within another graph so that node
        // locking can be restricted to inner workflows
        {
            "label": "run-sku-post-hooks",
            "taskDefinition": {
                "friendlyName": "Run Switch SKU graph post-hooks",
                "injectableName": "Task.Graph.Run.Switch.Discovery.Hooks.Post",
                "implementsTask": "Task.Base.Graph.Run",
                "options": {
                    "graphName": "Graph.Switch.SKU.Discovery.Hooks.Post",
                    "graphOptions": {}
                },
                "properties": {}
            },
            "waitOn": {
                "run-sku-graph": "succeeded"
            }
        }
    ]
};
