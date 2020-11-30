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
        >
          <el-table-column type="selection" align="left"></el-table-column>

          <el-table-column prop="machineModel" :label="$t('Bare Metal Server')" align="left" :sortable="true">
            <template slot-scope="scope">
              <el-tooltip class="item" effect="dark" :content="scope.row.machineModel" placement="right-end">
                <span style="display: block; word-break:keep-all;
  white-space:nowrap;overflow: hidden">{{ scope.row.machineModel }}</span>
              </el-tooltip>
            </template>
          </el-table-column>

          <el-table-column :prop="c.prop" :label="$t(c.label)" align="left"
                           v-for="c in columns" :sortable="c.sort"></el-table-column>

          <el-table-column prop="friendlyName" :label="$t('Workflow')" align="left" :sortable="true" width="210">
            <template slot-scope="scope">
              <el-tooltip class="item" effect="dark" :content="scope.row.friendlyName" placement="right-end">
                <span style="display: block; word-break:keep-all;
  white-space:nowrap;overflow: hidden">{{ scope.row.friendlyName }}</span>
              </el-tooltip>
            </template>
          </el-table-column>

          <el-table-column prop="userId" :label="$t('user')" align="left" :sortable="true">
          </el-table-column>

          <el-table-column prop="status" :label="$t('status')" align="left">
            <template slot-scope="scope">
              <i class="el-icon-loading" v-if="scope.row.status.indexOf('ing') != -1"></i>
              <span style="margin-left: 10px">{{ $t(scope.row.status) }}</span>
            </template>
          </el-table-column>

          <el-table-column prop="createTime" :label="$t('create_time')" align="left">
            <template slot-scope="scope">
              {{ scope.row.createTime | dateFormat }}
            </template>
          </el-table-column>

          <el-table-column prop="" :label="$t('opt')" align="left">
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

      </div>
    </el-tab-pane>
  </el-tabs>
</template>

<script>

import HttpUtil from "../../common/utils/HttpUtil"
import {ipValidator, requiredValidator} from "@/common/validator/CommonValidator";
import {WebSocketUtil} from "@/common/utils/WebSocket";

let _ = require('lodash');
export default {
  data() {
    return {
      refreshOne: false,
      refreshPointer: null,
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
  },
  mounted() {
    if (!this.websocket) {
      this.websocket = new WebSocketUtil();
      this.websocket.openSocket('taskLifecycle', this.notify);
      this.getData();
    }
  },
  methods: {
    cancel() {
      let ids = this.getSelectedIds();
      HttpUtil.post("/task/cancel", ids, (res) => {
        if (res.success) {
          this.$message.success(this.$t('opt_success'));
        } else {
          this.$message.success(this.$t('opt_fail'));
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

          if (lastRackHDLog.status == 'START') {
            if (!this.refreshOne) {
              this.refreshOne = true;
              that.getData(function () {
                that.editObj = _.find(that.tableData, (l) => l.id == that.editObj.id);
              });
            }
          }
          if (lastRackHDLog.status == 'END') {
            clearInterval(that.refreshPointer);
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
      HttpUtil.post("/task/list/" + this.query.pageIndex + "/" + this.query.pageSize, {}, (res) => {
        this.tableData = res.data.listObject;
        this.pageTotal = res.data.itemCount;
        this.loadingList = false;
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
    },
    handleClose() {
      this.editDialogVisible = false;
      window.clearInterval(this.refreshPointer);
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
          HttpUtil.get("/task/del/" + row.id, {}, (res) => {
            this.getData();
            this.$message.success(this.$t('delete_success!'));
          });
        })
      } else if (type == 'view') {
        this.editObj = JSON.parse(JSON.stringify(row));
        const that = this;
        this.refreshOne = false;
        HttpUtil.get("/task/logs?id=" + that.editObj.id, {}, (res) => {
          that.editDialogVisible = true;
          that.logs = res.data;
          that.refreshPointer = setInterval(that.getLogs, 5000);
        });
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

.container-task {
  background: #fff;
  width: 100%;
  height: calc(100vh - 125px);
}

.nowrap {
  white-space: nowrap;
}

.mb10 {
  margin-bottom: 10px;
}

.detail-info.log td {
  padding: 5px;
}

.detail-info.log tr td:first-child {
  vertical-align: top;
}

</style>