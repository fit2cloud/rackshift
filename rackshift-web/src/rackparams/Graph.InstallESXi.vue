<template>
  <div>
    <el-form label-width="130px" :rules="rules" :model="payLoad.options.defaults" ref="form" label-position="right">
      <el-row>
        <el-col :span="11">
          <el-form-item :label="$t('hostname')" prop="hostname">
            <el-input v-model="payLoad.options.defaults.hostname" autocomplete="off" aria-required="true"
                      maxlength="10"></el-input>
          </el-form-item>
          <el-form-item :label="$t('domain')" prop="domain">
            <el-input v-model="payLoad.options.defaults.domain" autocomplete="off" aria-required="true"
                      maxlength="10"></el-input>
          </el-form-item>
          <el-form-item :label="$t('root_pwd')" prop="rootPassword">
            <el-input v-model="payLoad.options.defaults.rootPassword" autocomplete="off"
                      show-password maxlength="10"></el-input>
          </el-form-item>
          <el-form-item :label="$t('image')" prop="repo">
            <el-select v-model="payLoad.options.defaults.repo" class="input-element" filterable
                       allow-create
                       default-first-option>
              <el-option v-for="g in allImages" :label="g.name"
                         :value="g.url"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item :label="$t('network_card')">
            <RSButton @click="addNet" type="add" :tip="$t('add_network_card')"></RSButton>

            <el-collapse v-model="nicActiveNames">
              <el-collapse-item v-for="(d, $index) in payLoad.options.defaults.networkDevices"
                                :title="getNetworkName(d)" :name="$index + ''">
                <RSButton @click="delNet" type="del" :tip="$t('del_network_card')"></RSButton>
                <el-form :model="d" :rules="nicRules"
                         ref="nicForm" label-position="right" label-width="185px">

                  <el-form-item prop="esxSwitchName" :label="$t('esxSwitchName')">
                    <el-input v-model="d.esxSwitchName" class="input-element" disabled>
                    </el-input>
                  </el-form-item>

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

                    <el-form-item prop="vlanIds" :label="$t('VLAN')">
                      <el-select
                          v-model="d.ipv4.vlanIds"
                          multiple
                          filterable
                          allow-create
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
        <el-col :span="13">
          <el-form-item :label="$t('DNS')" prop="dnsServers">
            <el-select v-model="payLoad.options.defaults.dnsServers" multiple
                       filterable
                       allow-create
                       default-first-option></el-select>
          </el-form-item>

          <el-form-item :label="$t('vSwitch')">
            <RSButton @click="addSwitch" type="add" :tip="$t('add_switch')"></RSButton>

            <el-collapse v-model="switchActiveNames">
              <el-collapse-item v-for="(d, $index) in payLoad.options.defaults.switchDevices"
                                :title="d.switchName" :name="'switch' + $index + ''">
                <RSButton @click="delSwitch" type="del" :tip="$t('del_switch')"></RSButton>
                <el-form :model="d" :rules="nicRules"
                         ref="nicForm" label-position="right" label-width="185px">

                  <el-form-item prop="esxSwitchName" :label="$t('esxSwitchName')">
                    <el-input v-model="d.switchName" class="input-element">
                    </el-input>
                  </el-form-item>

                  <el-form-item prop="uplinks" :label="$t('pls_select_') + $t('network_card')">
                    <el-select v-model="d.uplinks" class="input-element" multiple @change="changeSwitchName(d, $event)">
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
  </div>
