<template>
  <div>
    <el-form label-width="160px" :rules="rules" :model="payLoad.options.defaults" ref="form" label-position="right">
      <el-row>
        <el-col :span="11">
          <el-form-item :label="$t('hostname')" prop="hostname">
            <el-input v-model="payLoad.options.defaults.hostname" autocomplete="off" aria-required="true"
                      maxlength="20"></el-input>
          </el-form-item>
          <el-form-item :label="$t('root_pwd')" prop="rootPassword">
            <el-input v-model="payLoad.options.defaults.rootPassword" autocomplete="off"
                      show-password maxlength="20"></el-input>
          </el-form-item>
          <el-form-item :label="$t('image')" prop="repo">
            <el-select v-model="payLoad.options.defaults.repo" class="input-element" filterable
                       allow-create
                       default-first-option @change="changePT">
              <el-option v-for="g in allImages" :label="g.name"
                         :value="g.url"></el-option>
            </el-select>
          </el-form-item>

          <el-form-item :label="$t('profile')" prop="repo" v-if="payLoad.options.defaults.profile">
            <el-link target="_blank" @click="showContent('profile')">{{ payLoad.options.defaults.profile }}</el-link>
          </el-form-item>

          <el-form-item :label="$t('InstallScript')" prop="repo" v-if=" payLoad.options.defaults.installScript">
            <el-link target="_blank" @click="showContent('template')">{{ payLoad.options.defaults.installScript }}
            </el-link>
          </el-form-item>

          <el-form-item :label="$t('custom_partition')">
            <el-switch
                v-model="extraParams.customPartition" @change="changePartition">
            </el-switch>
            <el-tooltip class="dark" :content="partitionTips">
              <i class="el-icon-question" style="font-size: 20px; "></i>
            </el-tooltip>
            <table class="detail-info-rack" v-show="extraParams.customPartition">
              <thead>
              <tr>
                <th>{{ $t('mount_point') }}</th>
                <th style="width:100px;">
                  <el-col :span="8">{{ $t('capacity') }}</el-col>
                  <el-col :span="16">
                    <el-select v-model="extraParams.unit">
                      <el-option :value="unit" :key="unit" v-for="unit in  units"></el-option>
                    </el-select>
                  </el-col>
                </th>
                <th>{{ $t('fs_type') }}</th>
                <th>{{ $t('device_type') }}</th>
                <th>{{ $t('volume_group') }}</th>
                <th style="width:80px;">{{ $t('lvm_name') }}</th>
                <th>
                  <el-button type="primary" icon="el-icon-plus" circle
                             @click="payLoad.options.defaults.installPartitions.push({})">
                  </el-button>
                </th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="(partition, index) in payLoad.options.defaults.installPartitions"
                  v-show="partition.mountPoint != 'biosboot' && partition.mountPoint != '/boot/efi'">
                <td>
                  <el-input v-model="partition.mountPoint"
                            :disabled="partition.mountPoint == 'biosboot'"></el-input>
                </td>
                <td>
                  <el-input v-model="partition.size" :disabled="partition.mountPoint == 'biosboot'"
                            :title="partition.mountPoint == 'biosboot' ? 'MB' : extraParams.unit"></el-input>
                </td>
                <td>
                  <el-select v-model="partition.fsType" :disabled="partition.mountPoint == 'biosboot'">
                    <el-option v-for="x in fsType"
                               :value="x"><span>{{ x }}</span>
                    </el-option>
                  </el-select>
                </td>
                <td>
                  <el-select v-model="partition.deviceType" :disabled="partition.mountPoint == 'biosboot'">
                    <el-option v-for="x in deviceType"
                               :value="x.value"><span>{{ x.value }}</span>
                    </el-option>
                  </el-select>
                </td>
                <td>
                  <el-select v-model="partition.volumeGroup"
                             :disabled="partition.mountPoint == 'biosboot' || partition.deviceType == 'standard'">
                    <el-option v-for="x in volumeGroup"
                               :value="x"><span>{{ x }}</span>
                    </el-option>
                  </el-select>
                </td>
                <td>
                  <el-input v-model="partition.lvmName"
                            :disabled="partition.mountPoint == 'biosboot' || partition.deviceType == 'standard'"></el-input>
                </td>
                <td>
                  <el-button type="primary" icon="el-icon-minus" circle
                             @click="payLoad.options.defaults.installPartitions.splice(index, 1)">
                  </el-button>
                </td>
              </tr>
              </tbody>
            </table>
          </el-form-item>

          <el-form-item :label="$t('uefi_boot')">
            <el-switch v-model="extraParams.uefi" @change="changeUefiBoot"></el-switch>
          </el-form-item>

          <el-form-item :label="$t('post_install')">
            <RSCodeMirror v-model="payLoad.options.defaults.postInstallCommands"
                          @receiveValue="receiveValue"></RSCodeMirror>
          </el-form-item>
        </el-col>
        <el-col :span="13">
          <el-form-item :label="$t('network_card')">
            <RSButton @click="addNet" type="add" :tip="$t('add_network_card')"></RSButton>

            <el-collapse v-model="activeNames">
              <el-collapse-item v-for="(d, $index) in payLoad.options.defaults.networkDevices"
                                :title="getNetworkName(d)" :name="$index + ''">
                <RSButton @click="delNet($index)" type="del" :tip="$t('del_network_card')"></RSButton>
                <el-form :model="d" :rules="nicRules"
                         ref="nicForm" label-position="right" label-width="185px">

                  <el-form-item prop="device" :label="$t('pls_select_') + $t('network_card')">
                    <el-select v-model="d.device" class="input-element">
                      <el-option :value="n.mac" v-for="n in nics">
                  <span>
                    {{
                      n.number + '(' + n.mac + ')'
                    }}
                    <span style="color:red" v-if="n.pxe">{{ ' ' + $t('is_pxe') }}</span>
                  </span>

                      </el-option>
                    </el-select>
                  </el-form-item>

                  <el-form :model="d.ipv4" :rules="nicRules" ref="nic2Form" label-position="right" label-width="185px">
                    <el-form-item prop="ipAddr" :label="$t('ip_addr')">
                      <el-input v-model="d.ipv4.ipAddr"></el-input>
                    </el-form-item>

                    <el-form-item prop="gateway" :label="$t('gateway')">
                      <el-input v-model="d.ipv4.gateway"></el-input>
                    </el-form-item>

                    <el-form-item prop="netmask" :label="$t('netmask')">
                      <el-input v-model="d.ipv4.netmask"></el-input>
                    </el-form-item>

                    <el-form-item prop="vlanIds" :label="$t('vlan')">
                      <el-select
                          v-model="d.ipv4.vlanIds"
                          multiple
                          filterable
                          allow-create
                          @change="changeInt(d)"
                          default-first-option
                          :placeholder="$t('pls_input_vlan')">
                      </el-select>
                    </el-form-item>
                  </el-form>
                </el-form>
              </el-collapse-item>
            </el-collapse>
          </el-form-item>

          <el-form-item :label="$t('BOND4(802.3ad)')">
            <RSButton @click="addBond" type="add" :tip="$t('add_bond')"></RSButton>

            <el-collapse v-model="bondActiveNames">
              <el-collapse-item v-for="(d, $index) in payLoad.options.defaults.bonds"
                                :title="d.name" :name="'bond' + $index + ''">
                <RSButton @click="delBond($index)" type="del" :tip="$t('del_bond')"></RSButton>
                <el-form :model="d" :rules="nicRules"
                         ref="nicForm" label-position="right" label-width="185px">

                  <el-form-item prop="name" :label="$t('deviceName')">
                    <el-input v-model="d.name" class="input-element">
                    </el-input>
                  </el-form-item>

                  <el-form-item prop="nics" :label="$t('pls_select_') + $t('network_card')">
                    <el-select v-model="d.nics" class="input-element" multiple>
                      <el-option :value="n.mac" v-for="n in nics">
                  <span>
                    {{
                      n.number + '(' + n.mac + ')'
                    }}
                    <span style="color:red" v-if="n.pxe">{{ ' ' + $t('is_pxe') }}</span>
                  </span>

                      </el-option>
                    </el-select>
                  </el-form-item>

                  <el-form :model="d.ipv4" :rules="nicRules" ref="nic2Form" label-position="right" label-width="185px">
                    <el-form-item prop="ipAddr" :label="$t('ip_addr')">
                      <el-input v-model="d.ipv4.ipAddr"></el-input>
                    </el-form-item>

                    <el-form-item prop="gateway" :label="$t('gateway')">
                      <el-input v-model="d.ipv4.gateway"></el-input>
                    </el-form-item>

                    <el-form-item prop="netmask" :label="$t('netmask')">
                      <el-input v-model="d.ipv4.netmask"></el-input>
                    </el-form-item>
                  </el-form>

                  <el-form-item prop="bondvlaninterfaces" :label="$t('vlan')">
                    <RSButton @click="addBondVlan(d)" type="add" :tip="$t('add_vlan')"></RSButton>
                    <el-collapse-item v-for="(db, $index) in d.bondvlaninterfaces">
                      <RSButton @click="delBondVlan(d.bondvlaninterfaces, $index)" type="del"
                                :tip="$t('del_bond')"></RSButton>
                      <el-form :model="db" :rules="nicRules"
                               ref="nicForm" label-position="right" label-width="185px">

                        <el-form :model="db.ipv4" :rules="nicRules" ref="nic2Form" label-position="right"
                                 label-width="185px">
                          <el-form-item prop="ipAddr" :label="$t('ip_addr')">
                            <el-input v-model="db.ipv4.ipAddr"></el-input>
                          </el-form-item>

                          <el-form-item prop="gateway" :label="$t('gateway')">
                            <el-input v-model="db.ipv4.gateway"></el-input>
                          </el-form-item>

                          <el-form-item prop="netmask" :label="$t('netmask')">
                            <el-input v-model="db.ipv4.netmask"></el-input>
                          </el-form-item>
                        </el-form>

                        <el-form-item prop="vlanid" :label="$t('vlan')">
                          <el-input type="number" v-model="db.vlanid" @change="changeVlanId(db)"></el-input>
                        </el-form-item>

                      </el-form>
                    </el-collapse-item>
                  </el-form-item>

                </el-form>
              </el-collapse-item>
            </el-collapse>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">

        </el-col>
        <el-col :span="12"></el-col>
      </el-row>

    </el-form>

    <el-dialog
        :title="tip"
        :visible.sync="dialogVisible"
        :append-to-body="true"
        width="50%">
      <el-input type="textarea" rows="25" cols="150" v-model="content"></el-input>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="updateScript()">{{ $t("confirm") }}</el-button>
      </span>
    </el-dialog>
    <!--    </el-card>-->
  </div>
