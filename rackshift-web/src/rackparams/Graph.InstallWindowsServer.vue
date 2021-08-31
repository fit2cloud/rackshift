<template>
  <div>
    <el-form label-width="130px" :rules="rules" :model="payLoad.options.defaults" ref="form" label-position="right">
      <el-row>
        <el-col :span="11">
          <el-form-item :label="$t('hostname')" prop="hostname">
            <el-input v-model="payLoad.options.defaults.hostname" autocomplete="off" aria-required="true"
                      maxlength="20"></el-input>
          </el-form-item>
          <el-form-item :label="$t('domain')" prop="domain">
            <el-input v-model="payLoad.options.defaults.domain" autocomplete="off" aria-required="true"
                      maxlength="20"></el-input>
          </el-form-item>
          <el-form-item :label="$t('username')" prop="username">
            <el-input v-model="payLoad.options.defaults.username" autocomplete="off" aria-required="true"
                      maxlength="20"></el-input>
          </el-form-item>
          <el-form-item :label="$t('root_pwd')" prop="password">
            <el-input v-model="payLoad.options.defaults.password" autocomplete="off"
                      show-password maxlength="20"></el-input>
          </el-form-item>

          <el-form-item :label="$t('samba')" prop="smbRepo">
            <el-input v-model="payLoad.options.defaults.smbRepo"
                      placeholder="\\172.31.128.1\windowsServer2012"></el-input>
          </el-form-item>

          <el-form-item :label="$t('productkey')" prop="productkey">
            <el-input v-model="payLoad.options.defaults.productkey" placeholder="XXXX-XXXX-XXXX-XXXX-XXXX"></el-input>
          </el-form-item>

          <el-form-item :label="$t('smb_user')" prop="smbUser">
            <el-input v-model="payLoad.options.defaults.smbUser"></el-input>
          </el-form-item>

          <el-form-item :label="$t('smb_password')" prop="smbPassword">
            <el-input v-model="payLoad.options.defaults.smbPassword" show-password></el-input>
          </el-form-item>

        </el-col>
        <el-col :span="13">
          <el-form v-for="d in payLoad.options.defaults.networkDevices" :model="d" :rules="nicRules"
                   ref="nicForm" label-position="right" label-width="185px">

            <el-form-item prop="device" :label="$t('network_card_mac')">
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
            </el-form>
          </el-form>
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
  ipValidator,
  domainValidator,
  requiredSelectValidator,
  requiredValidator
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
      rules: {
        hostname: [
          {validator: hostnameValidator, trigger: 'blur', vue: this},
        ],
        password: [
          {validator: requiredValidator, trigger: 'blur', vue: this},
        ],
        domain: [
          {validator: domainValidator, trigger: 'blur', vue: this},
        ],
        username: [
          {validator: requiredValidator, trigger: 'blur', vue: this},
        ],
        smbRepo: [
          {validator: requiredValidator, trigger: 'blur', vue: this},
        ],
        productkey: [
          {validator: requiredValidator, trigger: 'blur', vue: this},
        ],
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
        ]
      },
      uefi: false,
      defaultPayLoad: {
        "options": {
          "defaults": {
            "hostname": "localhost",
            "domain": "rackhd",
            "password": "RackHDRocks!",
            "username": "onrack",
            "firewallDisable": true,
            "networkDevices": [
              {
                "device": null,
                "ipv4": {
                  "ipAddr": "172.31.128.152",
                  "gateway": "172.31.128.5",
                  "netmask": "255.255.255.0"
                }
              }
            ],
            "productkey": null,
            "smbUser": "onrack",
            "smbPassword": "onrack",
            "smbRepo": "\\\\172.31.128.1\\windowsServer2012",
            "repo": null
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
    this.getHardware();
  }
  ,
  methods: {
    restoreParams: function () {
      this.restorePartition();
    },
    restorePartition: function () {
      this.payLoad = JSON.parse(JSON.stringify(this.defaultPayLoad));
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
      let password = this.payLoad.options.defaults.password;
      let nic = this.payLoad.options.defaults.networkDevices[0].device;
      let ip = this.payLoad.options.defaults.networkDevices[0].ipv4.ipAddr;
      let gateway = this.payLoad.options.defaults.networkDevices[0].ipv4.gateway;
      let netmask = this.payLoad.options.defaults.networkDevices[0].ipv4.netmask;
      if (isAnyBlank(hostname, password, nic, ip, gateway, netmask)) {
        this.$message.error(this.$t("param_cannot_be_null!"));
        return false;
      }
      if (/[^0-9a-zA-Z\-]+/.test(hostname)) {
        this.$message.error(this.$t("hostname_must_not_have_invalid_word!"));
        return false;
      }
      return this.validateResult;
    }
    ,
    getAllImage: function () {
      HttpUtil.post("/image/allImage", {}, (res) => {
        this.allImages = _.filter(res.data, i => i.os == 'windows_server');
        if (!this.allImages || !this.allImages.length) {
          this.$message.error(this.$t('no_valid_image!'));
          return;
        }
        if (!this.payLoad.options.defaults.repo) {
          let centosImage = _.find(this.allImages, i => i.os == 'windows_server');
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