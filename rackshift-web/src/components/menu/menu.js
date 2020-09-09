import i18n from "@/i18n/i18n";

export default {
    menus: [
        {
            "name": i18n.t('OverView'),
            "router": "/overview",
            "requireRole": "admin",
            "order": "1",
            "icon": "el-icon-view",
            childs: [
                {
                    "name": i18n.t('Wizard'),
                    "router": "/Wizard",
                    "requireRole": "user,admin",
                    "order": "2",
                    "icon": "el-icon-question"
                }
            ]
        },
        {
            "name": i18n.t('Resource'),
            "router": "/Resource",
            "requireRole": "admin,user",
            "order": "3",
            "icon": "el-icon-folder-opened",
            childs: [
                {
                    "name": i18n.t('Bare Metal Server'),
                    "router": "/bare-metal",
                    "requireRole": "user,admin",
                    "order": "4",
                    "icon": "el-icon-monitor"
                },

                {
                    "name": i18n.t('Image'),
                    "router": "/image",
                    "requireRole": "user,admin",
                    "order": "5",
                    "icon": "el-icon-view"
                },
            ]
        },
        {
            "name": i18n.t('Network'),
            "router": "/network",
            "requireRole": "admin",
            "order": "6",
            "icon": "el-icon-s-grid",
            childs: [
                {
                    "name": i18n.t('Subnet'),
                    "router": "/network",
                    "requireRole": "admin",
                    "order": "7",
                    "icon": "el-icon-menu"
                },
                // {
                //     "name": i18n.t('Discovery'),
                //     "router": "/Discovery",
                //     "requireRole": "admin",
                //     "order": "2",
                //     "icon": "el-icon-s-promotion"
                // },
            ]
        },
        {
            "name": i18n.t('Control'),
            "router": "/Control",
            "requireRole": "admin",
            "order": "9",
            "icon": "el-icon-unlock",
            childs: [
                {
                    "name": i18n.t('Workflow'),
                    "router": "/WorkFlow",
                    "requireRole": "admin",
                    "order": "10",
                    "icon": "el-icon-s-unfold",
                },
                {
                    "name": i18n.t('Execution Log'),
                    "router": "/execution-log",
                    "requireRole": "admin",
                    "order": "11",
                    "icon": "el-icon-s-order",
                },
            ]
        }, {
            "name": i18n.t('Configuration'),
            "router": "/Setting",
            "requireRole": "admin",
            "order": "12",
            "icon": "el-icon-s-tools",
            childs: [
                {
                    "name": "Endpoint",
                    "router": "/endpoint",
                    "requireRole": "admin",
                    "order": "13",
                    "icon": "el-icon-s-promotion",
                },
                {
                    "name": i18n.t('User'),
                    "router": "/user",
                    "requireRole": "admin",
                    "order": "14",
                    "icon": "el-icon-user-solid",
                }, {
                    "name": i18n.t('Role'),
                    "router": "/role",
                    "requireRole": "admin",
                    "order": "15",
                    "icon": "el-icon-user",
                },
                // {
                //     "name": "系统参数",
                //     "router": "/system-parameter",
                //     "requireRole": "admin",
                //     "order" : "5",
                //     "icon" : "el-icon-s-home",
                // },
                // {
                //     "name": i18n.t('Plugin'),
                //     "router": "/Plugin",
                //     "requireRole": "user,admin",
                //     "order": "6",
                //     "icon": "el-icon-s-shop",
                // },
            ]
        }
    ]
}