</template>
<script>
import HttpUtil from "../common/utils/HttpUtil";
import {isAnyBlank} from "@/common/utils/CommonUtil";
import {
  hostnameValidator,
  repoSelectValidator,
  ipValidator,
  requiredSelectValidator,
  requiredValidator,
  vlanValidator
} from "@/common/validator/CommonValidator";
import RSCodeMirror from "@/common/script/RSCodeMirror";

let _ = require('lodash');
export default {
  activated() {
  },
  created() {

  },
  deactivated() {

  },
  components: {
    RSCodeMirror
  },
  data() {
    return {
      tip: null,
      dialogVisible: false,
      content: null,
      scriptType: null,
      options: {
        tabSize: 4,
        styleActiveLine: true,
        lineNumbers: true,
        autoCloseTags: true,
        line: true,
        mode: 'text/html',
        theme: 'ambiance'
      },
      partitionTips: this.$t("partition_tips"),
      activeNames: '0',
      bondActiveNames: 'bond0',
      rules: {
        hostname: [
          {validator: hostnameValidator, trigger: 'blur', vue: this},
        ],
        rootPassword: [
          {validator: requiredValidator, trigger: 'blur', vue: this},
        ],
        repo: [
          {validator: repoSelectValidator, trigger: 'blur', vue: this, name: 'image'},
        ]
      },
      nicRules: {
        device: [
          {validator: requiredSelectValidator, trigger: 'blur', vue: this, name: 'device'},
        ],
        ipAddr: [
          {validator: ipValidator, trigger: 'blur', vue: this},
        ],
        netmask: [
          {validator: ipValidator, trigger: 'blur', vue: this},
        ],
        gateway: [
          {validator: ipValidator, trigger: 'blur', vue: this},
        ],
        vlanIds: [
          {validator: vlanValidator, trigger: 'change', vue: this},
        ],
        vlanid: [
          {validator: requiredValidator, trigger: 'change', vue: this},
        ],
      },
      uefi: false,
      defaultPayLoad: {
        "options": {
          "defaults": {
            "version": "7",
            "repo": null,
            "rootPassword": "RackShift",
            "hostname": "rackshift-node",
            "networkDevices": [
              {
                "device": null,
                "ipv4": {
                  "ipAddr": "192.168.1.10",
                  "gateway": "192.168.1.1",
                  "netmask": "255.255.255.0"
                }
              }
            ],
            "installDisk": "/dev/sda",
            "installPartitions": [
              {
                "mountPoint": "/",
                "size": "auto",
                "fsType": "ext3",
                "deviceType": "standard"
              },
              {
                "mountPoint": "swap",
                "size": "4096",
                "fsType": "swap",
                "deviceType": "standard"
              },
              {
                "mountPoint": "/boot",
                "size": "4096",
                "fsType": "ext3",
                "deviceType": "standard"
              },
              {
                "mountPoint": "biosboot",
                "size": "1",
                "fsType": "biosboot",
                "deviceType": "standard"
              }
            ],
            "bonds": []
          }
        }
      },
      payLoad: this.$attrs.params,
      extraParams: this.$attrs.extraParams ? JSON.parse(JSON.stringify(this.$attrs.extraParams)) : {
        unit: 'MB'
      },
      allImages: [],
      cpus: [],
      memories: [],
      disks: [],
      nics: [],
      units: [
        'GB', 'MB'
      ],
      fsType: ['ext3', 'ext4', 'swap', 'xfs', 'biosboot'],
      deviceType: [{"name": this.$t("lvm"), "value": "lvm"}, {
        "name": this.$t("standard_partition"),
        "value": "standard"
      }],
      volumeGroup: ["rootvg"],
      validateResult: false,
    };
  },
  mounted() {
    this.getAllImage();
    this.getHardware();
    this.$watch("extraParams.unit", function (newV, oldV) {
      if (oldV == 'GB' && newV == 'MB') {
        for (var i = 0; i < this.payLoad.options.defaults.installPartitions.length && this.payLoad.options.defaults.installPartitions[i].mountPoint != 'biosboot'; i++) {
          if (/\d+/.test(this.payLoad.options.defaults.installPartitions[i].size))
            this.payLoad.options.defaults.installPartitions[i].size = this.payLoad.options.defaults.installPartitions[i].size * 1024 + "";
        }
      } else if (oldV == 'MB' && newV == 'GB') {
        for (var i = 0; i < this.payLoad.options.defaults.installPartitions.length && this.payLoad.options.defaults.installPartitions[i].mountPoint != 'biosboot'; i++) {
          if (/\d+/.test(this.payLoad.options.defaults.installPartitions[i].size))
            this.payLoad.options.defaults.installPartitions[i].size = parseInt((this.payLoad.options.defaults.installPartitions[i].size / 1024)) + "";
        }
      }
      this.extraParams.unit = newV;
      this.changePartition();
    })
  }
  ,
  methods: {
    changePartition() {
      if (!this.extraParams.customPartition) {
        delete this.payLoad.options.defaults.installPartitions;
      } else {
        this.payLoad.options.defaults.installPartitions = [
          {
            "mountPoint": "/",
            "size": "auto",
            "fsType": "ext3",
            "deviceType": "standard"
          },
          {
            "mountPoint": "swap",
            "size": "4096",
            "fsType": "swap",
            "deviceType": "standard"
          },
          {
            "mountPoint": "/boot",
            "size": "4096",
            "fsType": "ext3",
            "deviceType": "standard"
          },
          {
            "mountPoint": "biosboot",
            "size": "1",
            "fsType": "biosboot",
            "deviceType": "standard"
          }
        ];
      }
    },
    updateScript() {
      let image = _.find(this.allImages, i => i.url == this.payLoad.options.defaults.repo);
      if (image == null) return;
      let req = {};
      let url = null;
      if (this.scriptType == 'profile') {
        req = {
          "id": image.pid,
          "name": image.pName,
          "content": this.content,
        };
        url = "/profile/update";
      } else {

        req = {
          "id": image.tid,
          "name": image.tName,
          "content": this.content,
        };
        url = "/template/update";
      }
      let that = this;
      HttpUtil.put(url, req, (res) => {
        if (res.data) {
          that.getAllImage();
        } else {
          this.$message.error(this.$t('opt_fail'));
        }
      });
      this.dialogVisible = false;
    },
    showContent(type) {
      this.scriptType = type;
      let image = _.find(this.allImages, i => i.url == this.payLoad.options.defaults.repo);
      if (type == 'profile') {
        this.tip = this.payLoad.options.defaults.profile;
        this.content = image.pContent;
      } else {
        this.tip = this.payLoad.options.defaults.installScript;
        this.content = image.tContent;
      }
      this.dialogVisible = true;
    },
    changePT(url) {
      let centosImage = _.find(this.allImages, i => i.url == url);
      let customPXE = false;
      if (centosImage.pName) {
        this.payLoad.options.defaults.profile = centosImage.pName;
        customPXE = true;
      } else {
        delete this.payLoad.options.defaults.profile;
      }
      if (centosImage.tName) {
        this.payLoad.options.defaults.installScript = centosImage.tName;
        customPXE = true;
      } else {
        delete this.payLoad.options.defaults.installScript;
      }
      if (customPXE)
        this.$message.info(this.$t("not_support_validate"));
      this.payLoad.options.defaults.repo = url;
    },
    receiveValue(val) {
      this.payLoad.options.defaults.postInstallCommands = val;
    },
    addBond() {
      if (!this.payLoad.options.defaults.bonds)
        this.$set(this.payLoad.options.defaults, "bonds", []);
      this.payLoad.options.defaults.bonds.push({
        "name": null,
        "nics": [],
        "ipv4": {},
        "bondvlaninterfaces": []
      });
    },
    addBondVlan(bond) {
      bond.bondvlaninterfaces.push({
        "vlanid": null,
        "ipv4": {
          "ipAddr": null,
          "gateway": null,
          "netmask": null,
        }
      });
    },
    delBond(index) {
      this.payLoad.options.defaults.bonds.splice(index, 1);
    },
    delBondVlan(db, index) {
      db.splice(index, 1);
    },
    changeInt(arr) {
      let a = [];
      arr.ipv4.vlanIds.forEach(v => a.push(Number(v)));
      arr.ipv4.vlanIds = a;
    },
    changeVlanId(db) {
      db.vlanid = Number(db.vlanid);
    },
    getNetworkName(d) {
      return !d.device ? this.$t("network_card_mac") : this.$t("network_card_mac") + " " + d.device;
    },
    delNet(index) {
      if (this.payLoad.options.defaults.bonds && this.payLoad.options.defaults.bonds.length > 0) {
        this.payLoad.options.defaults.networkDevices.splice(index, 1);
      } else {
        if (this.payLoad.options.defaults.networkDevices.length - 1 > 0) {
          this.payLoad.options.defaults.networkDevices.splice(index, 1);
        }
      }
    },
    addNet() {
      if (this.nics.length == 0) {
        this.$message.warning(this.$t("nic_not_found"));
        return;
      }
      if (this.payLoad.options.defaults.networkDevices && this.payLoad.options.defaults.networkDevices.length == this.nics.length) {
        this.$message.warning(this.$t("cannot_add_more"));
        return;
      }
      this.payLoad.options.defaults.networkDevices.push({
        "device": null,
        "ipv4": {
          "ipAddr": "192.168.1.10",
          "gateway": "192.168.1.1",
          "netmask": "255.255.255.0"
        }
      })
    },
    changeUefiBoot: function () {
      if (this.extraParams.uefi) {
        if (this.payLoad.options.defaults.installPartitions) {
          let index = _.findIndex(this.payLoad.options.defaults.installPartitions, function (o) {
            return o.mountPoint == 'biosboot'
          });
          if (index != -1) {
            this.payLoad.options.defaults.installPartitions.splice(index, 1);
            this.payLoad.options.defaults.installPartitions.push(
                {
                  "mountPoint": "/boot/efi",
                  "size": "200",
                  "fsType": "vfat"
                }
            );
          }
        }
      } else {
        let index = _.findIndex(this.payLoad.options.defaults.installPartitions, function (o) {
          return o.mountPoint == '/boot/efi'
        });
        if (index != -1) {
          this.payLoad.options.defaults.installPartitions.splice(index, 1);
          this.payLoad.options.defaults.installPartitions.push(
              {
                "mountPoint": "biosboot",
                "size": "1",
                "fsType": "biosboot"
              }
          );
        }
      }
      console.table(this.payLoad.options.defaults.installPartitions);
    },
    restoreParams: function () {
      this.restorePartition();
    },
    restorePartition: function () {
      this.payLoad = JSON.parse(JSON.stringify(this.defaultPayLoad));
      this.extraParams.unit = "MB";
      this.getAllImage();
    },
    valid: function () {
      this.validateResult = true;
      let that = this;
      this.$refs['form'].validate((valid) => {
        if (valid) {
          that.$refs['nicForm'].forEach(f => {
            f.validate((valid) => {
              if (!valid) {
                that.validateResult = false;
              }
            });
          });

          that.$refs['nic2Form'].forEach(f => {
            f.validate((valid) => {
              if (!valid) {
                that.validateResult = false;
              }
            });
          });
        } else {
          that.validateResult = false;
        }
      });
      if (!that.validateResult) {
        this.$message.error(this.$t("param_check_error"));
        return;
      }

      let hostname = this.payLoad.options.defaults.hostname;
      let rootPassword = this.payLoad.options.defaults.rootPassword;
      if (this.payLoad.options.defaults.networkDevices && this.payLoad.options.defaults.networkDevices.length > 0) {
        let nic = this.payLoad.options.defaults.networkDevices[0].device;
        let ip = this.payLoad.options.defaults.networkDevices[0].ipv4.ipAddr;
        let gateway = this.payLoad.options.defaults.networkDevices[0].ipv4.gateway;
        let netmask = this.payLoad.options.defaults.networkDevices[0].ipv4.netmask;
        if (isAnyBlank(nic, ip, gateway, netmask)) {
          this.$message.error(this.$t("param_cannot_be_null!"));
          return false;
        }
      }

      if (isAnyBlank(hostname, rootPassword)) {
        this.$message.error(this.$t("param_cannot_be_null!"));
        return false;
      }
      if (/[^0-9a-zA-Z\-]+/.test(hostname)) {
        this.$message.error(this.$t("hostname_must_not_have_invalid_word!"));
        return false;
      }

      let exists = 0;
      for (let j = 0; this.payLoad.options.defaults.installPartitions && j < this.payLoad.options.defaults.installPartitions.length; j++) {
        let p = this.payLoad.options.defaults.installPartitions[j];
        if (p.deviceType == 'lvm' && !p.lvmName) {
          this.$message.error(this.$t('i18n_lvmname_null'));
          this.validateResult = false;
        }
        if (!p.mountPoint) {
          this.$message.error(this.$t('i18n_mount_point_cant_be_null'));
          this.validateResult = false;
        }
        if (!p.size) {
          this.$message.error(this.$t('i18n_capacity_cant_be_null'));
          this.validateResult = false;
        }
        if (!p.fsType) {
          this.$message.error(this.$t('i18n_file_type_cant_be_null'));
          this.validateResult = false;
        }
        if (p.mountPoint != 'biosboot' && ((this.unit == 'GB' && p.size < 1) || (this.unit == 'MB' && p.size < 1024))) {
          this.$message.error(this.$t('i18n_all_greater_than_1_gb'));
          this.validateResult = false;
        }
        if (p.size && !/^[0-9]*$/.test(p.size)) {
          if (p.size != 'auto') {
            this.$message.error(this.$t('i18n_auto_or_number_0_or_1'));
            this.validateResult = false;
          }
        }
        if (p.size != 'auto' && !/\d+/.test(p.size)) {
          this.$message.error(this.$t('i18n_must_be_auto_or_number'));
          this.validateResult = false;
        }
        if (p.mountPoint == '/' || p.mountPoint == '/boot' || p.mountPoint == 'swap' || p.mountPoint == 'biosboot' || p.mountPoint == '/boot/efi') {
          exists++;
          if (p.mountPoint == 'swap' && p.fsType != 'swap') {
            this.$message.error(this.$t('i18n_swap_must'));
            this.validateResult = false;
          }

          if ((this.unit == 'GB' && p.size < 1 && p.mountPoint != 'biosboot' && p.mountPoint != '/boot/efi') || (this.unit == 'MB' && p.size < 1024 && p.mountPoint != 'biosboot' && p.mountPoint != '/boot/efi')) {
            this.$message.error(this.$t('i18n_must_be_root_swap_boot'));
            this.validateResult = false;
          }
        }
      }
      if (this.extraParams.customPartition && exists != 4) {
        this.$message.error(this.$t('i18n_mut_only_one'));
        this.validateResult = false;
      }

      if (this.extraParams.customPartition && this.payLoad.options.defaults.installPartitions.length < 4) {
        this.$message.error(this.$t('i18n_must_be_root_swap_boot'));
        this.validateResult = false;
      }

      let macs = [];
      if (this.payLoad.options.defaults.networkDevices && this.payLoad.options.defaults.networkDevices.length > 0) {
        for (let a = 0; a < this.payLoad.options.defaults.networkDevices.length; a++) {
          if (this.payLoad.options.defaults.networkDevices[a].device) {
            macs.push(this.payLoad.options.defaults.networkDevices[a].device);
          }

          if (this.payLoad.options.defaults.networkDevices[a].ipv4.vlanIds && this.payLoad.options.defaults.networkDevices[a].ipv4.vlanIds.length == 0) {
            delete this.payLoad.options.defaults.networkDevices[a].ipv4.vlanIds;
          }
        }
      }

      macs = _.groupBy(macs, function (b) {
        return b
      })

      _.forEach(macs, (mac) => {
        if (mac.length > 1) {
          this.$message.error(this.$t("i18n_mac_dup"));
          that.validateResult = false;
        }
      })

      if (this.payLoad.options.defaults.bonds && this.payLoad.options.defaults.bonds.length == 0) {
        delete this.payLoad.options.defaults.bonds;
      }
      return this.validateResult;
    }
    ,
    getAllImage: function () {
      HttpUtil.post("/image/allImage", {}, (res) => {
        this.allImages = _.filter(res.data, i => i.os == 'centos');
        if (!this.allImages || !this.allImages.length) {
          this.$message.error(this.$t('no_valid_image!'));
          return;
        }
        if (!this.payLoad.options.defaults.repo) {
          let centosImage = _.find(this.allImages, i => i => i.os == 'centos');
          if (centosImage) {
            this.payLoad.options.defaults.repo = centosImage.url;
            if (centosImage.pName) {
              this.payLoad.options.defaults.profile = centosImage.pName;
            }
            if (centosImage.tName) {
              this.payLoad.options.defaults.installScript = centosImage.tName;
            }
          } else {
            this.$message.error(this.$t('no_valid_image!'));
          }
        } else {
          let that = this;
          let centosImage = _.find(that.allImages, i => i.url == that.payLoad.options.defaults.repo);
          if (centosImage) {
            this.payLoad.options.defaults.repo = centosImage.url;
            if (centosImage.pName) {
              this.payLoad.options.defaults.profile = centosImage.pName;
            } else {
              delete this.payLoad.options.defaults.profile;
            }
            if (centosImage.tName) {
              this.payLoad.options.defaults.installScript = centosImage.tName;
            } else {
              delete this.payLoad.options.defaults.installScript;
            }
            if (centosImage.pName || centosImage.tName) {
              this.$message.info(this.$t("not_support_validate"));
            }
          } else {
            this.payLoad.options.defaults.repo = null;
          }

        }
      });
    }
    ,
    getHardware() {
      HttpUtil.get("/bare-metal/hardwares/" + this.$attrs.bareMetalId, null, (res) => {
        this.cpus = res.data.cpus;
        this.memories = res.data.memories;
        this.disks = res.data.disks;
        this.nics = res.data.nics;

        if (this.payLoad.options.defaults.networkDevices && this.payLoad.options.defaults.networkDevices[0].device == 'em1' && this.nics.length > 0) {
          this.payLoad.options.defaults.networkDevices[0].device = this.nics[0].mac;
        }
      })
    }
    ,
  }
}
</script>
