# RackShift

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/2d7d7a82829e4e4e80c0f2a9aa2397ca)](https://app.codacy.com/manual/rackshift/rackshift?utm_source=github.com&utm_medium=referral&utm_content=rackshift/rackshift&utm_campaign=Badge_Grade_Dashboard)

> [English](README_EN.md) | 中文

RackShift 是一款基于 RackHD 的裸金属服务器全生命周期的开源纳管平台，包括裸金属的上架、RAID 配置、固件升级、安装 OS、部署中间件，WEB Kvm等裸金属服务器运维人员需要的系列功能。提供可视化，便捷的操作界面，全面兼容世面上主流服务器厂商，如 DELL、 HP、 浪潮、 华为、 联想、 IBM等。

## 功能特性
- RBAC权限管理  
- 主流品牌裸金属服务器信息管理以及生命周期管控
- 主流品牌裸金属服务器定制化 RAID
- 主流品牌裸金属服务器 WEB Kvm
- 主流品牌裸金属服务器 Linux/Windows 安装工作流  
- 主流品牌裸金属服务器 带外管理
- 自定义 Workflow工作流
- 多 Endpoint、 PXE DHCP、TFTP 管理

## UI
 
![runoob](https://f2c-south.oss-cn-shenzhen.aliyuncs.com/RackHD-dont-del/RackHD%E4%B8%80%E9%94%AE%E5%8C%85/3.0/rs2.png)
![runoob](https://f2c-south.oss-cn-shenzhen.aliyuncs.com/RackHD-dont-del/RackHD%E4%B8%80%E9%94%AE%E5%8C%85/3.0/rs3.png)
![runoob](https://f2c-south.oss-cn-shenzhen.aliyuncs.com/RackHD-dont-del/RackHD%E4%B8%80%E9%94%AE%E5%8C%85/3.0/rs4.png)

## 技术优势
  
- 全生命周期: 能够覆盖裸金属从发现、上架、部署、运维全生命周期的管控
- 自动化：所有工作流全部基于PXE或者带外协议实现自动化，真正解放机房运维人员
- 易操作: 操作友好，解决开源裸金属装机平台不够直观，出现问题而不知道解决的痛点

## 功能列表

<table class="wrapped confluenceTable"><colgroup><col><col><col></colgroup><tbody><tr><th class="confluenceTh">顶层菜单</th><th class="confluenceTh">二级菜单</th><th class="confluenceTh">用例</th></tr><tr><td rowspan="2" class="confluenceTd">Overview</td><td rowspan="2" class="confluenceTd">使用向导</td><td class="confluenceTd"><p>第一次装机需要的配置</p></td></tr><tr><td colspan="1" class="confluenceTd">帮助文档</td></tr><tr><td rowspan="15" class="confluenceTd">资源<br><br></td><td rowspan="11" class="confluenceTd">裸金属</td><td class="confluenceTd"><p>查询过滤列出所有被发现的裸金属服务器</p></td></tr><tr><td colspan="1" class="confluenceTd">开关机重启 PXE</td></tr><tr><td colspan="1" class="confluenceTd">标签编辑</td></tr><tr><td colspan="1" class="confluenceTd">编辑 IPMI 账号</td></tr><tr><td colspan="1" class="confluenceTd">VNC/WEB KVM 连接</td></tr><tr><td colspan="1" class="confluenceTd">查看 Catalogs</td></tr><tr><td colspan="1" class="confluenceTd">查看 CPU，内存，磁盘，网卡，主板等信息，BIOS，网卡 BIOS 版本等信息</td></tr><tr><td colspan="1" class="confluenceTd">重做 Raid 重装系统</td></tr><tr><td colspan="1" class="confluenceTd">添加/删除</td></tr><tr><td colspan="1" class="confluenceTd">执行工作流，自定义参数配置</td></tr><tr><td colspan="1" class="confluenceTd">全生命周期管理，多机器并行，同一台机器多任务串行执行工作流</td></tr><tr><td rowspan="4" class="confluenceTd">镜像</td><td class="confluenceTd"><p>查询过滤列列出所有镜像</p></td></tr><tr><td colspan="1" class="confluenceTd">上传镜像</td></tr><tr><td colspan="1" class="confluenceTd">使用本地镜像</td></tr><tr><td colspan="1" class="confluenceTd">删除镜像</td></tr><tr><td rowspan="6" class="confluenceTd">网络</td><td rowspan="4" class="confluenceTd">子网</td><td class="confluenceTd"><p>配置网段信息（startIp ，endIp ，netmask ，dns ，gateway ，vlan ）</p></td></tr><tr><td colspan="1" class="confluenceTd">配置 Endpoint DHCP服务</td></tr><tr><td colspan="1" class="confluenceTd">查看所有Endpoint子网</td></tr><tr><td colspan="1" class="confluenceTd">编辑子网，查看DHCP IP 分配日志</td></tr><tr><td rowspan="2" class="confluenceTd">发现</td><td class="confluenceTd"><p>列出所有的发现规则</p></td></tr><tr><td colspan="1" class="confluenceTd"><p>配置发现规则</p></td></tr><tr><td rowspan="7" class="confluenceTd">控制</td><td rowspan="3" class="confluenceTd">工作流</td><td class="confluenceTd"><p>可以查询过滤列出所有默认提供的工作流</p></td></tr><tr><td colspan="1" class="confluenceTd">可以编辑自定义工作流</td></tr><tr><td colspan="1" class="confluenceTd">选择指定的一台或者多台物理机运行某个工作流并且可以跳转查看日志</td></tr><tr><td rowspan="2" class="confluenceTd">任务</td><td class="confluenceTd"><p>可以查询过滤列出所有默认提供的工作流</p></td></tr><tr><td colspan="1" class="confluenceTd">可以编辑自定义工作流</td></tr><tr><td rowspan="2" class="confluenceTd">工作流执行记录</td><td colspan="1" class="confluenceTd"><p>查询过滤列出所有工作流执行记录</p></td></tr><tr><td colspan="1" class="confluenceTd">查看报错日志</td></tr><tr><td rowspan="9" class="confluenceTd">系统设置<br><br><br><br><br></td></tr><tr><td rowspan="2" class="confluenceTd">用户</td><td colspan="1" class="confluenceTd"><p>查询过滤列出所有用户</p></td></tr><tr><td colspan="1" class="confluenceTd">创建编辑用户</td></tr><tr><td rowspan="2" class="confluenceTd">角色</td><td colspan="1" class="confluenceTd"><p>查询过滤列出所有角色</p></td></tr><tr><td colspan="1" class="confluenceTd">创建编辑角色</td></tr><tr><td rowspan="2" class="confluenceTd">Endpoint</td><td colspan="1" class="confluenceTd"><p>列出正在运行的节点</p></td></tr><tr><td colspan="1" class="confluenceTd">添加节点</td></tr><tr><td rowspan="2" class="confluenceTd">全局系统配置</td><td colspan="1" class="confluenceTd"><p>列出所有系统运行参数配置</p></td></tr><tr><td colspan="1" class="confluenceTd">编辑系统配置</td></tr></tbody></table>

<!-- # (详细的版本规划请参考 [版本路线图](https://github.com/metersphere/metersphere/blob/master/ROADMAP.md)-->

## 技术栈

- 后端: [Spring Boot](https://www.tutorialspoint.com/spring_boot/spring_boot_introduction.htm)
- 前端: [Vue.js](https://vuejs.org/)
- 中间件: [MySQL](https://www.mysql.com/)
- 基础设施: [Docker](https://www.docker.com/)

## 致谢

-  [RackHD](https://rackhd.github.io/)：感谢 RackHD 提供的底层实现
-  [MAAS](https://maas.io/)：感谢 MAAS 提供的生命周期纳管思路
-  [Digital Rebar](https://rackn.com/rebar/)：感谢 Digital Rebar 提供的操作方式和UI参考
-  [Element](https://element.eleme.cn/#/)：感谢 Element 提供的优秀组件库

