<template>
  <div>
    <el-form label-width="101px">
      <el-form-item :label="$t('custom_raid')">
        <table class="detail-info">
          <thead>
          <tr>
            <th>{{ $t('raid_type') }}</th>
            <th>{{ $t('raid_disk') }}</th>
            <th>
              <el-button type="primary" icon="el-icon-plus" circle
                         @click="addRaid()">
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
              <el-select v-model="config.drives" multiple @change="changeDisk(config, index)">
                <el-option v-for="t in odisks" :label="t.drives"
                           :value="parseInt(t.drive)">
                  {{
                    (t.type ? t.type : $t('unknown')) + ' raidLevel:' + (t.raid ? t.raid : '无') + '-enclosureId:' + t.enclosureId + '-driveId:' + t.drive + '-' + t.size
                  }}
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
import {isomerismDisk} from "../common/utils/RackHDUtil"

export default {
  activated() {
  },
  created() {
  },
  deactivated() {

  },
  data() {
    return {
      enclosure: 32,
      defaultPayLoad: {
        "options": {
          "bootstrap-rancher": {
            "dockerFile": "secure.erase.docker.tar.xz"
          },
          "create-raid": {
            "createDefault": false,
            "controller": 0,
            "path": "/opt/MegaRAID/perccli/perccli64",
            "raidList": [
              {
                "enclosure": 32,
                "type": null,
                "drives": [],
                "name": "VD0"
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
            "controller": 0,
            "path": "/opt/MegaRAID/perccli/perccli64",
            "raidList": [
              {
                "enclosure": 32,
                "type": null,
                "drives": [],
                "name": "VD0"
              }
            ]
          }
        }
      },
      extraParams: this.$attrs.extraParams ? JSON.parse(JSON.stringify(this.$attrs.extraParams)) : {},
      cpus: [],
      memories: [],
      disks: [],
      diskMap: {},
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
    addRaid() {
      this.payLoad.options['create-raid'].raidList.push(
          {
            "enclosure": this.enclosure,
            "type": null,
            "drives": [],
            "name": "VD" + this.payLoad.options['create-raid'].raidList.length
          }
      );
    },
    changeDisk(config) {
      if (config.drives && config.drives.length > 0) {
        let disk = _.find(this.odisks, (o) => o.drive == config.drives[0]);
        if (disk) {
          config.enclosure = disk.enclosureId;
        }
      }
    },
    restoreParams: function () {
      this.restorePartition();
    },
    restorePartition: function () {
      this.payLoad = JSON.parse(JSON.stringify(this.defaultPayLoad));
    },
    valid: function () {
      let raidConfig = this.payLoad.options["create-raid"].raidList;
      if (raidConfig.length == 0) {
        this.$message.error(this.$t('i18n_pls_os_must_choose_one_raid', '请至少选择一组磁盘组建raid！'));
        return false;
      }
      let diskStr = [];
      for (let i = 0; i < raidConfig.length; i++) {
        let config = raidConfig[i];
        let raidType = config.type;
        let raidDisk = config.drives;
        if (isomerismDisk(this.getSelectDisks(raidDisk))) {
          this.$message.error(this.$t('i18n_di') + (i + 1) + this.$t('i18n_yigou_disk'));
          return;
        }
        diskStr = diskStr.concat(raidDisk);
        if (!raidType) {
          this.$message.error(this.$t('i18n_di') + (i + 1) + this.$t('i18n_zu_de_raid_not_config'));
          return false;
        }
        if (!raidDisk || raidDisk.length == 0) {
          this.$message.error(this.$t('i18n_di') + (i + 1) + this.$t('i18n_zu_de_raid_not_config'));
          return false;
        }
        switch (raidType) {
          case "raid1":
            if (raidDisk.length % 2 != 0) {
              this.$message.error(this.$t('i18n_di') + (i + 1) + this.$t('i18n_zu') + this.$t('i18n_raid1_must_be_2n'));
              return false;
            }
            break;
          case "raid3":
            if (raidDisk.length < 3) {
              this.$message.error(this.$t('i18n_di') + (i + 1) + this.$t('i18n_zu') + this.$t('i18n_raid1_must_greater_than_3'));
              return false;
            }
            break;
          case "raid5":
            if (raidDisk.length < 3) {
              this.$message.error(this.$t('i18n_di') + (i + 1) + this.$t('i18n_zu') + this.$t('i18n_raid5_must_greater_than_3'));
              return false;
            }
            break;
          case "raid6":
            if (raidDisk.length < 4) {
              this.$message.error(this.$t('i18n_di') + (i + 1) + this.$t('i18n_zu') + this.$t('i18n_raid6_must_greater_than_4'));
              return false;
            }
            break;
          case "raid10":
            if (raidDisk.length < 4) {
              this.$message.error(this.$t('i18n_di') + (i + 1) + this.$t('i18n_zu') + this.$t('i18n_raid10_must_greater_than_4'));
              return false;
            }
            if (raidDisk.length % 2 != 0) {
              this.$message.error(this.$t('i18n_di') + (i + 1) + this.$t('i18n_zu') + this.$t('i18n_raid10_must_be_2n'));
              return false;
            }
            break;
          case "raid50":
            if (raidDisk.length < 6) {
              this.$message.error(this.$t('i18n_di') + (i + 1) + this.$t('i18n_zu') + this.$t('i18n_raid50_must_greater_than_6'));
              return false;
            }
            if (raidDisk.length % 2 != 0) {
              this.$message.error(this.$t('i18n_di') + (i + 1) + this.$t('i18n_zu') + this.$t('i18n_raid50_must_be_2n'));
              return false;
            }
            break;
          case "raid60":
            if (raidDisk.length < 8) {
              this.$message.error(this.$t('i18n_di') + (i + 1) + this.$t('i18n_zu') + this.$t('i18n_raid56_must_be_greater_than_8'));
              return false;
            }
            if (raidDisk.length % 2 != 0) {
              this.$message.error(this.$t('i18n_di') + (i + 1) + this.$t('i18n_zu') + this.$t('i18n_raid60_must_be_2n'));
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
      disks = diskStr;
      for (let j = 0; j < disks.length; j++) {
        if (newDisks.indexOf(disks[j]) == -1) {
          newDisks.push(disks[j]);
        } else {
          this.$message.error(this.$t('i18n_disk') + disks[j] + this.$t('i18n_duplicate_in_array'));
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
        let that = this;
        if (res.data.disks && res.data.disks.length > 0) {
          res.data.disks = _.orderBy(res.data.disks, ['drive'], ['asc']);
          res.data.disks.forEach(d => that.diskMap[d.drive] = d);
        }
        this.disks = res.data.disks;
        this.nics = res.data.nics;
        if (this.disks && this.disks.length > 0) {
          this.enclosure = this.disks[0].enclosureId;
          if (this.payLoad.options['create-raid'].raidList.length > 0) {
            _.forEach(this.payLoad.options['create-raid'].raidList, r => {
              r.enclosure = this.disks[0].enclosureId;
            })
          }
        }
      })
    }
    ,
    getSelectDisks(raidDisk) {
      let selected = [];
      if (raidDisk && raidDisk.length) {
        raidDisk.forEach(d => selected.push(this.diskMap[d]));
      }
      return selected;
    }
  }
}
</script>