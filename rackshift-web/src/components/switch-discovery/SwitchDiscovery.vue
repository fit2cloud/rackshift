<template>
  <div>
    <el-button class="el-button el-button--default el-button--mini is-plain pointer" @click="back"><i
        class="el-icon-back"></i>&nbsp;&nbsp;&nbsp;&nbsp;{{
        $t('back')
      }}
    </el-button>
    <el-divider content-position="left">{{ $t('Switch_Discovery_Rule') }}</el-divider>
    <div class="container-switch-discovery">

      <div class="machine-title">
        <el-button-group class="batch-button">
          <el-button type="primary" icon="el-icon-circle-plus-outline" @click="handleEdit({}, 'add')">{{
              $t('add')
            }}
          </el-button>
          <el-button type="primary" icon="el-icon-delete" @click="delAllSelection">{{ $t('del') }}
          </el-button>
          <el-button type="primary" icon="el-icon-refresh" @click="getData">{{ $t('refresh') }}</el-button>
        </el-button-group>
      </div>

      <el-table
          :data="tableData"
          class="table"
          ref="multipleTable"
          v-loading="loadingList"
          header-cell-class-name="table-header"
          style="width: 100%"
          @sort-change="sortChange($event)"
          @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" align="left"></el-table-column>

        <el-table-column :prop="c.dbProp" :label="$t(c.label)" align="left"
                         v-for="c in columns" :sortable="c.sort"></el-table-column>
        <el-table-column prop="syncStatus" :label="$t('sync_status')" align="left">
          <template slot-scope="scope">
            <i class="el-icon-loading" v-if="scope.row.syncStatus.indexOf('ING') != -1"></i>
            {{ $t(scope.row.syncStatus) }}
          </template>
        </el-table-column>

        <el-table-column prop="number" :label="$t('founded_machine_number')" align="left">
          <template slot-scope="scope">
            <el-link v-if="scope.row.number != '0'" type="primary" @click="showMachine(scope.row)">
              {{ scope.row.number }}
            </el-link>
            <span v-if="scope.row.number == '0'">{{ scope.row.number }}</span>
          </template>
        </el-table-column>

        <el-table-column prop="lastSyncTimestamp" :label="$t('last_sync_timestamp')" align="left">
          <template slot-scope="scope">
            {{ scope.row.lastSyncTimestamp | dateFormat }}
          </template>
        </el-table-column>

        <el-table-column prop="" :label="$t('opt')" align="left">
          <template slot-scope="scope">
            <RSButton @click="handleEdit(scope.row, 'edit')"></RSButton>
            <RSButton @click="handleEdit(scope.row, 'del')" type="del"></RSButton>
            <RSButton @click="handleEdit(scope.row, 'sync')" type="sync" :tip="$t('sync')"></RSButton>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
            @size-change="handleSizeChange"
            @current-change="handlePageChange"
            :current-page="query.pageIndex"
            :page-sizes="[10, 20, 50, 100]"
            :page-size="10"
            layout="total, sizes, prev, pager, next, jumper"
            :total="pageTotal">
        </el-pagination>
      </div>

      <el-drawer
          :title="editType == 'edit' ? $t('edit_switch-discovery') : $t('add_switch-discovery')"
          :visible.sync="editDialogVisible"
          direction="rtl"
          :modal=true
          :wrapperClosable="false"
          :appendToBody="true"
          :before-close="handleClose">
        <div class="demo-drawer__content">
          <el-form :model="editObj" :rules="rules" ref="form" label-width="50px" :label-position="labelPosition">
            <el-form-item :label="$t('name')" prop="name">
              <el-input v-model="editObj.name" autocomplete="off" maxlength="30"></el-input>
            </el-form-item>

            <el-form-item :label="$t('start_ip')" prop="startIp">
              <el-input v-model="editObj.startIp" autocomplete="off"></el-input>
            </el-form-item>

            <el-form-item :label="$t('end_ip')" prop="endIp">
              <el-input v-model="editObj.endIp" autocomplete="off"></el-input>
            </el-form-item>

            <el-form-item :label="$t('mask')" prop="mask">
              <el-input v-model="editObj.mask" autocomplete="off"></el-input>
            </el-form-item>

            <el-form-item :label="$t('detect_params')" prop="params">
              <el-switch v-model="editObj.config">{{ $t('set_detect_params') }}</el-switch>
              <table class="test-protocol" v-if="editObj.config">
                <tr>
                  <td>{{ $t('protocol') }}</td>
                  <td>{{ $t('possible_params') }}</td>
                  <td>
                    <RSButton type="add" :tip="$t('add_protocol_param')" @click="addTestProtocol"></RSButton>
                  </td>
                </tr>
                <tr v-for="(p, $index) in editObj.credentialParam">
                  <td>
                    <el-select v-model="p.protocol">
                      <el-option v-for="pro in protocols" :value="pro.name" :label="$t(pro.name)">
                      </el-option>
                    </el-select>
                  </td>
                  <td>
                    <el-form :rules="protocolRules" ref="protocolForm" :model="p">
                      <el-form-item v-if="p && p.protocol" v-for="param in findProtocol(p)" :label="$t(param.label)"
                                    :prop="param.prop">
                        <el-input :type="param.type" v-model="p[param.prop]"  show-password v-if="param.type == 'password'"></el-input>
                        <el-input :type="param.type" v-model="p[param.prop]" v-if="param.type != 'password'"></el-input>
                      </el-form-item>
                    </el-form>
                  </td>
                  <td style="vertical-align: center;">
                    <RSButton type="del" @click="delTestProtocol($index)"></RSButton>
                  </td>
                </tr>
              </table>
            </el-form-item>

          </el-form>
          <div class="demo-drawer__footer">
            <el-button @click="editDialogVisible = false">{{ $t('cancel') }}</el-button>
            <el-button type="primary" @click="confirmEdit" :loading="loading">{{
                loading ? $t('submitting') +
                    '...' : $t('confirm')
              }}
            </el-button>
          </div>
        </div>
      </el-drawer>

    </div>
  </div>
