<template>
  <el-tabs class="t100vw" v-model="activeName" @tab-click="refreshChildData">
    <el-tab-pane :label="$t('Task')" name="task">
      <div class="container-task">

        <div class="machine-title">
          <el-button-group class="batch-button">
            <el-button type="primary" icon="el-icon-delete" @click="delAllSelection">{{ $t('del') }}
            </el-button>
            <el-button type="primary" icon="el-icon-refresh" @click="cancel()">{{ $t('cancel') }}</el-button>
            <el-button type="primary" icon="el-icon-refresh" @click="getData()">{{ $t('refresh') }}</el-button>
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
            @sort-change="sortChange($event)"
            type=selection
            :reserve-selection="true"
            :row-key="row => row.id"
            highlight-current-row
        >
          <el-table-column type="selection" :reserve-selection="true" align="left"></el-table-column>


          <el-table-column prop="id" :label="$t('id')" align="left" sortable="custom">
            <template slot-scope="scope">
              <el-tooltip class="item" effect="dark" :content="scope.row.id" placement="right-end">
                <span class="rs-nowrap">{{ scope.row.id }}</span>
              </el-tooltip>
            </template>
          </el-table-column>

          <el-table-column prop="machineModel" :label="$t('Bare Metal Server')" align="left" sortable="custom">
            <template slot-scope="scope">
              <span class="rs-nowrap">{{ scope.row.machineModel }}</span>
            </template>
          </el-table-column>

          <el-table-column prop="machineSn" :label="$t('machine_sn')" align="left" sortable="custom">
            <template slot-scope="scope">
              <span class="rs-nowrap">{{ scope.row.machineSn }}</span>
            </template>
          </el-table-column>

          <el-table-column prop="friendlyName" :label="$t('Workflow')" align="left" sortable="custom" width="210">
            <template slot-scope="scope">
              <span class="rs-nowrap">{{ $t(scope.row.friendlyName) }}</span>
            </template>
          </el-table-column>

          <el-table-column prop="userId" :label="$t('user')" align="left" sortable="custom">
          </el-table-column>

          <el-table-column prop="params" :label="$t('param')" align="left">
            <template slot-scope="scope">
              <el-link @click="viewParam(scope.row.params)">查看</el-link>
            </template>
          </el-table-column>

          <el-table-column prop="status" :label="$t('status')" align="left">
            <template slot-scope="scope">
              <i class="el-icon-loading" v-if="scope.row.status.indexOf('ing') != -1"></i>
              <span>{{ $t(scope.row.status) }}</span>
              <span class="percent" v-if="scope.row.status.indexOf('ing') != -1">{{
                  getSuccessRate(scope.row)
                }}</span>
            </template>
          </el-table-column>

          <el-table-column prop="createTime" :label="$t('create_time')" align="left" sortable="custom">
            <template slot-scope="scope">
              <span class="rs-nowrap">{{ scope.row.createTime | dateFormat }}</span>
            </template>
          </el-table-column>

          <el-table-column prop="" :label="$t('opt')" align="left">
            <template slot="header" slot-scope="scope">
              <el-input
                  v-model="queryVO.searchKey"
                  size="mini"
                  :placeholder="$t('input_key_search')" v-on:change="getData()"/>
            </template>
            <template slot-scope="scope">
              <RSButton @click="handleEdit(scope.row, 'view')" type="view" :tip="$t('view_log')"></RSButton>
              <RSButton @click="handleEdit(scope.row, 'del')" type="del"></RSButton>
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
            :title="$t('view_log')"
            :visible.sync="editDialogVisible"
            direction="rtl"
            size="50%"
            :wrapperClosable="false"
            :before-close="handleClose">
          <div class="demo-drawer__content">
            <table class="detail-info log" v-if="logs.length">
              <tr>
                <td>{{ $t('create_time') }}</td>
                <td>{{ $t('output') }}</td>
              </tr>

              <tr v-for="(l, index) in logs">
                <td class="nowrap">{{ l.createTime | dateFormat }}</td>
                <td>{{ l.outPut }}</td>
              </tr>

              <tr>
                <td><i v-if="editObj.status == 'running'" class="el-icon-loading"></i></td>
              </tr>
            </table>

            <span v-else class="mb10">{{ $t('no_more_logs') }}</span>
            <div class="demo-drawer__footer">
              <el-button @click="handleClose">{{ $t('close') }}</el-button>
            </div>
          </div>
        </el-drawer>


        <el-drawer
            :title="$t('view_log')"
            :visible.sync="editDialogVisible2"
            direction="rtl"
            size="50%"
            :wrapperClosable="false"
            :before-close="handleClose2">
          <div class="demo-drawer__content">
            <json-viewer v-if="editObj.graphObjects" :value="editObj.graphObjects" :expand-depth=2
                         :copyable="{copyText: $t('copy'), copiedText: $t('copied'), timeout: 2000}"></json-viewer>

            <span v-else class="mb10">{{ $t('no_more_logs') }}</span>

            <!--                <workflow-chart-->
            <!--                    :transitions="transitions"-->
            <!--                    :states="states" />-->


            <div class="demo-drawer__footer">
              <el-button @click="handleClose2">{{ $t('close') }}</el-button>
            </div>
          </div>
        </el-drawer>

        <el-dialog
            :title="$t('param')"
            :append-to-body="true"
            :visible.sync="paramVisable" class="about-us">
          <json-viewer :value="jsonData" :expand-depth=6
                       :copyable="{copyText: $t('copy'), copiedText: $t('copied'), timeout: 2000}"></json-viewer>
        </el-dialog>
      </div>
    </el-tab-pane>
  </el-tabs>
