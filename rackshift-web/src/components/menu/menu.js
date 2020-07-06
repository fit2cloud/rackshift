export default {
    menus: [
        {
            "name": "OverView",
            "router": "/OverView",
            "requireRole": "admin",
            "order" : "1",
            childs: [
                {
                    "name": "使用向导",
                    "router": "/wizard",
                    "requireRole": "user,admin",
                    "order" : "1"
                }
            ]
        },
        {
            "name": "资源",
            "router": "/Resource",
            "requireRole": "admin,user",
            "order" : "2",
            childs: [
                {
                    "name": "裸金属",
                    "router": "/BareMetal",
                    "requireRole": "user,admin",
                    "order" : "1",
                },
                {
                    "name": "插件",
                    "router": "/Plugin",
                    "requireRole": "user,admin",
                    "order" : "2",
                },
                {
                    "name": "镜像",
                    "router": "/Image",
                    "requireRole": "user,admin",
                    "order" : "3",
                },
            ]
        },
        {
            "name": "网络",
            "router": "/Network",
            "requireRole": "admin",
            "order" : "3",
            childs: [
                {
                    "name": "子网",
                    "router": "/Subnet",
                    "requireRole": "admin",
                    "order" : "1",
                },
                {
                    "name": "发现",
                    "router": "/Discovery",
                    "requireRole": "admin",
                    "order" : "2",
                },
            ]
        },
        {
            "name": "控制",
            "router": "/Control",
            "requireRole": "admin",
            "order" : "4",
            childs: [
                {
                    "name": "工作流",
                    "router": "/WorkFlow",
                    "requireRole": "admin",
                    "order" : "1",
                },
                {
                    "name": "任务",
                    "router": "/Task",
                    "requireRole": "admin",
                    "order" : "2",
                },
                {
                    "name": "执行记录",
                    "router": "/WorkFlowLog",
                    "requireRole": "admin",
                    "order" : "3",
                },
            ]
        }, {
            "name": "系统设置",
            "router": "/Setting",
            "requireRole": "admin",
            "order" : "5",
            childs: [
                {
                    "name": "License",
                    "router": "/License",
                    "requireRole": "admin",
                    "order" : "1",
                },
                {
                    "name": "用户",
                    "router": "/User",
                    "requireRole": "admin",
                    "order" : "2",
                }, {
                    "name": "角色",
                    "router": "/Role",
                    "requireRole": "admin",
                    "order" : "3",
                },
                {
                    "name": "执行记录",
                    "router": "/WorkFlowLog",
                    "requireRole": "admin",
                    "order" : "4",
                }, {
                    "name": "Endpoint",
                    "router": "/Endpoint",
                    "requireRole": "admin",
                    "order" : "5",
                },
            ]
        }
    ]
}