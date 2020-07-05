export default {
    menus: [
        {
            "name": "OverView",
            "router": "/OverView",
            "requireRole": "admin",
            childs: [
                {
                    "name": "使用向导",
                    "router": "/wizard",
                    "requireRole": "user,admin",
                }
            ]
        },
        {
            "name": "资源",
            "router": "/Resource",
            "requireRole": "admin,user",
            childs: [
                {
                    "name": "裸金属",
                    "router": "/BareMetal",
                    "requireRole": "user,admin",
                },
                {
                    "name": "插件",
                    "router": "/Plugin",
                    "requireRole": "user,admin",
                },
                {
                    "name": "镜像",
                    "router": "/Image",
                    "requireRole": "user,admin",
                },
            ]
        },
        {
            "name": "网络",
            "router": "/Network",
            "requireRole": "admin",
            childs: [
                {
                    "name": "子网",
                    "router": "/Subnet",
                    "requireRole": "admin",
                },
                {
                    "name": "发现",
                    "router": "/Discovery",
                    "requireRole": "admin",
                },
            ]
        },
        {
            "name": "控制",
            "router": "/Control",
            "requireRole": "admin",
            childs: [
                {
                    "name": "工作流",
                    "router": "/WorkFlow",
                    "requireRole": "admin",
                },
                {
                    "name": "任务",
                    "router": "/Task",
                    "requireRole": "admin",
                },
                {
                    "name": "执行记录",
                    "router": "/WorkFlowLog",
                    "requireRole": "admin",
                },
            ]
        }, {
            "name": "系统设置",
            "router": "/Setting",
            "requireRole": "admin",
            childs: [
                {
                    "name": "License",
                    "router": "/License",
                    "requireRole": "admin",
                },
                {
                    "name": "用户",
                    "router": "/User",
                    "requireRole": "admin",
                }, {
                    "name": "角色",
                    "router": "/Role",
                    "requireRole": "admin",
                },
                {
                    "name": "执行记录",
                    "router": "/WorkFlowLog",
                    "requireRole": "admin",
                }, {
                    "name": "Endpoint",
                    "router": "/Endpoint",
                    "requireRole": "admin",
                },
            ]
        }
    ]
}