</template>

<script>

import HttpUtil from "../../common/utils/HttpUtil"
import {ipValidator, requiredValidator} from "@/common/validator/CommonValidator";
import {humpToLine} from "@/common/utils/CommonUtil"
import JsonViewer from 'vue-json-viewer'
import Vue from 'vue'
// Import JsonViewer as a Vue.js plugin
import WorkflowChart from 'vue-workflow-chart';

Vue.use(JsonViewer)
let _ = require('lodash');
export default {
  data() {
    return {
      states: [{
        "id": "state_1",
        "label": "State 1",
      }, {
        "id": "state_2",
        "label": "State 2",
      }, {
        "id": "state_3",
        "label": "State 3",
      }, {
        "id": "state_4",
        "label": "State 4",
      }, {
        "id": "state_5",
        "label": "State 5",
      }, {
        "id": "state_6",
        "label": "State 6",
      }],
      transitions: [{
        "id": "transition_1",
        "label": "this is a transition",
        "target": "state_2",
        "source": "state_1",
      }, {
        "id": "transition_1",
        "label": "this is a transition",
        "target": "state_3",
        "source": "state_2",
      }, {
        "id": "transition_1",
        "label": "this is a transition",
        "target": "state_4",
        "source": "state_3",
      }, {
        "id": "transition_1",
        "label": "this is a transition",
        "target": "state_5",
        "source": "state_4",
      }, {
        "id": "transition_1",
        "label": "this is a transition",
        "target": "state_6",
        "source": "state_5",
      },],
      editDialogVisible2: false,
      paramVisable: false,
      jsonData: {},
      queryVO: {},
      refreshOne: false,
      refreshSubTaskPointer: null,
      refreshTaskPointer: null,
      logs: [],
      activeName: 'task',
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
          {validator: ipValidator, trigger: 'blur', vue: this},
        ],
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
          label: 'machine_sn',
          prop: "machineSn",
          sort: true
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
      websocket: null
    };
  },
  computed: {
    action: function () {
      return '/task/upload?fileName=' + this.fileName;
    }
  },
  destroyed() {
    if (this.websocket) {
      this.websocket.close();
    }
    if (this.refreshTaskPointer) {
      clearInterval(this.refreshTaskPointer);
    }
    if (this.refreshSubTaskPointer) {
      clearInterval(this.refreshSubTaskPointer);
    }
  },
  components: {
    WorkflowChart
  },
  mounted() {
    this.getData();
  },
  methods: {
    getSuccessRate(row) {
      let graph = JSON.parse(row.graphObjects);
      let suc = [];
      _.forIn(graph, function (value, key) {
        if (value.state == "succeeded")
          suc.push(value);
      });
      return suc.length + '/' + Object.keys(graph).length
    },
    copy(e) {
      e.preventDefault();
      var content = JSON.stringify(this.jsonData);
      let oInput = document.createElement('input')
      oInput.value = content;
      document.body.appendChild(oInput);
      oInput.select();
      document.execCommand("Copy");
      oInput.style.display = 'none';
      document.body.removeChild(oInput);
      this.$message.success(this.$t('copy_success'));
    },
    close() {
      this.paramVisable = false;
    },
    viewParam(param) {
      if (!param) {
        this.$message.info(this.$t("no_param"));
        return;
      }
      this.jsonData = JSON.parse(param);
      this.paramVisable = true;
    },
    sortChange(val) {
      if (val.order) {
        this.queryVO.sort = humpToLine(val.prop) + " " + val.order.replace("ending", "");
      } else {
        delete this.queryVO.sort;
      }
      this.getData();
    },
    cancel() {
      let ids = this.getSelectedIds();
      if (!ids.length) {
        this.$message.error(this.$t('pls_select_') + this.$t('Task') + "!");
        return;
      }
      HttpUtil.post("/task/cancel", ids, (res) => {
        if (res.success) {
          this.$message.success(this.$t('cancel_task_success'));
        } else {
          this.$message.error(this.$t('opt_fail'));
        }
      })
    },
    notify(msg) {
      this.getData();
      this.$notify({
        title: this.$t('server_message'),
        message: msg,
      });
    },
    getLogs() {
      let that = this;
      HttpUtil.get("/task/logs?id=" + that.editObj.id, {}, (res) => {
        that.editDialogVisible = true;
        that.logs = res.data;

        if (that.logs && that.logs.length) {
          let lastRackHDLog = that.logs[that.logs.length - 1];
          HttpUtil.post("/task/list/" + this.query.pageIndex + "/" + this.query.pageSize, {}, (res) => {
            this.tableData = res.data.listObject;
            this.pageTotal = res.data.itemCount;
          });
          if (lastRackHDLog.status == 'START') {
            if (!this.refreshOne) {
              this.refreshOne = true;
              that.getData(function () {
                that.editObj = _.find(that.tableData, (l) => l.id == that.editObj.id);
              });
            }
          }
          if (lastRackHDLog.status == 'END') {
            clearInterval(that.refreshSubTaskPointer);
            that.getData(function () {
              that.editObj = _.find(that.tableData, (l) => l.id == that.editObj.id);
            });
          }
        }
      })
    },
    // 获取 easy-mock 的模拟数据
    getData(callback) {
      this.loadingList = true;
      HttpUtil.post("/task/list/" + this.query.pageIndex + "/" + this.query.pageSize, this.queryVO, (res) => {
        let that = this;
        that.tableData = res.data.listObject;
        that.pageTotal = res.data.itemCount;
        that.loadingList = false;
        if (callback) {
          callback();
        }

        if (_.find(res.data.listObject, (o) => o.status == 'running' || o.status == 'created')) {
          if (!that.refreshTaskPointer) {
            that.refreshTaskPointer = setInterval(that.getDataNoLoading, 3000);
          }
        } else {
          if (that.refreshTaskPointer) {
            clearInterval(that.refreshTaskPointer);
          }
        }
      });
    },
    getDataNoLoading(callback) {
      HttpUtil.post("/task/list/" + this.query.pageIndex + "/" + this.query.pageSize, {}, (res) => {
        this.tableData = res.data.listObject;
        this.pageTotal = res.data.itemCount;
        let that = this;
        if (!_.find(res.data.listObject, (o) => o.status == 'running' || o.status == 'created')) {
          if (that.refreshTaskPointer) {
            clearInterval(that.refreshTaskPointer);
          }
        }

        if (callback) {
          callback();
        }
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
      window.clearInterval(this.refreshSubTaskPointer);
    },
    handleClose2() {
      this.editDialogVisible2 = false;
      window.clearInterval(this.refreshSubTaskPointer);
    },
    add() {
      this.editDialogVisible = true;
      this.editType = 'add';
    },
    confirmEdit() {
    },
    // 多选操作
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    getSelectedIds: function () {
      this.delList = [].concat(this.multipleSelection);
      let ids = _.map(this.delList, (item) => item.id);
      return ids;
    },
    delAllSelection() {
      let ids = this.getSelectedIds();
      if (!ids || ids.length == 0) {
        this.$message.error(this.$t('pls_select_task') + "!");
        return;
      }
      this.$confirm(this.$t('confirm_to_del'), this.$t('tips'), {
        type: 'warning'
      }).then(() => {
        HttpUtil.post("/task/del", ids, (res) => {
          if (res.success) {
            this.$message.success(this.$t('delete_success'));
            this.getData();
          } else {
            this.$message.success(this.$t('delete_fail'));
          }
          this.multipleSelection = [];
        }, (res) => {
          this.$message.error(res);
        });
      }).catch(function (e) {
        this.multipleSelection = [];
      });

    },
    // 编辑操作
    handleEdit(row, type) {
      if (type == 'edit') {
        this.editDialogVisible = true;
        this.editType = type;
        this.editObj = JSON.parse(JSON.stringify(row));
      } else if (type == 'del') {
        this.$confirm(this.$t('confirm_to_del'), this.$t('tips'), {
          type: 'warning'
        }).then(() => {
          HttpUtil.delete("/task/del/" + row.id, {}, (res) => {
            this.getData();
            this.$message.success(this.$t('delete_success!'));
          });
        })
      } else if (type == 'view') {
        this.editObj = JSON.parse(JSON.stringify(row));
        this.editObj.graphObjects = JSON.parse(this.editObj.graphObjects);
        this.editDialogVisible2 = true;
        // const that = this;
        // this.refreshOne = false;
        // HttpUtil.get("/task/logs?id=" + that.editObj.id, {}, (res) => {
        //   that.editDialogVisible2 = true;
        //   that.logs = res.data;
        //   that.refreshSubTaskPointer = setInterval(that.getLogs, 5000);
        // });
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
@import '~vue-workflow-chart/dist/vue-workflow-chart.css';

.vue-workflow-chart-state-delete {
  color: white;
  background: red;
}

.vue-workflow-chart-transition-arrow-delete {
  fill: red;
}

.vue-workflow-chart-transition-path-delete {
  stroke: red;
}

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

.container-task {
  background: #fff;
  width: 100%;
  height: calc(100vh - 125px);
}

.nowrap {
  white-space: nowrap;
}

.mb10 {
  display: block;
  margin-bottom: 10px;
}

.detail-info.log td {
  padding: 5px;
  line-height: 18px;
}

.detail-info.log tr td:first-child {
  vertical-align: top;
}

.percent {
  margin-left: 3px;
}
</style>