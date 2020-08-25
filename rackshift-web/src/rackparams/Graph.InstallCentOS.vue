<template>
    <div>
        <el-card>
          <el-form label-width="101px">
            <el-form-item :label="$t('hostname')">
              <el-input v-model="payLoad.options.defaults.hostname" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item :label="$t('root_pwd')">
              <el-input v-model="payLoad.options.defaults.rootPassword" autocomplete="off"
                        type="password"></el-input>
            </el-form-item>
            <el-form-item :label="$t('image')">
              <el-select v-model="payLoad.options.defaults.repo">
                <el-option v-for="g in allImages" :label="g.name"
                           :value="g.url"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('network_devices')">
              <table class="detail-info" style="float: left;">
                <tr>
                  <td>{{ $t('network_card_mac') }}</td>
                  <td>{{ $t('ip_addr') }}</td>
                  <td>{{ $t('gateway') }}</td>
                  <td>{{ $t('netmask') }}</td>
                </tr>
                <tr v-for="d in payLoad.options.defaults.networkDevices">
                  <td>
                    <el-select v-model="d.device">
                      <el-option :label="n.number + '(' + n.mac + ')'" :value="n.mac"
                                 v-for="n in nics"></el-option>
                    </el-select>
                  </td>
                  <td>
                    <el-input v-model="d.ipv4.ipAddr"></el-input>
                  </td>
                  <td>
                    <el-input v-model="d.ipv4.gateway"></el-input>
                  </td>
                  <td>
                    <el-input v-model="d.ipv4.netmask"></el-input>
                  </td>
                </tr>
              </table>
            </el-form-item>
            <el-form-item :label="$t('custom_partition')">
              <el-switch
                  v-model="showPartition"
                  active-color="#13ce66"
                  inactive-color="#ff4949">
              </el-switch>
              <table class="detail-info" style="float: left;margin-top:20px;" v-show="showPartition">
                <thead>
                <tr>
                  <th>
                    <el-button type="primary" icon="el-icon-plus" circle
                               @click="payLoad.options.defaults.installPartitions.push({})">
                    </el-button>
                  </th>
                  <th>{{ $t('mount_point') }}</th>
                  <th>
                    <el-row>
                      <el-col :span="12">{{ $t('capacity') }}</el-col>
                      <el-col :span="12">
                        <el-select v-model="unit">
                          <el-option :value="unit" :key="unit" v-for="unit in  units"></el-option>
                        </el-select>
                      </el-col>
                    </el-row>
                  </th>
                  <th>{{ $t('fs_type') }}</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="(partition, index) in payLoad.options.defaults.installPartitions"
                    v-show="partition.mountPoint != 'biosboot'">
                  <td>
                    <el-button type="primary" icon="el-icon-minus" circle
                               @click="payLoad.options.defaults.installPartitions.splice(index, 1)">
                    </el-button>
                  </td>
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
                </tr>
                </tbody>
              </table>
            </el-form-item>
          </el-form>
        </el-card>
    </div>
