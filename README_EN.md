# RackShift

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/2d7d7a82829e4e4e80c0f2a9aa2397ca)](https://app.codacy.com/manual/rackshift/rackshift?utm_source=github.com&utm_medium=referral&utm_content=rackshift/rackshift&utm_campaign=Badge_Grade_Dashboard)

> [English](README_EN.md) | 中文

RackShift is a RackHD-based open source management platform for the full life cycle of bare metal servers, including bare metal shelves, RAID configuration, firmware upgrades, OS installation, deployment middleware, WEB Kvm and other bare metal server operation and maintenance personnel. Features. Provides a visual and convenient operation interface, and is fully compatible with mainstream server manufacturers in the world, such as DELL, HP, Inspur, Huawei, Lenovo, IBM, etc.

## Features
- RBAC authority management
- Mainstream brand bare metal server information management and life cycle control
- Customized RAID for mainstream brand bare metal servers
- Mainstream brand bare metal server WEB Kvm
- Mainstream brand bare metal server Linux/Windows installation workflow
- Mainstream brand bare metal server out-of-band management
- Customize Workflow
- Multi Endpoint, PXE DHCP, TFTP management

## UI
 
![runoob](https://f2c-south.oss-cn-shenzhen.aliyuncs.com/RackHD-dont-del/RackHD%E4%B8%80%E9%94%AE%E5%8C%85/3.0/rs1.png)

## Technical advantage
     
- Full life cycle: It can cover the full life cycle management and control of bare metal from discovery, shelving, deployment, operation and maintenance;
- Automation: All workflows are based on PXE or out-of-band protocol to achieve automation, which truly liberates computer room operation and maintenance personnel
- Easy to operate: The operation is friendly, the open source bare metal installation platform is not intuitive enough to solve the problem, and the pain points to be solved are not known

## Function list

<table class="wrapped confluenceTable"><colgroup><col><col><col></colgroup><tbody><tr><th class="confluenceTh">Top menu</th><th class=" confluenceTh">Secondary menu</th><th class="confluenceTh">Use case</th></tr><tr><td rowspan="2" class="confluenceTd">Overview</td><td rowspan="2" class="confluenceTd">Using the wizard</td><td class="confluenceTd"><p>The configuration required for the first installation</p></td></tr><tr> <td colspan="1" class="confluenceTd">Help documentation</td></tr><tr><td rowspan="15" class="confluenceTd">Resources<br><br></td> <td rowspan="11" class="confluenceTd">Bare metal</td><td class="confluenceTd"><p>Query and filter list all discovered bare metal servers</p></td>< /tr><tr><td colspan="1" class="confluenceTd">Switch and restart PXE</td></tr><tr><td colspan="1" class="confluenceTd">Tag editing< /td></tr><tr><td colspan="1" class="confluenceTd">Edit IPMI account</td></tr><tr><td colspan="1" class="confluenceTd"> VNC/WEB KVM connection</td></tr><tr><td colspan="1" class="confluenceTd">View Catalogs</td></tr><tr><td colspan="1" class ="confluenceTd">Check CPU, memory, disk, network card, motherboard and other information, BIOS, network card BIOS version and other information</td></tr><tr><td colspan="1" class="confluenceTd">Re Do R aid Reinstall the system</td></tr><tr><td colspan="1" class="confluenceTd">Add/Delete</td></tr><tr><td colspan="1" class ="confluenceTd">Execute workflow, customize parameter configuration</td></tr><tr><td colspan="1" class="confluenceTd">Full life cycle management, multiple machines in parallel, multiple machines on the same machine Task serial execution workflow</td></tr><tr><td rowspan="4" class="confluenceTd">Mirror</td><td class="confluenceTd"><p>Query and filter columns Export all mirrors</p></td></tr><tr><td colspan="1" class="confluenceTd">Upload mirrors</td></tr><tr><td colspan="1 "class="confluenceTd">Use local mirror</td></tr><tr><td colspan="1" class="confluenceTd">Delete mirror</td></tr><tr><td rowspan ="6" class="confluenceTd">Network</td><td rowspan="4" class="confluenceTd">Subnet</td><td class="confluenceTd"><p>Configure network segment information ( startIp, endIp, netmask, dns, gateway, vlan)</p></td></tr><tr><td colspan="1" class="confluenceTd">Configure the Endpoint DHCP service</td></ tr><tr><td colspan="1" class="confluenceTd">View all Endpoint subnets</td></tr><tr><td colspan="1" class="confluenceTd">Edit subnets , Check the DHCP IP allocation log</td></tr><tr><td rowspan="2" class="confluenceTd">found</td><td class="confluenceTd"><p>list all Discovery rules</p></td></tr><tr><td colspan="1" class ="confluenceTd"><p>Configure discovery rules</p></td></tr><tr><td rowspan="6" class="confluenceTd">Control</td><td rowspan="3 "class="confluenceTd">Workflow</td><td class="confluenceTd"><p>You can query and filter to list all the workflows provided by default</p></td></tr><tr> <td colspan="1" class="confluenceTd">Can edit custom workflow</td></tr><tr><td colspan="1" class="confluenceTd">Select one or more specified A physical machine runs a certain workflow and can jump to view the log</td></tr><tr><td rowspan="2" class="confluenceTd">task</td><td class="confluenceTd" ><p>You can query and filter to list all the workflows provided by default</p></td></tr><tr><td colspan="1" class="confluenceTd">You can edit custom workflows< /td></tr><tr><td colspan="1" class="confluenceTd">Workflow execution record</td><td colspan="1" class="confluenceTd"><p>1. Query Filter and list all workflow execution records</p><p>2. View error log</p></td></tr><tr><td rowspan="8" class="confluenceTd">System settings <br><br><br><br><br></td></tr><tr><td class="confluenceTd">User</td><td colspan="1" class="confluenceTd "><p>1. Query and filter list all users</p><p>2. Create and edit users</p></td></tr><tr><td rowspan="2" class=" confluenceTd">role</td><td colspan="1" class="confluenceTd"><p>Query filter to list all roles</p></td></tr><tr><td colspan=" 1" class="confluenceTd">Create an editor role</td>< /tr><tr><td rowspan="2" class="confluenceTd">Endpoint</td><td colspan="1" class="confluenceTd"><p>List the running nodes</p> </td></tr><tr><td colspan="1" class="confluenceTd">Add node</td></tr><tr><td rowspan="2" class="confluenceTd"> Global system configuration</td><td colspan="1" class="confluenceTd"><p>List all system operating parameter configurations</p></td></tr><tr><td colspan=" 1" class="confluenceTd">Edit system configuration</td></tr></tbody></table>

<!-- # (详细的版本规划请参考 [版本路线图](https://github.com/metersphere/metersphere/blob/master/ROADMAP.md)-->

## Technology stack

- Back End: [Spring Boot](https://www.tutorialspoint.com/spring_boot/spring_boot_introduction.htm)
- Front End: [Vue.js](https://vuejs.org/)
- Midddleware: [MySQL](https://www.mysql.com/)
- Infrastructure: [Docker](https://www.docker.com/)

## Thanks

-  [RackHD](https://rackhd.github.io/)：Thanks to the underlying implementation provided by RackHD
-  [MAAS](https://maas.io/)：Thanks to MAAS for providing life cycle management ideas
-  [Digital Rebar](https://rackn.com/rebar/)：Thanks for the operation method and UI reference provided by Digital Rebar
-  [Element](https://element.eleme.cn/#/)：Thanks to Element for the excellent component library

