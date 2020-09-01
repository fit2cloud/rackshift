export default {
    menus: [
        {
            "name": "OverView",
            "router": "/overview",
            "requireRole": "admin",
            "order" : "1",
            "icon" : "el-icon-view",
            childs: [
                {
                    "name": "使用向导",
                    "router": "/Wizard",
                    "requireRole": "user,admin",
                    "order" : "1",
                    "icon" : "el-icon-question"
                }
            ]
        },
        {
            "name": "资源",
            "router": "/Resource",
            "requireRole": "admin,user",
            "order" : "2",
            "icon" : "el-icon-folder-opened",
            childs: [
                {
                    "name": "裸金属",
                    "router": "/bare-metal",
                    "requireRole": "user,admin",
                    "order": "1",
                    "icon" : "el-icon-monitor"
                },

                {
                    "name": "镜像",
                    "router": "/image",
                    "requireRole": "user,admin",
                    "order" : "3",
                    "icon" : "el-icon-view"
                },
            ]
        },
        {
            "name": "网络",
            "router": "/network",
            "requireRole": "admin",
            "order" : "3",
            "icon" : "el-icon-s-grid",
            childs: [
                {
                    "name": "子网",
                    "router": "/network",
                    "requireRole": "admin",
                    "order" : "1",
                    "icon" : "el-icon-menu"
                },
                {
                    "name": "发现",
                    "router": "/Discovery",
                    "requireRole": "admin",
                    "order" : "2",
                    "icon" : "el-icon-s-promotion"
                },
            ]
        },
        {
            "name": "控制",
            "router": "/Control",
            "requireRole": "admin",
            "order" : "4",
            "icon" : "el-icon-unlock",
            childs: [
                {
                    "name": "工作流",
                    "router": "/WorkFlow",
                    "requireRole": "admin",
                    "order" : "1",
                    "icon" : "el-icon-s-unfold",
                },
                {
                    "name": "任务",
                    "router": "/Task",
                    "requireRole": "admin",
                    "order" : "2",
                    "icon" : "el-icon-s-operation",
                },
                {
                    "name": "执行记录",
                    "router": "/execution-log",
                    "requireRole": "admin",
                    "order" : "3",
                    "icon" : "el-icon-s-order",
                },
            ]
        }, {
            "name": "系统设置",
            "router": "/Setting",
            "requireRole": "admin",
            "order" : "5",
            "icon" : "el-icon-s-tools",
            childs: [
                {
                    "name": "Endpoint",
                    "router": "/endpoint",
                    "requireRole": "admin",
                    "order" : "5",
                    "icon" : "el-icon-s-promotion",
                },
                {
                    "name": "用户",
                    "router": "/user",
                    "requireRole": "admin",
                    "order": "2",
                    "icon" : "el-icon-user-solid",
                }, {
                    "name": "角色",
                    "router": "/role",
                    "requireRole": "admin",
                    "order": "3",
                    "icon" : "el-icon-user",
                },
                {
                    "name": "系统参数",
                    "router": "/system-parameter",
                    "requireRole": "admin",
                    "order" : "5",
                    "icon" : "el-icon-s-home",
                },
                {
                    "name": "插件",
                    "router": "/Plugin",
                    "requireRole": "user,admin",
                    "order" : "6",
                    "icon" : "el-icon-s-shop",
                },
            ]
        }
    ]
}