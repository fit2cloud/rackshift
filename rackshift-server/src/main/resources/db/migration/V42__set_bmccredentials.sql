insert into template
values (uuid(), 'set_bmc_credentials.sh',
        '#!/usr/bin/env bash\n\n# Copyright 2016-2018, DELL EMC, Inc.\n\nchannel=''''\nfunction set_channel()\n\{\n    for i in \{1..15\}; do\n        ipmitool user list \$i &>/dev/null\n        status=\$?\n        if [ \"\$status\" -eq \"0\" ] ; then\n            channel=\$i\n            break\n        fi\n    done\n\}\nset_channel\necho \"channel number is\" \$channel\nif [ -z \"\$\{channel\}\" ]; then\n echo \"Channel number was not set correctly, exiting script\"\nexit 1\nfi\necho \" Getting the user list\"\ncmdReturn=\$(ipmitool user list \$channel)\nmyarray=(\$\{cmdReturn//\$''\\n''/ \})\nmapfile -t userlist < <(ipmitool user list \$channel)\nuserNumber=\$\{#userlist[@]\}\nif [ \"\$userNumber\" -gt  \"1\" ]; then\n   userNumber=\$(expr \$userNumber - 1)\nfi\n\n#The check variable is a flag to determine if the user already exists\n#(1:already exist and 0:user does not exist)\ncheck=0\n#The i variable is an index to determine the userID from the cmdReturn(userList)\ni=0\n#UserID used for adding new user\nnewUserNumber=0\n\nfor x in \$cmdReturn; do\n   if [ <%=user%> == \$x ]; then\n   userNumber=\$\{myarray[\$((\$i-1))]\}\n   echo \"Username already present, overwriting existing user\"\n   ipmitool user set name \$userNumber <%=user%>\n   ipmitool user set password \$userNumber <%-password%>\n   ipmitool channel setaccess \$channel \$userNumber callin=on ipmi=on link=on privilege=4\n   ipmitool user enable \$userNumber\n   check=\$((check + 1))\n  exit\n  fi\n  i=\$((i+1))\ndone\n\nfunction get_newUserNumber()\n\{\n  cmdReturn=\$(sudo ipmitool user summary \$channel)\n  myarray=(\$\{cmdReturn//\$''\\n''/ \})\n  maxCount=\$\{myarray[3]\}\n  if [ \$userNumber -lt \$maxCount ]; then\n    #try to find out the empty user id\n    maxLength=\$\{#userlist[@]\}\n    for ((i=1;i<\$maxLength;i++)) do\n      id=`echo \$\{userlist[i]\} | awk ''\{print \$1\}''`\n      if [ \$id != \$i ]; then\n        newUserNumber=\$i\n        break\n      fi\n    done\n    if [ \$newUserNumber -eq 0 ]; then\n      newUserNumber=\$((userNumber + 1))\n    fi\n  else\n    echo ''reach max user count''\n    exit 1\n  fi\n\}\n\nif [ \$check == 0 ]; then\n echo \"Creating a new user\"\n get_newUserNumber\n ipmitool user set name \$newUserNumber <%=user%>\n ipmitool user set password \$newUserNumber <%-password%>\n ipmitool channel setaccess \$channel \$newUserNumber callin=on ipmi=on link=on privilege=4\n ipmitool user enable \$newUserNumber\nexit\nfi\necho \"Done\"\n',
        'system', 1629698788504, 1629698788504);


insert into profile
values (uuid(), 'install-esx.ipxe',
        '# The progress notification is just something nice-to-have, so progress notification failure should\n# never impact the normal installation process\n<% if( typeof progressMilestones !== \''undefined\'' && progressMilestones.enterProfileUri ) \{ %>\n    # since there is no curl like http client in ipxe, so use imgfetch instead\n    # note: the progress milestones uri must be wrapped in unescaped format, otherwise imgfetch will fail\n    imgfetch --name fakedimage http://<%=server%>:<%=port%><%-progressMilestones.enterProfileUri%> ||\n    imgfree fakedimage ||\n<% \} %>\n\niseq $\{platform\} efi && goto is_efi || goto not_efi\n\n:not_efi\nkernel <%=repo%>/<%=mbootFile%> -c <%=esxBootConfigTemplateUri%> BOOTIF=01-$\{netX/mac\}\ngoto boot_img\n\n:is_efi\nkernel <%=repo%>/efi/boot/bootx64.efi -c <%=esxBootConfigTemplateUri%>\ngoto boot_img\n\n:boot_img\n<% if( typeof progressMilestones !== \''undefined\'' && progressMilestones.startInstallerUri ) \{ %>\n    imgfetch --name fakedimage http://<%=server%>:<%=port%><%-progressMilestones.startInstallerUri%> ||\n    imgfree fakedimage ||\n<% \} %>\n\nboot\n',
        'system', 1629698788504, 1629698788504);

insert into template
values (uuid(), 'esx-boot-cfg',
        'bootstate=0\ntitle=Loading ESXi installer\nprefix=<%=repo%>\nkernel=<%=tbootFile%>\nkernelopt=runweasel formatwithmbr com1_baud=115200 com1_Port=<%=comportaddress%> tty2Port=<%=comport%> gdbPort=<%=gdbPort%> debugLogToSerial=<%=debugLogToSerial%> logPort=<%=logPort%> ks=<%=installScriptUri%>\nmodules=<%=moduleFiles%> <%=kargs%>\nbuild=\nupdated=0\n',
        'system', 1629698788504, 1629698788504);

insert into template
values (uuid(), 'esx.rackhdcallback',
        '#!/bin/sh\n# esx       callback to rackhd post installation API hook\n# description: calls back to rackhd post installation API hook\n\necho \"Attempting to call back to RackHD ESX installer\"\nLOCALSH=/etc/rc.local.d/local.sh\n# *sigh*, busybox shell does not support \{1..30\}. Retry 30 times with 1 second\n# sleep in between.\nfor retry in $(awk \''BEGIN \{ for ( i=0; i<30; i++ ) \{ print i; \} \}\'');\ndo\n    BODY=\"\{\"\n    BODY=$BODY\"\\\"nodeId\\\": \\\"<%=nodeId%>\\\"\"\n    BODY=$BODY\"\}\"\n    BODYLEN=$(echo -n $\{BODY\} | wc -c)\n    echo -ne \"POST /api/current/notification HTTP/1.0\\r\\nHost: <%=server%>\\r\\nContent-Type: application/json\\r\\nContent-Length: $\{BODYLEN\}\\r\\n\\r\\n$\{BODY\}\" | nc -i 3 <%=server%> <%=port%>\n    if [ \"$?\" -ne 0 ];\n    then\n        echo \"Failed to connect to RackHD API callback, retrying\"\n        sleep 1\n    else\n        if [ -e /vmfs/volumes/datastore1/rackhd_callback ]\n        then\n            echo \"Remove RackHD callback script\"\n            rm /vmfs/volumes/datastore1/rackhd_callback\n            echo \"Create RackHD SSH keys for non-root users after second reboot and on startup\"\n            /vmfs/volumes/datastore1/rackhd_create_sshkeys\n            echo \''#!/bin/sh\'' > $LOCALSH\n            cat /vmfs/volumes/datastore1/rackhd_create_sshkeys >> $LOCALSH\n            echo \''exit 0\'' >> $LOCALSH\n            /sbin/auto-backup.sh\n        else\n            touch /vmfs/volumes/datastore1/rackhd_callback\n        fi\n        exit 0\n    fi\ndone;\n\necho \"Exceeded retries connecting to RackHD API callback. Exiting with failure code 1\"\nexit 1\n',
        'system', 1629698788504, 1629698788504);

insert into system_parameter
values ('bmc_credentials', 'true', 'text', null);
insert into system_parameter
values ('bmc_username', 'rackshift', 'text', null);
insert into system_parameter
values ('bmc_password', 'rackshift', 'text', null);