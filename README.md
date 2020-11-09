# RackShift

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/2d7d7a82829e4e4e80c0f2a9aa2397ca)](https://app.codacy.com/manual/rackshift/rackshift?utm_source=github.com&utm_medium=referral&utm_content=rackshift/rackshift&utm_campaign=Badge_Grade_Dashboard)

RackShift 是一款开源的裸金属服务器全生命周期管理平台，功能覆盖裸金属（物理机）的上架、RAID 配置、固件升级、操作系统安装、中间件部署等。RackShift 基于 RackHD 实现，提供可视化的 Web UI，支持世面上主流服务器品牌如浪潮、戴尔、华为、联想、惠普等。

![runoob](https://f2c-south.oss-cn-shenzhen.aliyuncs.com/RackHD-dont-del/RackShift/rs8.jpg)

## 解决的问题
- 大规模网络环境下裸金属设备的自动发现；
- 批量装机、RAID配置和固件更新；
- 裸金属云化、自服务化，快速构建企业的基础设施。

## 技术优势
  
- 全生命周期: 能够覆盖裸金属从发现、上架、部署到运维的全生命周期管理；
- 自动化：基于 PXE 或者带外协议实现工作流自动化，解放机房运维人员；
- 易操作: 可视化操作界面，易于操作和管理。

## 功能列表

<table class="wrapped confluenceTable"><colgroup><col><col></colgroup><tbody><tr><td class="confluenceTd">自动化</td><td class="confluenceTd">支持主流品牌裸金属服务器的 PXE / IPMI / SNMP / HTTP 自动发现与自动配置</td></tr><tr><td class="confluenceTd">速度</td><td class="confluenceTd">无人值守安装 Ubuntu， CentOS, Windows 和 RHEL，部署完毕只需要重启一次，整个部署时间不超过8分钟</td></tr><tr><td colspan="1" class="confluenceTd">信息纳管</td><td colspan="1" class="confluenceTd">收集硬件设备信息， CPU， 内存， RAID 控制器， 磁盘， 网卡等，自动检测硬件变更</td></tr><tr><td colspan="1" class="confluenceTd">网络探测</td><td colspan="1" class="confluenceTd">支持通过多种协议自动探测网络中的硬件设备</td></tr><tr><td colspan="1" class="confluenceTd">存储优化</td><td colspan="1" class="confluenceTd">用户可选的 RAID ， Bcache ，LVM 存储配置</td></tr><tr><td colspan="1" class="confluenceTd">多样的管理工具</td><td colspan="1" class="confluenceTd">支持多种品牌的官方管理工具如 DELL Racadm，HP SMH 等等</td></tr></tbody></table>

详细的版本规划请参考 [版本路线图](https://github.com/rackshift/rackshift/blob/master/ROADMAP.md)

## 技术架构
![runoob](https://f2c-south.oss-cn-shenzhen.aliyuncs.com/RackHD-dont-del/RackShift/rackshift-struc.jpg)

组件说明：

- RackShift-Web： 基于 VUE2.6.11 开发的单页应用；
- RackShift-Server： 基于 SSM 的 SpringBoot 应用，对 RackHD 的操作进行更高的抽象并且控制与 RackHD API的交互，控制 RackShift-Proxy 节点，与 RackShift-WEB 一并打包成一个应用部署；
- RackShift-Proxy： 可单独与 RackHD 模块部署，主要用于主节点控制客户节点进行注入镜像下发，DHCP 配置，远程 KVM 等等；
- RackHD： DELL EMC 开源的裸金属供应软件，现已停止维护；
- Mysql：RackShift-Server 主要运行数据的存储区；
- Mongo：RackHD 与 RackShift-Server 的运行数据存储区；
- RabbitMQ: 各组件之间通信的中间件；
- DockerEngine：各组件都是以 Docker 容器运行在节点计算机。

## 组件调用关系
![runoob](https://f2c-south.oss-cn-shenzhen.aliyuncs.com/RackHD-dont-del/RackShift/rs-call.jpg)

## 技术栈

- 前端: [Vue.js](https://vuejs.org/)
- 后端: [Spring Boot](https://www.tutorialspoint.com/spring_boot/spring_boot_introduction.htm)s
- 数据库: [MySQL](https://www.mysql.com/)

## 快速开始

- 下载最新 Release 安装包，并且使用 tar -zxvf 解压进入解压后的 installer 目录
- ./install.sh

## 帮助文档
- [帮助文档](https://rackshift.github.io/rackshift-docs-static/)

## 微信群

<img src="https://f2c-south.oss-cn-shenzhen.aliyuncs.com/RackHD-dont-del/RackShift/wechat2.jpg" width="300" height="300" align="middle" />

## 致谢

-  [RackHD](https://rackhd.github.io/)：感谢 RackHD 提供的底层实现；
-  [MAAS](https://maas.io/)：感谢 MAAS 提供的生命周期纳管思路；
-  [Digital Rebar](https://rackn.com/rebar/)：感谢 Digital Rebar 提供的操作方式和 UI 参考；
-  [Element](https://element.eleme.cn/#/)：感谢 Element 提供的优秀组件库。
