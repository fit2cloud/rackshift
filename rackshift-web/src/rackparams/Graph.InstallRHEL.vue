<template>
  <div>
    <el-form label-width="130px" :rules="rules" :model="payLoad.options.defaults" ref="form" label-position="right">
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
                       default-first-option>
              <el-option v-for="g in allImages" :label="g.name"
                         :value="g.url"></el-option>
            </el-select>
          </el-form-item>

          <el-form-item :label="$t('custom_partition')">
            <el-switch
                v-model="extraParams.customPartition">
            </el-switch>
            <table class="detail-info-rack" v-show="extraParams.customPartition">
              <thead>
              <tr>
                <th>{{ $t('mount_point') }}</th>
                <th>
                  <el-row>
                    <el-col :span="12">{{ $t('capacity') }}</el-col>
                    <el-col :span="12">
                      <el-select v-model="extraParams.unit">
                        <el-option :value="unit" :key="unit" v-for="unit in  units"></el-option>
                      </el-select>
                    </el-col>
                  </el-row>
                </th>
                <th>{{ $t('fs_type') }}</th>
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
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">

        </el-col>
        <el-col :span="12"></el-col>
      </el-row>

    </el-form>
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

let _ = require('lodash');
export default {
  activated() {
  },
  created() {

  },
  deactivated() {

  },
  data() {
    return {
      activeNames: '0',
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
                "fsType": "ext3"
              },
              {
                "mountPoint": "swap",
                "size": "4096",
                "fsType": "swap"
              },
              {
                "mountPoint": "/boot",
                "size": "4096",
                "fsType": "ext3"
              },
              {
                "mountPoint": "biosboot",
                "size": "1",
                "fsType": "biosboot"
              }
            ]
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
    })
  }
  ,
  methods: {
    changeInt(arr) {
      let a = [];
      arr.ipv4.vlanIds.forEach(v => a.push(Number(v)));
      arr.ipv4.vlanIds = a;
    },
    getNetworkName(d) {
      return !d.device ? this.$t("network_card_mac") : this.$t("network_card_mac") + " " + d.device;
    },
    delNet(index) {
      if (this.payLoad.options.defaults.networkDevices.length - 1 > 0) {
        this.payLoad.options.defaults.networkDevices.splice(index, 1);
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
      let nic = this.payLoad.options.defaults.networkDevices[0].device;
      let ip = this.payLoad.options.defaults.networkDevices[0].ipv4.ipAddr;
      let gateway = this.payLoad.options.defaults.networkDevices[0].ipv4.gateway;
      let netmask = this.payLoad.options.defaults.networkDevices[0].ipv4.netmask;
      if (isAnyBlank(hostname, rootPassword, nic, ip, gateway, netmask)) {
        this.$message.error(this.$t("param_cannot_be_null!"));
        return false;
      }
      if (/[^0-9a-zA-Z\-]+/.test(hostname)) {
        this.$message.error(this.$t("hostname_must_not_have_invalid_word!"));
        return false;
      }

      let exists = 0;
      for (let j = 0; j < this.payLoad.options.defaults.installPartitions.length; j++) {
        let p = this.payLoad.options.defaults.installPartitions[j];
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
      if (exists != 4) {
        this.$message.error(this.$t('i18n_mut_only_one'));
        this.validateResult = false;
      }

      if (this.payLoad.options.defaults.installPartitions.length < 4) {
        this.$message.error(this.$t('i18n_must_be_root_swap_boot'));
        this.validateResult = false;
      }

      let macs = [];
      if (this.payLoad.options.defaults.networkDevices && this.payLoad.options.defaults.networkDevices.length > 0) {
        for (let a = 0; a < this.payLoad.options.defaults.networkDevices.length; a++) {
          if (this.payLoad.options.defaults.networkDevices[a].device) {
            macs.push(this.payLoad.options.defaults.networkDevices[a].device);
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
      return this.validateResult;
    }
    ,
    getAllImage: function () {
      HttpUtil.post("/image/list/" + 1 + "/" + 1000, {}, (res) => {
        this.allImages = _.filter(res.data.listObject, i => i.os == 'redhat');
        if (!this.allImages) {
          this.$message.error(this.$t('no_valid_image!'));
          return;
        }
        if (!this.payLoad.options.defaults.repo) {
          let centosImage = _.find(this.allImages, i => i.os == 'redhat');
          if (centosImage) {
            this.payLoad.options.defaults.repo = centosImage.url;
          } else {
            this.$message.error(this.$t('no_valid_image!'));
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

        if (this.payLoad.options.defaults.networkDevices[0].device == 'em1' && this.nics.length > 0) {
          this.payLoad.options.defaults.networkDevices[0].device = this.nics[0].mac;
        }
      })
    }
    ,
  }
}
</script>