</template>
<script>
import HttpUtil from "../common/utils/HttpUtil";
import {isAnyBlank} from "@/common/utils/CommonUtil";
import {
  hostnameValidator,
  repoSelectValidator,
  domainValidator ,
  dnsValidator,
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
      nicActiveNames: '0',
      switchActiveNames: 'switch0',
      rules: {
        hostname: [
          {validator: hostnameValidator, trigger: 'blur', vue: this},
        ],
        domain: [
          {validator: domainValidator, trigger: 'blur', vue: this},
        ],
        dnsServers: [
          {validator: dnsValidator, trigger: 'change', vue: this},
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
        switchName: [
          {validator: requiredValidator, trigger: 'blur', vue: this},
        ],
        uplinks: [
          {validator: requiredValidator, trigger: 'change', vue: this},
        ],
      },
      uefi: false,
      defaultPayLoad: {
        "options": {
          "defaults": {
            "version": "6.7",
            "repo": "http://172.31.128.1:8080/esxi/6.7",
            "rootPassword": "RackHDRocks!",
            "hostname": "rackhd-node",
            "domain": "example.com",
            "users": [
              {
                "name": "rackhd1",
                "password": "123456"
              }
            ],
            "dnsServers": [
              "172.12.88.91",
              "192.168.20.77"
            ],
            "ntpServers": [
              "0.vmware.pool.ntp.org",
              "1.vmware.pool.ntp.org"
            ],
            "networkDevices": [
              {
                "device": null,
                "ipv4": {
                  "ipAddr": "172.31.128.7",
                  "gateway": "172.31.128.1",
                  "netmask": "255.255.255.0",
                  "vlanIds": [
                    10
                  ]
                },
                "esxSwitchName": "vSwitch0"
              }
            ],
            "installDisk": "/dev/sda",
            "switchDevices": [
              {
                "switchName": "vSwitch0",
                "uplinks": [
                  "vmnic4"
                ]
              }
            ],
            "postInstallCommands": [
              "echo This command will run at the end ",
              "echo of the post installation step"
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
  }
  ,
  methods: {
    changeSwitchName(d) {
      _.forEach(this.payLoad.options.defaults.networkDevices, (m) => {
        if (m.device) {
          if (d.uplinks && d.uplinks.indexOf(m.device) != -1) {
            m.esxSwitchName = d.switchName;
          }
        }
      })
      console.log(d);
    },
    getNetworkName(d) {
      return !d.device ? this.$t("network_card_mac") : this.$t("network_card_mac") + " " + d.device;
    },
    delNet(index) {
      if (this.payLoad.options.defaults.networkDevices.length - 1 > 0) {
        this.payLoad.options.defaults.networkDevices.splice(this.payLoad.options.defaults.networkDevices.length - 1, 1);
      }
    },
    addNet() {
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
    delSwitch(index) {
      if (this.payLoad.options.defaults.switchDevices.length - 1 > 0) {
        this.payLoad.options.defaults.switchDevices.splice(this.payLoad.options.defaults.switchDevices.length - 1, 1);
      }
    },
    addSwitch() {
      if (this.payLoad.options.defaults.switchDevices && this.payLoad.options.defaults.switchDevices.length == this.nics.length) {
        this.$message.warning(this.$t("cannot_add_more"));
        return;
      }
      this.payLoad.options.defaults.switchDevices.push({
        "switchName": "vSwitch" + this.payLoad.options.defaults.switchDevices.length,
        "uplinks": []
      })
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

      if (this.payLoad.options.defaults.switchDevices && this.payLoad.options.defaults.switchDevices.length > 0) {
        if (_.filter(this.payLoad.options.defaults.switchDevices, (s) => s.switchName == 'vSwitch0').length == 0) {
          this.$message.error(this.$t("i18n_at_least_sw0"));
          that.validateResult = false;
        }
      }

      return this.validateResult;
    }
    ,
    getAllImage: function () {
      HttpUtil.post("/image/list/" + 1 + "/" + 1000, {}, (res) => {
        this.allImages = res.data.listObject;
        if (!this.allImages) {
          this.$message.error(this.$t('no_valid_image!'));
          return;
        }
        if (!this.payLoad.options.defaults.repo) {
          let centosImage = _.find(this.allImages, i => i.os == 'esxi');
          if (centosImage) {
            this.payLoad.options.defaults.repo = centosImage.url;
          } else {

            this.$message.error(this.$t('no_valid_image!'));
            this.allImages = _.filter(this.allImages, i => i.os == 'esxi');
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