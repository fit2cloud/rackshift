<template>
  <el-tabs class="t100vw" v-model="activeName" @tab-click="refreshChildData">
    <el-tab-pane :label="$t('Plugin')" name="task">
      <div class="container-task">

        <div class="machine-title">
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

          <el-table-column prop="name" :label="$t('name')" align="left">
            <template slot-scope="scope">
              <span class="rs-nowrap">{{ scope.row.name }}</span>
            </template>
          </el-table-column>

          <el-table-column prop="platform" :label="$t('platform')" align="left" width="210">
            <template slot-scope="scope">
              <span class="rs-nowrap">{{ scope.row.platform }}</span>
            </template>
          </el-table-column>

          <el-table-column prop="image" :label="$t('image')" align="left" width="210">
            <template slot-scope="scope">
              <span class="rs-nowrap">{{ scope.row.image }}</span>
            </template>
          </el-table-column>

          <el-table-column prop="platform" :label="$t('base_instruction')" align="left" width="210">
            <template slot-scope="scope">
              <span class="rs-nowrap">{{ scope.row.baseInstruction }}</span>
            </template>
          </el-table-column>

          <el-table-column prop="createTime" :label="$t('create_time')" align="left">
            <template slot-scope="scope">
              <span class="rs-nowrap">{{ scope.row.createTime | dateFormat }}</span>
            </template>
          </el-table-column>

          <el-table-column prop="" :label="$t('opt')" align="left">
            <template slot-scope="scope">
              <RSButton @click="handleEdit(scope.row, 'view')" type="run" :tip="$t('execute_instruction')"></RSButton>
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
            :title="$t('view_plugin')"
            :visible.sync="editDialogVisible"
            direction="rtl"
            size="50%"
            :wrapperClosable="false"
            :before-close="handleClose">
          <div class="demo-drawer__content">
            <el-form label-position="top">
              <el-form-item label-width="80px" :label="$t('base_instruction')">
                <el-input type="textarea" v-model="editObj.baseInstruction">
                </el-input>
              </el-form-item>
            </el-form>
            <div class="demo-drawer__footer">
              <el-button @click="handleClose">{{ $t('close') }}</el-button>
            </div>
          </div>
        </el-drawer>


        <el-drawer
            :title="editType == 'edit' ? $t('edit_plugin') : $t('add_plugin')"
            :visible.sync="instructionVisible"
            direction="rtl"
            size="100%"
            :with-header="false"
            :wrapperClosable="false"

            :before-close="handleClose">
          <Instruction @back="back" class="drawer-full" :pluginId="currentPluginId"></Instruction>
        </el-drawer>

      </div>
    </el-tab-pane>
  </el-tabs>
</template>

<script>

import HttpUtil from "../../common/utils/HttpUtil"
import {ipValidator, requiredValidator} from "@/common/validator/CommonValidator";
import Instruction from "@/components/instruction/Instruction";

let _ = require('lodash');
export default {
  data() {
    return {
      currentPluginId: null,
      instructionVisible: false,
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
          sort: false
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
      return '/plugin/upload?fileName=' + this.fileName;
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
  mounted() {
    this.getData();
  },
  components: {
    Instruction
  },
  methods: {
    back() {
      this.instructionVisible = false;
    },
    // 获取 easy-mock 的模拟数据
    getData(callback) {
      this.loadingList = true;
      HttpUtil.post("/plugin/list/" + this.query.pageIndex + "/" + this.query.pageSize, this.queryVO, (res) => {
        let that = this;
        that.tableData = res.data.listObject;
        that.pageTotal = res.data.itemCount;
        that.loadingList = false;
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
      this.instructionVisible = false;
      this.editDialogVisible = false;
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
        this.$message.error(this.$t('pls_select_plugin') + "!");
        return;
      }
      this.$confirm(this.$t('confirm_to_del'), this.$t('tips'), {
        type: 'warning'
      }).then(() => {
        HttpUtil.post("/plugin/del", ids, (res) => {
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
          HttpUtil.get("/plugin/del/" + row.id, {}, (res) => {
            this.getData();
            this.$message.success(this.$t('delete_success!'));
          });
        })
      } else if (type == 'view') {
        this.editObj = JSON.parse(JSON.stringify(row));
        this.currentPluginId = this.editObj.id;
        this.instructionVisible = true;
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