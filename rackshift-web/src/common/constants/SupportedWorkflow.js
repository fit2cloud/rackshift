let supportedWorkflow = [
    {
        'injectableName': 'Graph.InstallCentOS',
        'friendlyName': '安装Centos7 64位版',
        settable: true,
        brands: ['HP', 'DELL', 'Inspur']
    },
    {
        'injectableName': 'Graph.Dell.perccli.Catalog',
        'friendlyName': '搜集Dell服务器磁盘Raid信息',
        settable: false,
        brands: ['DELL']
    },
    {
        'injectableName': 'Graph.Raid.Delete.MegaRAID',
        'friendlyName': '清空Dell服务器磁盘和Raid信息',
        settable: false,
        brands: ['DELL']
    },
    {
        'injectableName': 'Graph.Raid.Create.PercRAID',
        'friendlyName': '创建Dell服务器磁盘Raid虚拟磁盘',
        settable: true,
        brands: ['DELL']
    },
];

export {
    supportedWorkflow
}