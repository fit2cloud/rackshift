<template>
  <div>
    <el-button class="el-button el-button--default el-button--mini is-plain pointer" @click="back"><i
        class="el-icon-back"></i>&nbsp;&nbsp;&nbsp;&nbsp;{{
        $t('back')
      }}
    </el-button>
    <el-divider content-position="left">{{ $t('Instruction') }}</el-divider>
    <div class="container-instruction">

      <div class="machine-title">
        <el-button-group class="batch-button">
          <el-button type="primary" icon="el-icon-circle-plus-outline" @click="handleEdit(null, 'add')">{{
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
          @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" align="left"></el-table-column>

        <el-table-column :prop="c.prop" :label="$t(c.label)" align="left"
                         v-for="c in columns" :sortable="c.sort"></el-table-column>

        <el-table-column prop="createTime" :label="$t('create_time')" align="left">
          <template slot-scope="scope">
            {{ scope.row.createTime | dateFormat }}
          </template>
        </el-table-column>

        <el-table-column prop="" :label="$t('opt')" align="left">
          <template slot-scope="scope">
            <RSButton @click="handleEdit(scope.row, 'edit')"></RSButton>
            <RSButton @click="handleEdit(scope.row, 'del')" type="del"></RSButton>
            <RSButton @click="handleEdit(scope.row, 'run')" type="run" :tip="$t('Run')"></RSButton>
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
          :title="editType == 'edit' ? $t('edit_instruction') : $t('add_instruction')"
          :visible.sync="editDialogVisible"
          direction="rtl"
          :modal=true
          :wrapperClosable="false"
          :appendToBody=true
          :before-close="handleClose">
        <div class="demo-drawer__content">
          <el-form :model="editObj" :rules="rules" ref="form" label-width="50px" :label-position="labelPosition">
            <el-form-item :label="$t('name')" prop="name">
              <el-input type="text" v-model="editObj.name" autocomplete="off"></el-input>
            </el-form-item>

            <el-form-item :label="$t('instruction_content')" prop="content">
              <el-input type="textarea" v-model="editObj.content" autocomplete="off"
                        placeholder="例如：sel clear = ipmitool -I lanplus -H xxx -U xxx -P xxx sel clear,多条命令请换行"></el-input>
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

      <!--run commands-->
      <el-dialog :title="$t('Run')" :visible.sync="runInstruction" width="85vw" :close-on-click-modal="false"
                 :appendToBody=true>
        <div>
          <el-row>
            <el-col :span="12">
              <div class="instruction-title">{{ $t('select_machine') }}</div>
              <el-table
                  :data="bareMetalData"
                  class="table"
                  ref="bareMetalMultipleTable"
                  v-loading="loadingBareMetalList"
                  header-cell-class-name="table-header"
                  style="width: 100%"
                  @sort-change="sortChange($event)"
                  @selection-change="handleMetalSelectionChange"
              >
                <el-table-column type="selection" align="left"></el-table-column>

                <el-table-column prop="machine_model" :label="$t('machine_model')" align="left"
                                 sortable="custom" style="overflow: scroll" width="180">
                  <template slot-scope="scope">
                    <span class="rs-nowrap">{{ scope.row.machineModel }}</span>
                  </template>
                </el-table-column>

                <el-table-column prop="machine_sn" :label="$t('machine_sn')" align="left"
                                 sortable="custom">
                  <template slot-scope="scope">
                    {{ scope.row.machineSn }}
                  </template>
                </el-table-column>

                <el-table-column prop="management_ip" :label="$t('management_ip')" align="left"
                                 sortable="custom">
                  <template slot-scope="scope">
                    {{ scope.row.managementIp }}
                  </template>
                </el-table-column>

                <el-table-column prop="" :label="$t('opt')" align="left">
                  <template slot="header" slot-scope="scope">
                    <el-input
                        v-model="queryVO.searchKey"
                        size="mini"
                        :placeholder="$t('input_key_search')" v-on:change="getBareMetalData()"/>
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
            </el-col>

            <el-col :span="12">
              <div class="instruction-title">{{ $t('logs') }}</div>
              <el-button-group class="batch-button">
                <el-button type="primary" icon="el-icon-delete" @click="delAllSelectionLog">{{ $t('del') }}
                </el-button>
                <el-button type="primary" icon="el-icon-refresh" @click="getLogs">{{ $t('refresh') }}</el-button>
              </el-button-group>

              <div class="instruction-logs">
                <el-table
                    :data="logs"
                    class="table"
                    ref="multipleLogTable"
                    v-loading="loadingLogList"
                    header-cell-class-name="table-header"
                    style="width: 100%"
                    v-if="logs.length"
                    @selection-change="handleLogSelectionChange"
                >
                  <el-table-column type="selection" align="left"></el-table-column>
                  <el-table-column prop="createTime" :label="$t('create_time')" align="left"
                                   sortable="custom">
                    <template slot-scope="scope">
                      {{ scope.row.createTime | dateFormat }}
                    </template>
                  </el-table-column>

                  <el-table-column prop="content" :label="$t('output')" align="left"
                                   sortable="custom">
                    <template slot-scope="scope">
                      {{ scope.row.content }}
                    </template>
                  </el-table-column>

                </el-table>

                <span v-else class="mb10">{{ $t('no_more_logs') }}</span>
              </div>

            </el-col>
          </el-row>
        </div>

        <template v-slot:footer>
          <div class="dialog-footer">
            <el-button @click="runInstruction = false">{{ $t('close') }}</el-button>
            <el-button type="primary" @click="runCommands" :loading="runLoading">{{ $t('Run') }}</el-button>
          </div>
        </template>
      </el-dialog>
      <!---->

    </div>
  </div>
</template>

<script>import HttpUtil from "../../common/utils/HttpUtil"
import {requiredValidator} from "@/common/validator/CommonValidator";
import PowerStatus from "@/common/powerstatus/Power-Status";

let _ = require('lodash');
export default {
  props: ['pluginId'],
  data() {
    return {
      loadingLogList: false,
      runLoading: false,
      loadingBareMetalList: false,
      bareMetalColumns: [
        {
          label: 'CPU',
          prop: "cpu"
        },
        {
          label: 'memory',
          prop: "memory",
          expandLanguage: "en_US",
          custom: true,
          formatter: function (item) {
            if (item) {
              return parseInt(item) + ' GB'
            }
            return item;
          }
        },
        {
          label: 'disk',
          prop: "disk",
          custom: true,
          formatter: function (item) {
            if (item) {
              return parseInt(item) / 1000 + ' TB'
            }
            return item;
          }
        },
      ],
      runInstruction: false,
      formLabelWidth: "80px",
      activeName: 'instruction',
      rules: {
        name: [
          {validator: requiredValidator, trigger: 'blur', vue: this},
        ],
        content: [
          {validator: requiredValidator, trigger: 'blur', vue: this},
        ],
      },
      query: {
        name: '',
        pageIndex: 1,
        pageSize: 10
      },
      bareMetalQuery: {
        name: '',
        pageIndex: 1,
        pageSize: 10
      },
      queryVO: {
        searchKey: null
      },
      tableData: [],
      loadingList: [],
      multipleSelection: [],
      multipleMetalSelection: [],
      multipleLogSelection: [],
      delList: [],
      editVisible: false,
      customProtocol: false,
      labelPosition: 'top',
      pageTotal: 0,
      bareMetalpageTotal: 0,
      idx: -1,
      id: -1,
      loading: false,
      columns: [
        {
          label: 'name',
          prop: "name",
          sort: true
        },
        {
          label: 'instruction_content',
          prop: "content",
          sort: true
        },
      ],
      baremetalColumns: [
        {
          label: 'name',
          prop: "name",
          sort: true
        },
        {
          label: 'instruction_content',
          prop: "content",
          sort: true
        },
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
      curObj: {},
      bareMetalData: [],
      logs: [],
    };
  },
  components: {PowerStatus},
  mounted() {
    this.getData();
  },
  watch: {
    pluginId() {
      this.getData();
    }
  },
  methods: {
    statusFilter(row) {
      if (row.status.indexOf("ing") == -1) {
        if (row.serverId)
          return '<span style="display: inline-block;white-space: nowrap;">' +
              this.$t('PXE') + ' ' + this.$t(row.status) + '<i class="el-icon-check" style="color:#55BA23;margin-left:5px;"></i><br>'
              + this.$t('OBM') + ' ' + this.$t('info') + (row.outBandList.length > 0 ? '<i class="el-icon-check" style="color:#55BA23;margin-left:5px;"></i>' : '<i class="el-icon-close" style="margin-left:5px;color: red;"></i>') + '</span>';
        else
          return '<span style="display: inline-block;white-space: nowrap;">' +
              this.$t('PXE') + ' ' + this.$t('status') + '<i class="el-icon-close" style="margin-left:5px;color: red;"></i><br>'
              + this.$t('OBM') + ' ' + this.$t('info') + (row.outBandList.length > 0 ? '<i class="el-icon-check" style="color:#55BA23;margin-left:5px;"></i>' : '<i class="el-icon-close" style="margin-left:5px;color: red;"></i>') + '</span>';
      } else {
        return '<span style="display: inline-block;white-space: nowrap;">' +
            this.$t('PXE') + ' ' + this.$t(row.status) + '<br>';
      }
    },
    filterPower(tag, row) {
      return row.power == tag;
    },
    runCommands() {
      if (!this.getBareMetalSelectedIds().length) {
        this.$message(this.$t('pls_select') + this.$t('Bare Metal Server'));
        return;
      }
      let req = {
        bareMetalIds: this.getBareMetalSelectedIds(),
        id: this.editObj.id
      }
      this.runLoading = true;
      HttpUtil.post("instruction/runCommands", req, (res) => {
        if (res.data) {
          this.$message.success(this.$t('opt_success'));
        }
        this.getLogs();
        this.runLoading = false;
      }, (error) => {
        this.$message.error(this.$t('opt_fail'));
        this.getLogs();
        this.runLoading = false;
      });
    },
    getLogs() {
      this.loadingLogList = true;
      HttpUtil.get("instruction/logs?id=" + this.editObj.id, null, (res) => {
        this.logs = res.data;
        this.loadingLogList = false;
      })
    },
    resizeWith(c) {
      return (c.expandLanguage && c.expandLanguage == localStorage.getItem('lang')) ? '100px' : '90px';
    },
    handleChange(value, direction, movedKeys) {
      console.log(value, direction, movedKeys);
    },
    showMachine(rule) {
      if (rule.number) {
        this.$emit('queryByRuleId', rule.id);
        this.back();
      }
    },
    back() {
      this.$emit('back');
    },
    // 获取 easy-mock 的模拟数据
    getData() {
      this.loadingList = true;
      HttpUtil.post("/instruction/list/" + this.query.pageIndex + "/" + this.query.pageSize, {pluginId: this.pluginId}, (res) => {
        this.tableData = res.data.listObject;
        this.pageTotal = res.data.itemCount;
        this.loadingList = false;
      });
    },
    getBareMetalData() {
      this.loadingBareMetalList = true;
      HttpUtil.post("/bare-metal/list/" + this.bareMetalQuery.pageIndex + "/" + this.bareMetalQuery.pageSize, this.queryVO, (res) => {
        this.bareMetalData = res.data.listObject;
        this.bareMetalpageTotal = res.data.itemCount;
        this.loadingBareMetalList = false;
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
    confirmEdit() {
      this.validateResult = true;
      this.$refs.form.validate((f) => {
        if (!f) {
          this.validateResult = false;
        }
      });
      if (!this.validateResult) {
        return;
      }
      this.loading = true;
      let param = JSON.parse(JSON.stringify(this.editObj));
      param.pluginId = this.pluginId;
      if (this.editType == 'edit') {
        HttpUtil.post("/instruction/update", param, (res) => {
          this.editDialogVisible = false;
          this.$message.success(this.$t('edit_success'));
          this.getData();
          this.loading = false;
        })
      } else {
        HttpUtil.post("/instruction/add", param, (res) => {
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
    handleLogSelectionChange(val) {
      this.multipleLogSelection = val;
    }
    ,
    handleMetalSelectionChange(val) {
      this.multipleMetalSelection = val;
    }
    ,
    getSelectedIds: function () {
      this.delList = [].concat(this.multipleSelection);
      let ids = _.map(this.delList, (item) => item.id);
      return ids;
    }
    , getSelectedLogIds: function () {
      this.delList = [].concat(this.multipleLogSelection);
      let ids = _.map(this.delList, (item) => item.id);
      return ids;
    }
    ,
    getBareMetalSelectedIds: function () {
      let delList = [].concat(this.multipleMetalSelection);
      let ids = _.map(delList, (item) => item.id);
      return ids;
    }
    ,
    delAllSelection() {
      let ids = this.getSelectedIds();
      if (!ids || ids.length == 0) {
        this.$message.error(this.$t('pls_select_instruction') + "!");
        return;
      }
      this.$confirm(this.$t('confirm_to_del'), this.$t('tips'), {
        type: 'warning'
      }).then(() => {
        HttpUtil.post("/instruction/del", ids, (res) => {
          if (res.success) {
            this.$message.success(this.$t('delete_success'));
            this.getData();
          } else {
            this.$message.success(this.$t('delete_fail'));
          }
        });
        this.multipleSelection = [];
      });
    },
    delAllSelectionLog() {
      let ids = this.getSelectedLogIds();
      if (!ids || ids.length == 0) {
        this.$message.error(this.$t('pls_select_log') + "!");
        return;
      }
      this.$confirm(this.$t('confirm_to_del'), this.$t('tips'), {
        type: 'warning'
      }).then(() => {
        HttpUtil.post("/instruction/delLog", ids, (res) => {
          if (res.success) {
            this.$message.success(this.$t('delete_success'));
            this.getLogs();
          } else {
            this.$message.success(this.$t('delete_fail'));
          }
        });
        this.multipleLogSelection = [];
      });
    }
    ,
// 编辑操作
    handleEdit(row, type) {
      if (type == 'edit') {
        this.editDialogVisible = true;
        this.editType = type;
        this.editObj = JSON.parse(JSON.stringify(row));
        this.resetFields();
      } else if (type == 'del') {
        this.$confirm(this.$t('confirm_to_del'), this.$t('tips'), {
          type: 'warning'
        }).then(() => {
          HttpUtil.get("/instruction/del/" + row.id, {}, (res) => {
            this.getData();
            this.$message.success(this.$t('delete_success!'));
          });
        })
      } else if (type == 'run') {
        this.runInstruction = true;
        this.editObj = JSON.parse(JSON.stringify(row));
        HttpUtil.post("/bare-metal/list/" + this.bareMetalQuery.pageIndex + "/" + this.bareMetalQuery.pageSize, this.queryVO, (res) => {
          this.bareMetalData = res.data.listObject;
          this.bareMetalpageTotal = res.data.itemCount;
        });

        this.getLogs();
      } else {
        this.editDialogVisible = true;
        this.editType = type;
        this.editObj = {};
        this.resetFields();
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

.container-instruction {
  background: #fff;
  width: 100%;
  height: calc(100vh - 125px);
}


.detail-info {
  border: 1px solid #EBEEF5;
  border-spacing: 0px !important;
  width: 100%;
  /*table-layout: fixed;*/
  word-break: break-all
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

.instruction-title {
  margin-bottom: 10px;
  font-size: 30px;
  white-space: nowrap;
  text-align: left;
}

.instruction-logs {
  height: 500px;
  overflow-y: scroll;
}

.nowrap {
  width: 25%
}
</style>