</template>

<script>

import HttpUtil from "../../common/utils/HttpUtil"
import {ipValidator, requiredValidator, maskValidator} from "@/common/validator/CommonValidator";
import {WebSocketUtil} from "@/common/utils/WebSocket";
import {checkMask, humpToLine} from "@/common/utils/CommonUtil";


let _ = require('lodash');
export default {
  data() {
    return {
      websocket: null,
      protocols: [
        {
          "name": "SNMP",
          params: [
            {
              "type": "text",
              "prop": "community",
              "label": "community",
            },
            {
              "type": "number",
              "prop": "port",
              "label": "port",
            },
          ]
        },
      ],
      activeName: 'switch-discovery',
      rules: {
        name: [
          {validator: requiredValidator, trigger: 'blur', vue: this},
        ],
        startIp: [
          {validator: ipValidator, trigger: 'blur', vue: this},
        ],
        endIp: [
          {validator: ipValidator, trigger: 'blur', vue: this},
        ],
        mask: [
          {validator: maskValidator, trigger: 'blur', vue: this},
        ],
      },
      protocolRules: {
        userName: [
          {validator: requiredValidator, trigger: 'blur', vue: this},
        ],
        pwd: [
          {validator: requiredValidator, trigger: 'blur', vue: this},
        ],
        community: [
          {validator: requiredValidator, trigger: 'blur', vue: this},
        ],
        port: [
          {validator: requiredValidator, trigger: 'blur', vue: this},
        ],
      },
      queryVO: {
        searchKey: null
      },
      query: {
        name: '',
        pageIndex: 1,
        pageSize: 10
      },
      tableData: [],
      loadingList: [],
      multipleSelection: [],
      delList: [],
      editVisible: false,
      labelPosition: 'top',
      pageTotal: 0,
      idx: -1,
      id: -1,
      loading: false,
      columns: [
        {
          label: 'name',
          prop: "name",
          dbProp: "name",
          sort: false
        },
        {
          label: 'start_ip',
          prop: "start_ip",
          dbProp: "startIp",
          sort: false
        },
        {
          label: 'end_ip',
          prop: "end_ip",
          dbProp: "endIp",
          sort: false
        },
        {
          label: 'mask',
          prop: "mask",
          dbProp: "mask",
        }
      ],
      editDialogVisible: false,
      editType: 'edit',
      editObj: {
        name: null,
      },
      allOs: [],
      allOsVersion: [],
      allEndPoints: [],
      fileName: null,
    };
  },
  computed: {
    action: function () {
      return '/switch-discovery/upload?fileName=' + this.fileName;
    }
  },
  components: {},
  mounted() {
    if (!this.websocket) {
      this.websocket = new WebSocketUtil();
      this.websocket.openSocket('switch-discovery', this.notify);
      this.getData();
    }
  },
  destroyed() {
    if (this.websocket) {
      this.websocket.close();
    }
  },
  methods: {
    sortChange(val) {
      if (val.order) {
        this.queryVO.sort = humpToLine(val.prop) + " " + val.order.replace("ending", "");
      } else {
        delete this.queryVO.sort;
      }
      this.getData();
    },
    notify(msg) {
      this.getData();
      this.$notify({
        title: this.$t('server_message'),
        message: this.$t('scan_over'),
      });
    },
    showMachine(rule) {
      if (rule.number) {
        this.$emit('queryByRuleId', rule.id);
        this.back();
      }
    },
    back() {
      this.$emit('back');
      if (this.websocket) {
        this.websocket.close();
      }
    },
    delTestProtocol(index) {
      this.editObj.credentialParam.splice(index, 1);
    },
    addTestProtocol() {
      if (!this.editObj.credentialParam)
        this.editObj.credentialParam = [];
      this.editObj.credentialParam.push(
          {
            "protocol": "SNMP",
          }
      );
    },
    findProtocol(p) {
      if (p.protocol) {
        return _.find(this.protocols, o => o.name == p.protocol).params;
      } else {
        return [];
      }
    },
    // 获取 easy-mock 的模拟数据
    getData() {
      this.loadingList = true;
      HttpUtil.post("/switch-discovery/list/" + this.query.pageIndex + "/" + this.query.pageSize, this.queryVO, (res) => {
        this.tableData = res.data.listObject;
        this.pageTotal = res.data.itemCount;
        this.loadingList = false;
      });
    },
    refreshChildData() {
      this.$refs.device.getData();
    },
    handleSizeChange(val) {
      this.query.pageSize = val;
      this.handlePageChange(this.query.pageIndex);
    },
    handleClose() {
      this.editDialogVisible = false;
    },
    add() {
      this.editDialogVisible = true;
      this.editType = 'add';
    },
    confirmEdit() {
      this.validateResult = true;
      this.$refs.form.validate((f) => {
        if (!f) {
          this.validateResult = false;
        }
      });

      if (this.$refs.protocolForm)
        this.$refs.protocolForm.forEach(
            form => {
              form.validate(f => {
                    if (!f) {
                      this.validateResult = false;
                    }
                  }
              )
            }
        );
      if (
          !this.validateResult
      ) {
        this.$message.error(this.$t('validate_error'));
        return;
      }
      let param = JSON.parse(JSON.stringify(this.editObj));
      if (!checkMask(param.mask)) {
        this.$message.error(this.$t('netmask_validate_error'));
        return;
      }
      param.credentialParam = JSON.stringify(this.editObj.credentialParam);
      this.loading = true;
      if (this.editType == 'edit') {
        HttpUtil.put("/switch-discovery/update", param, (res) => {
          this.editDialogVisible = false;
          this.$message.success(this.$t('edit_success'));
          this.getData();
          this.loading = false;
        })
      } else {
        HttpUtil.post("/switch-discovery/add", param, (res) => {
          this.editDialogVisible = false;
          this.$message.success(this.$t('add_success'));
          this.getData();
          this.loading = false;
        })
      }
    },
// 多选操作
    handleSelectionChange(val) {
      this.multipleSelection = val;
    }
    ,
    getSelectedIds: function () {
      this.delList = [].concat(this.multipleSelection);
      let ids = _.map(this.delList, (item) => item.id);
      return ids;
    }
    ,
    delAllSelection() {
      let ids = this.getSelectedIds();
      if (!ids || ids.length == 0) {
        this.$message.error(this.$t('pls_select_switch-discovery') + "!");
        return;
      }
      this.$confirm(this.$t('confirm_to_del'), this.$t('tips'), {
        type: 'warning'
      }).then(() => {
        HttpUtil.post("/switch-discovery/del", ids, (res) => {
          if (res.success) {
            this.$message.success(this.$t('delete_success'));
            this.getData();
          } else {
            this.$message.success(this.$t('delete_fail'));
          }
        });
        this.multipleSelection = [];
      });
    }
    ,
// 编辑操作
    handleEdit(row, type) {
      if (type == 'edit') {
        this.editDialogVisible = true;
        this.editType = type;
        this.editObj = JSON.parse(JSON.stringify(row));
        this.editObj.credentialParam = this.editObj.credentialParam ? JSON.parse(this.editObj.credentialParam) : [];
        this.resetFields();
      } else if (type == 'del') {
        this.$confirm(this.$t('confirm_to_del'), this.$t('tips'), {
          type: 'warning'
        }).then(() => {
          HttpUtil.delete("/switch-discovery/del/" + row.id, {}, (res) => {
            this.getData();
            this.$message.success(this.$t('delete_success!'));
          });
        })
      } else if (type == 'sync') {
        HttpUtil.post("/switch-discovery/sync",
            [row.id]
            , (res) => {
              this.$message.success(this.$t('opt_success'));
              this.getData();
            });
      } else {
        this.editDialogVisible = true;
        this.editType = type;
        let mainEndPoint = _.find(this.allEndPoints, e => e.type == 'main_endpoint');
        this.resetFields();
        this.editObj = {
          endpointId: mainEndPoint ? mainEndPoint.id : null,
          url: null,
          config: true,
          credentialParam: [
            {
              "protocol": "SNMP"
            }
          ]
        };
      }
    }
    ,
    resetFields() {
      if (this.$refs.form) {
        this.$refs.form.resetFields();
      }
    },
// 分页导航
    handlePageChange(val) {
      this.$set(this.query, 'pageIndex', val);
      this.getData();
    }
    ,
  }
}
</script>

<style scoped>
.handle-box {
  margin-bottom: 20px;
}

.table {
  width: 100%;
  font-size: 14px;
}

.mr10 {
  margin-right: 10px;
}

.container-switch-discovery {
  background: #fff;
  width: 100%;
  height: calc(100vh - 125px);
}


.detail-info {
  border: 1px solid #EBEEF5;
  border-spacing: 0px !important;
  width: 100%;
}

.test-protocol td {
  border: 1px solid #EBEEF5;
  border-bottom: none;
  border-right: none;
  padding: 12px 10px;
  min-width: 0;
  box-sizing: border-box;
  text-overflow: ellipsis;
  vertical-align: middle;
  position: relative;
  text-align: center;
}

.test-protocol {
  border: 1px solid #EBEEF5;
  border-spacing: 0px !important;
  width: 100%;
}

</style>