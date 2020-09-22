# RackShift

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/2d7d7a82829e4e4e80c0f2a9aa2397ca)](https://app.codacy.com/manual/rackshift/rackshift?utm_source=github.com&utm_medium=referral&utm_content=rackshift/rackshift&utm_campaign=Badge_Grade_Dashboard)

RackShift is an open source bare metal server full life cycle platform, which covers bare metal (physical machine) shelf, RAID configuration, firmware upgrade, operating system installation, middleware deployment, etc. Based on RackHD, RackShift provides a visual Web UI and supports mainstream server brands such as Inspur, DELL EMC, HPE, Huawei, Lenovo, etc.

![runoob](https://f2c-south.oss-cn-shenzhen.aliyuncs.com/RackHD-dont-del/RackShift/rs8.jpg)

## solved problem
- Challenges faced by equipment identification and discovery in large-scale network environments
- Batch installation, RAID, update firmware
- Bare metal cloudification, self-service, rapid construction of enterprise infrastructure
- For big data, smart data center

## Technical advantage
  
- Full life cycle: can cover the full life cycle management of bare metal from discovery, shelf, deployment to operation and maintenance;
- Automation: Realize workflow automation based on PXE or out-of-band protocol, liberating computer room operation and maintenance personnel;
- Easy operation: Visual operation interface, easy to operate and manage.

## function list

<table class="wrapped confluenceTable"><colgroup><col><col></colgroup><tbody><tr><td class="confluenceTd">Automation</td><td class="confluenceTd"> support PXE / IPMI / SNMP / HTTP automatic discovery and automatic configuration of mainstream brand bare metal servers</td></tr><tr><td class="confluenceTd">Speed</td><td class="confluenceTd"> Unattended installation of Ubuntu, CentOS, Windows and RHEL, only need to restart once after deployment, the entire deployment time is less than 8 minutes</td></tr><tr><td colspan="1" class="confluenceTd"> Information management</td><td colspan="1" class="confluenceTd">Collect hardware device information, CPU, memory, RAID controller, disk, network card, etc., and automatically detect hardware changes</td></tr> <tr><td colspan="1" class="confluenceTd">Network detection</td><td colspan="1" class="confluenceTd">supports automatic detection of hardware devices in the network through multiple protocols</td ></tr><tr><td colspan="1" class="confluenceTd">Storage optimization</td><td colspan="1" class="confluenceTd">User-selectable RAID, Bcache, LVM storage Configuration</td></tr><tr><td colspan="1" class="confluenceTd">DevOps</td><td colspan="1" class="confluenceTd">Integrate Ansible and provide Rest Api completion A series of automated configuration of the server</td></tr><tr><td colspan="1" class="confluenceTd">Various management tools</td><td colspan="1" class="confluenceTd ">Support multiple brands of official management tools such as DELL Racadm, HP SMH, etc.</td></tr><tr><td col span="1" class="confluenceTd">Monitoring</td><td colspan="1" class="confluenceTd"><p>By default, it supports SNMP-based out-of-band hardware health and status monitoring, and users can customize the configuration Prometheus performs OS-level monitoring</p></td></tr><tr><td colspan="1" class="confluenceTd">KVM</td><td colspan="1" class="confluenceTd ">Support mainstream server brand WEB KVM</td></tr><tr><td colspan="1" class="confluenceTd">identity verification</td><td colspan="1" class="confluenceTd" >Integrate LDAP, support OpenID, SAML and other authorization modes</td></tr></tbody></table>

## Technology Architecture
![runoob](https://f2c-south.oss-cn-shenzhen.aliyuncs.com/RackHD-dont-del/RackShift/rs_structure.png)

Component description:

- RackShift-WEB: Single page application based on VUE2.6.11
- RackShift-Server: SSM-based SpringBoot application, which provides a higher abstraction of RackHD operations and controls interaction with RackHD API, controls RackShift-Proxy nodes, and is packaged together with RackShift-WEB into an application deployment
- RackShift-Proxy: It can be deployed separately with the RackHD module. It is mainly used by the master node to control the client node for injection mirroring, DHCP configuration, remote KVM, etc.
- RackHD: DELL EMC's open source bare metal supply software, now no longer maintained
- Mysql: RackShift-Server main operating data storage area
- Mongo: Operating data storage area for RackHD and RackShift-Server
- RabbitMQ: middleware for communication between components
- DockerEngine: All components are run on node computers as Docker containers

## Component call relationship
![runoob](https://f2c-south.oss-cn-shenzhen.aliyuncs.com/RackHD-dont-del/RackShift/rs_call.png)

## Technology Stack

- Frontend: [Vue.js](https://vuejs.org/)
- Backend: [Spring Boot](https://www.tutorialspoint.com/spring_boot/spring_boot_introduction.htm)
- Database: [MySQL](https://www.mysql.com/)

## WeChat Group

TBD

## Thanks

- [RackHD](https://rackhd.github.io/): Thanks to RackHD for providing the underlying implementation;
- [MAAS](https://maas.io/): Thank you for the life cycle management ideas provided by MAAS;
- [Digital Rebar](https://rackn.com/rebar/): Thanks to Digital Rebar for the operation method and UI reference;
- [Element](https://element.eleme.cn/#/): Thanks for the excellent component library provided by Element.