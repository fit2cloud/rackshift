import i18n from "@/i18n/i18n";

export default {
    menus: [
        // {
        //     "name": 'OverView',
        //     "router": "/overview",
        //     "requireRole": "admin",
        //     "order": "1",
        //     "icon": "el-icon-view",
        //     childs: [
        //         {
        //             "name": 'Wizard',
        //             "router": "/wizard",
        //             "requireRole": "user,admin",
        //             "order": "2",
        //             "icon": "el-icon-question"
        //         }
        //     ]
        // },
        {
            "name": 'Resource',
            "router": "/Resource",
            "requireRole": "admin,user",
            "order": "3",
            "icon": "el-icon-folder-opened",
            childs: [
                {
                    "name": 'Bare Metal Server',
                    "router": "/bare-metal",
                    "requireRole": "user,admin",
                    "order": "4",
                    "icon": "el-icon-monitor"
                },

                {
                    "name": 'Image',
                    "router": "/image",
                    "requireRole": "user,admin",
                    "order": "5",
                    "icon": "el-icon-view"
                },
            ]
        },
        {
            "name": 'Network',
            "router": "/network",
            "requireRole": "admin",
            "order": "6",
            "icon": "el-icon-s-grid",
            childs: [
                {
                    "name": 'Subnet',
                    "router": "/network",
                    "requireRole": "admin",
                    "order": "7",
                    "icon": "el-icon-menu"
                },
                {
                    "name": 'Discovery',
                    "router": "/Discovery",
                    "requireRole": "admin",
                    "order": "2",
                    "icon": "el-icon-s-promotion"
                },
            ]
        },
        {
            "name": 'Control',
            "router": "/Control",
            "requireRole": "admin",
            "order": "9",
            "icon": "el-icon-unlock",
            childs: [
                {
                    "name": 'Workflow',
                    "router": "/WorkFlow",
                    "requireRole": "admin",
                    "order": "10",
                    "icon": "el-icon-s-unfold",
                },
                {
                    "name": 'Execution Log',
                    "router": "/execution-log",
                    "requireRole": "admin",
                    "order": "11",
                    "icon": "el-icon-s-order",
                },
            ]
        }, {
            "name": 'Configuration',
            "router": "/Setting",
            "requireRole": "admin",
            "order": "12",
            "icon": "el-icon-s-tools",
            childs: [
                {
                    "name": 'endpoint',
                    "router": "/endpoint",
                    "requireRole": "admin",
                    "order": "13",
                    "icon": "el-icon-s-promotion",
                },
                {
                    "name": 'User',
                    "router": "/user",
                    "requireRole": "admin",
                    "order": "14",
                    "icon": "el-icon-user-solid",
                }
                // {
                //     "name": "系统参数",
                //     "router": "/system-parameter",
                //     "requireRole": "admin",
                //     "order" : "5",
                //     "icon" : "el-icon-s-home",
                // },
                // {
                //     "name": 'Plugin',
                //     "router": "/Plugin",
                //     "requireRole": "user,admin",
                //     "order": "6",
                //     "icon": "el-icon-s-shop",
                // },
            ]
        }
    ]
}