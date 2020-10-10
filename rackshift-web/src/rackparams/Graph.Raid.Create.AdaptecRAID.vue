<template>
  <div>
    <el-form label-width="101px">
      <el-form-item :label="$t('custom_raid')">
        <table class="detail-info" style="float: left;margin-top:20px;">
          <thead>
          <tr>
            <th>{{ $t('raid_type') }}</th>
            <th>{{ $t('raid_disk') }}</th>
            <th>
              <el-button type="primary" icon="el-icon-plus" circle
                         @click="payLoad.options['create-raid'].raidList.push({})">
              </el-button>
            </th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="(config, index) in payLoad.options['create-raid'].raidList">
            <td>
              <el-select v-model="config.type">
                <el-option v-for="t in raidTypes" :label="t" :value="t">
                  {{ t }}
                </el-option>
              </el-select>
            </td>

            <td>
              <el-select v-model="config.drives" multiple>
                <el-option v-for="t in odisks" :label="t.drives"
                           :value="parseInt(t.drive)">
                  {{ t.raid + '-enclosureId:' + t.enclosureId + '-driveId:' + t.drive + '-' + t.size }}
                </el-option>
              </el-select>
            </td>
            <td>
              <el-button type="primary" icon="el-icon-minus" circle
                         @click="payLoad.options['create-raid']['raidList'].splice(index, 1)">
              </el-button>
            </td>
          </tr>
          </tbody>
        </table>
      </el-form-item>
    </el-form>
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
          "bootstrap-rancher": {
            "dockerFile": "secure.erase.docker.tar.xz"
          },
          "create-raid": {
            "createDefault": false,
            "controller": 1,
            "path": "/usr/Arcconf/arcconf",
            "raidList": [
              {
                "type": "5",
                "drives": "0 0 0 1 0 2 0 3"
              }
            ]
          }
        }
      },
      raidTypes: ["raid0", "raid1", "raid5", "raid10", "raid50"],
      payLoad: this.$attrs.params ? JSON.parse(JSON.stringify(this.$attrs.params)) : {
        "options": {
          "bootstrap-rancher": {
            "dockerFile": "secure.erase.docker.tar.xz"
          },
          "create-raid": {
            "createDefault": false,
            "controller": 1,
            "path": "/usr/Arcconf/arcconf",
            "raidList": [
              {
                "type": "5",
                "drives": "0 0 0 1 0 2 0 3"
              }
            ]
          }
        }
      },
      extraParams: this.$attrs.extraParams ? JSON.parse(JSON.stringify(this.$attrs.extraParams)) : {},
      cpus: [],
      memories: [],
      disks: [],
      nics: [],
      validateResult: false,
    };
  },
  watch: {},
  computed: {
    odisks: function () {
      return _.orderBy(this.disks, ['drive'], ['asc']);
    }
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
      let raidConfig = this.payLoad.options["create-raid"].raidList;
      if (raidConfig.length == 0) {
        this.$notify.error(this.$t('i18n_pls_os_must_choose_one_raid', '请至少选择一组磁盘组建raid！'));
        return false;
      }
      let diskStr = "";
      for (let i = 0; i < raidConfig.length; i++) {
        let config = raidConfig[i];
        let raidType = config.type;
        let raidDisk = config.drives;
        diskStr += raidDisk + ",";
        if (!raidType) {
          this.$notify.error(this.$t('i18n_di', '第') + (i + 1) + this.$t('i18n_zu_de_raid_not_config', '组的RAID策略没有设置！'));
          return false;
        }
        if (!raidDisk || raidDisk.length == 0) {
          this.$notify.error(this.$t('i18n_di', '第') + (i + 1) + this.$t('i18n_zu_de_raid_not_config', '组的RAID策略没有设置！'));
          return false;
        }
        switch (raidType) {
          case "raid1":
            if (raidDisk.length % 2 != 0) {
              this.$notify.error(this.$t('i18n_di', '第') + (i + 1) + this.$t('i18n_zu', '组') + this.$t('i18n_raid1_must_be_2n', 'raid1只能是2块或者2n块磁盘！'));
              return false;
            }
            break;
          case "raid3":
            if (raidDisk.length < 3) {
              this.$notify.error(this.$t('i18n_di', '第') + (i + 1) + this.$t('i18n_zu', '组') + this.$t('i18n_raid1_must_greater_than_3', 'raid3不能少于3块磁盘！'));
              return false;
            }
            break;
          case "raid5":
            if (raidDisk.length < 3) {
              this.$notify.error(this.$t('i18n_di', '第') + (i + 1) + this.$t('i18n_zu', '组') + this.$t('i18n_raid5_must_greater_than_3', 'raid5不能少于3块磁盘！'));
              return false;
            }
            break;
          case "raid6":
            if (raidDisk.length < 4) {
              this.$notify.error(this.$t('i18n_di', '第') + (i + 1) + this.$t('i18n_zu', '组') + this.$t('i18n_raid6_must_greater_than_4', 'raid6不能少于4块磁盘！'));
              return false;
            }
            break;
          case "raid10":
            if (raidDisk.length < 4) {
              this.$notify.error(this.$t('i18n_di', '第') + (i + 1) + this.$t('i18n_zu', '组') + this.$t('i18n_raid10_must_greater_than_4', 'raid10不能少于4块磁盘！'));
              return false;
            }
            if (raidDisk.length % 2 != 0) {
              this.$notify.error(this.$t('i18n_di', '第') + (i + 1) + this.$t('i18n_zu', '组') + this.$t('i18n_raid10_must_be_2n', 'raid10磁盘数必须是2的倍数！'));
              return false;
            }
            break;
          case "raid50":
            if (raidDisk.length < 6) {
              this.$notify.error(this.$t('i18n_di', '第') + (i + 1) + this.$t('i18n_zu', '组') + this.$t('i18n_raid50_must_greater_than_6', 'raid50不能少于6块磁盘！'));
              return false;
            }
            if (raidDisk.length % 2 != 0) {
              this.$notify.error(this.$t('i18n_di', '第') + (i + 1) + this.$t('i18n_zu', '组') + this.$t('i18n_raid50_must_be_2n', 'raid50磁盘数必须是2的倍数！'));
              return false;
            }
            break;
          case "raid60":
            if (raidDisk.length < 8) {
              this.$notify.error(this.$t('i18n_di', '第') + (i + 1) + this.$t('i18n_zu', '组') + this.$t('i18n_raid56_must_be_greater_than_8', 'raid60不能少于8块磁盘！'));
              return false;
            }
            if (raidDisk.length % 2 != 0) {
              this.$notify.error(this.$t('i18n_di', '第') + (i + 1) + this.$t('i18n_zu', '组') + this.$t('i18n_raid60_must_be_2n', 'raid60磁盘数必须是2的倍数！'));
              return false;
            }
            break;
        }
      }
      //判断磁盘没有被重复选择
      if (diskStr.length == 0) {
        return false;
      }
      let disks = [];
      let newDisks = [];
      disks = diskStr.substr(0, diskStr.length - 1).split(",");
      for (let j = 0; j < disks.length; j++) {
        if (newDisks.indexOf(disks[j]) == -1) {
          newDisks.push(disks[j]);
        } else {
          this.$notify.error(this.$t('i18n_disk', '磁盘：') + diskMap[disks[j]].drive + this.$t('i18n_duplicate_in_array', '被重复选择进多个阵列，请检查！'));
          return false;
        }
      }
      return true;
    }
    ,
    getHardware() {
      HttpUtil.get("/bare-metal/hardwares/" + this.$attrs.bareMetalId, null, (res) => {
        this.cpus = res.data.cpus;
        this.memories = res.data.memories;
        this.disks = res.data.disks;
        this.nics = res.data.nics;
      })
    }
    ,
  }
}
</script>