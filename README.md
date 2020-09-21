# RackShift

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/2d7d7a82829e4e4e80c0f2a9aa2397ca)](https://app.codacy.com/manual/rackshift/rackshift?utm_source=github.com&utm_medium=referral&utm_content=rackshift/rackshift&utm_campaign=Badge_Grade_Dashboard)

RackShift 是一款开源的裸金属服务器全生命周期平台，功能覆盖裸金属（物理机）的上架、RAID 配置、固件升级、操作系统安装、中间件部署等。RackShift 基于 RackHD 实现，提供可视化的 Web UI，支持世面上主流服务器品牌如浪潮、DELL EMC、HPE、华为、联想等。

![runoob](https://f2c-south.oss-cn-shenzhen.aliyuncs.com/RackHD-dont-del/RackShift/rs8.jpg)

## 技术优势
  
- 全生命周期: 能够覆盖裸金属从发现、上架、部署到运维的全生命周期管理；
- 自动化：基于 PXE 或者带外协议实现工作流自动化，解放机房运维人员；
- 易操作: 可视化操作界面，易于操作和管理。

## 功能列表

<table class="confluenceTable"><colgroup><col style="width: 77.0px;"><col style="width: 105.0px;"><col style="width: 329.0px;"></colgroup><tbody><tr><td rowspan="14" class="confluenceTd">资源管理<br><br><br><br><br></td><td rowspan="8" class="confluenceTd">裸金属服务器<br><br><br><br><br></td><td class="confluenceTd">支持多种主流品牌的主动/被动发现</td></tr><tr><td class="confluenceTd">支持服务器全生命周期状态的管理</td></tr><tr><td colspan="1" class="confluenceTd">可批量装机/RAID</td></tr><tr><td class="confluenceTd">可同时运行多个工作流对多台服务器进行批量部署</td></tr><tr><td colspan="1" class="confluenceTd">裸金属支持自动选择合适的节点进行部署</td></tr><tr><td class="confluenceTd">可通过IPMI/SNMP/HTTP等协议进行远程控制</td></tr><tr><td class="confluenceTd">部署日志的查看和管理</td></tr><tr><td class="confluenceTd">可通过集成的WEB KVM远程管理裸金属服务器</td></tr><tr><td rowspan="3" class="confluenceTd">镜像</td><td colspan="1" class="confluenceTd">可在线上传并部署镜像</td></tr><tr><td colspan="1" class="confluenceTd">可对镜像进行在线实时管理</td></tr><tr><td colspan="1" class="confluenceTd">支持多节点高可用</td></tr><tr><td rowspan="3" class="confluenceTd">网络</td><td colspan="1" class="confluenceTd">DHCP地址池以及PXE配置</td></tr><tr><td colspan="1" class="confluenceTd">可查看DHCP服务分配日志</td></tr><tr><td colspan="1" class="confluenceTd">支持同时管理多个节点网段的DHCP服务</td></tr><tr><td rowspan="2" class="confluenceTd">控制</td><td class="confluenceTd">工作流</td><td colspan="1" class="confluenceTd">可自定义工作流</td></tr><tr><td colspan="1" class="confluenceTd">部署日志</td><td colspan="1" class="confluenceTd">在线管理</td></tr><tr><td rowspan="2" class="confluenceTd">系统</td><td colspan="1" class="confluenceTd">节点管理</td><td colspan="1" class="confluenceTd">支持实时管理节点和节点状态</td></tr><tr><td colspan="1" class="confluenceTd">用户管理</td><td colspan="1" class="confluenceTd">支持RBAC</td></tr></tbody></table>

## 技术架构
![runoob](https://f2c-south.oss-cn-shenzhen.aliyuncs.com/RackHD-dont-del/RackShift/rs_structure.png)

组件说明：

- RackShift-WEB： 基于vue2.6.11开发的单页应用
- RackShift-Server： 基于SSM的SpringBoot应用，对RackHD的操作进行更高的抽象并且控制与RackHD API的交互，控制RackShift-Proxy节点，与RackShift-WEB一并打包成一个应用部署
- RackShift-Proxy： 可单独与RackHD模块部署，主要用于主节点控制客户节点进行注入镜像下发，DHCP配置，远程KVM等等
- RackHD： DELL EMC开源的裸金属供应软件，现已停止维护
- Mysql：RackShift-Server主要运行数据的存储区
- Mongo：RackHD与RackShift-Server的运行数据存储区
- RabbitMQ: 各组件之间通信的中间件
- DockerEngine：各组件都是以Docker容器运行在节点计算机

## 组件调用关系
![runoob](https://f2c-south.oss-cn-shenzhen.aliyuncs.com/RackHD-dont-del/RackShift/rs_call.png)

## 技术栈

- 前端: [Vue.js](https://vuejs.org/)
- 后端: [Spring Boot](https://www.tutorialspoint.com/spring_boot/spring_boot_introduction.htm)
- 数据库: [MySQL](https://www.mysql.com/)

## 微信群

TBD

## 致谢

-  [RackHD](https://rackhd.github.io/)：感谢 RackHD 提供的底层实现；
-  [MAAS](https://maas.io/)：感谢 MAAS 提供的生命周期纳管思路；
-  [Digital Rebar](https://rackn.com/rebar/)：感谢 Digital Rebar 提供的操作方式和 UI 参考；
-  [Element](https://element.eleme.cn/#/)：感谢 Element 提供的优秀组件库。