</template>
<script>
import HttpUtil from "../common/utils/HttpUtil";

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
                "size": "5000",
                "fsType": "swap"
              },
              {
                "mountPoint": "/boot",
                "size": "5000",
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
      payLoad: this.$attrs.params ? JSON.parse(JSON.stringify(this.$attrs.params)) : {
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
                "size": "5000",
                "fsType": "swap"
              },
              {
                "mountPoint": "/boot",
                "size": "5000",
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
      extraParams: this.$attrs.extraParams ? JSON.parse(JSON.stringify(this.$attrs.extraParams)) : {
        unit: 'MB'
      },
      unit: this.$attrs.extraParams ? JSON.parse(JSON.stringify(this.$attrs.extraParams)).unit : 'MB',
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
      showPartition: false
    };
  },
  watch: {
    unit: function (newV, oldV) {
      if (oldV == 'GB' && newV == 'MB') {
        for (var i = 0; i < this.payLoad.options.defaults.installPartitions.length && this.payLoad.options.defaults.installPartitions[i].mountPoint != 'biosboot'; i++) {
          if (/\d+/.test(this.payLoad.options.defaults.installPartitions[i].size))
            this.payLoad.options.defaults.installPartitions[i].size = this.payLoad.options.defaults.installPartitions[i].size * 1024;
        }
      } else if (oldV == 'MB' && newV == 'GB') {
        for (var i = 0; i < this.payLoad.options.defaults.installPartitions.length && this.payLoad.options.defaults.installPartitions[i].mountPoint != 'biosboot'; i++) {
          if (/\d+/.test(this.payLoad.options.defaults.installPartitions[i].size))
            this.payLoad.options.defaults.installPartitions[i].size = parseInt((this.payLoad.options.defaults.installPartitions[i].size / 1024));
        }
      }
      this.extraParams.unit = newV;
    }
  },
  mounted() {
    this.getAllImage();
    this.getHardware();
  }
  ,
  methods: {
    restoreParams: function () {
      this.restorePartition();
    },
    restorePartition: function () {
      this.payLoad = JSON.parse(JSON.stringify(this.defaultPayLoad));
      this.getAllImage();
    },
    valid: function () {
      this.validateResult = true;
      let exists = 0;
      for (let j = 0; j < this.payLoad.options.defaults.installPartitions.length; j++) {
        let p = this.payLoad.options.defaults.installPartitions[j];
        if (!p.mountPoint) {
          this.$notify.error(this.$t('i18n_mount_point_cant_be_null', '挂载点不能为空！'));
          this.validateResult = false;
        }
        if (!p.size) {
          this.$notify.error(this.$t('i18n_capacity_cant_be_null', '容量不能为空！'));
          this.validateResult = false;
        }
        if (!p.fsType) {
          this.$notify.error(this.$t('i18n_file_type_cant_be_null', '文件类型不能为空！'));
          this.validateResult = false;
        }
        if (p.mountPoint != 'biosboot' && ((this.unit == 'GB' && p.size < 1) || (this.unit == 'MB' && p.size < 1024))) {
          this.$notify.error(this.$t('i18n_all_greater_than_1_gb', '所有分区最低容量不能小于1GB！'));
          this.validateResult = false;
        }
        if (p.size && !/^[0-9]*$/.test(p.size)) {
          if (p.size != 'auto') {
            this.$notify.error(this.$t('i18n_auto_or_number_0_or_1', '分区容量只能是数字或者auto，并且容量为auto的分区只能为0个或者1个!'));
            this.validateResult = false;
          }
        }
        if (p.size != 'auto' && !/\d+/.test(p.size)) {
          this.$notify.error(this.$t('i18n_must_be_auto_or_number', '分区容量必须是auto或数字！'));
          this.validateResult = false;
        }
        if (p.mountPoint == '/' || p.mountPoint == '/boot' || p.mountPoint == 'swap' || p.mountPoint == 'biosboot') {
          exists++;
          if (p.mountPoint == 'swap' && p.fsType != 'swap') {
            this.$notify.error(this.$t('i18n_swap_must', 'swap分区格式必须是swap！'));
            this.validateResult = false;
          }

          if ((this.unit == 'GB' && p.size < 1 && p.mountPoint != 'biosboot') || (this.unit == 'MB' && p.size < 1024 && p.mountPoint != 'biosboot')) {
            this.$notify.error(this.$t('i18n_must_be_root_swap_boot', '必须有根，swap，biosboot和/boot分区，并且除了biosboot分区所有分区最低容量不能小于1GB！'));
            this.validateResult = false;
          }
        }
      }
      if (exists != 4) {
        this.$notify.error(this.$t('i18n_mut_only_one', '必须有且仅有一个根，swap，biosboot和和/boot分区！'));
        this.validateResult = false;
      }

      if (this.payLoad.options.defaults.installPartitions.length < 4) {
        this.$notify.error(this.$t('i18n_must_be_root_swap_boot', '必须有根，swap，biosboot和/boot分区，并且除了biosboot分区所有分区最低容量不能小于1GB！'));
        return false;
      }

      return this.validateResult;
    }
    ,
    getAllImage: function () {
      HttpUtil.post("/image/list/" + 1 + "/" + 1000, {}, (res) => {
        this.allImages = res.data.listObject;
        if (!this.allImages) {
          this.$notify.info(this.$t('no_valid_image!'));
          return;
        }
        if (!this.payLoad.options.defaults.repo) {
          let centosImage = _.find(this.allImages, i => i.os == 'centos');
          if (centosImage) {
            this.payLoad.options.defaults.repo = centosImage.url;
          } else {
            this.$notify.info(this.$t('no_valid_image!'));
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