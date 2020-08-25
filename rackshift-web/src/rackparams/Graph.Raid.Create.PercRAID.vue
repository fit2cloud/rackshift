<template>
  <div>
    <el-card>
      <el-form label-width="101px">
        <el-form-item :label="$t('custom_raid')">
          <table class="detail-info" style="float: left;margin-top:20px;">
            <thead>
            <tr>
              <th>
                <el-button type="primary" icon="el-icon-plus" circle
                           @click="payLoad.options['create-raid'].raidList.push({})">
                </el-button>
              </th>
              <th>{{ $t('raid_type') }}</th>
              <th>{{ $t('raid_disk') }}</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="(config, index) in payLoad.options['create-raid'].raidList">
              <td>
                <el-button type="primary" icon="el-icon-minus" circle
                           @click="payLoad.options['create-raid']['raidList'].splice(index, 1)">
                </el-button>
              </td>
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
                             :value="t.drive">
                    {{ t.raid + '-enclosureId:' + t.enclosureId + '-driveId:' + t.drive + '-' + t.size }}
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
      this.validateResult = true;
      return this.validateResult;
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