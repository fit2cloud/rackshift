<template>
  <el-tabs class="t100vw" v-model="activeName">
    <el-tab-pane :label="$t('endpoint')" name="endpoint">
      <div class="container">

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
            @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" align="left"></el-table-column>

          <el-table-column prop="name" :label="$t('name')" align="left">
            <template slot-scope="scope">
              {{ $t(scope.row.name) }}
            </template>
          </el-table-column>

          <el-table-column prop="type" :label="$t('type')" align="left">
            <template slot-scope="scope">
              {{ scope.row.type | endpointType }}
            </template>
          </el-table-column>

          <el-table-column prop="ip" :label="$t('ip')" align="left">
            <template slot-scope="scope">
              {{ scope.row.ip }}
            </template>
          </el-table-column>

          <el-table-column prop="createTime" :label="$t('create_time')" align="left">
            <template slot-scope="scope">
              {{ scope.row.createTime | dateFormat }}
            </template>
          </el-table-column>

          <el-table-column prop="status" :label="$t('status')" align="left">
            <template slot-scope="scope">
              <span v-if="scope.row.status == 'Online'" class="green">
                Online
              </span>
              <span v-if="scope.row.status == 'Offline'" class="grey">
                Offline
              </span>
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
            :title="editType == 'edit' ? $t('edit_endpoint') : $t('add_endpoint')"
            :visible.sync="editDialogVisible"
            :wrapperClosable="false"
            direction="rtl"
            :before-close="handleClose">
          <div class="demo-drawer__content">
            <el-form :model="editObj" labelPosition="top" :rules="rules" ref="editForm" label-width="80px">

              <el-form-item :label="$t('name')" prop="name">
                <el-input v-model="editObj.name" autocomplete="off"
                          :placeholder="$t('pls_input_param_value')" class="input-element"></el-input>
              </el-form-item>

              <el-form-item :label="$t('type')" prop="type">

                <el-select v-model="editObj.type" class="input-element">
                  <el-option v-for="t in onlySlave(allEndPointType)" :label="$t(t.name)" :value="t.value"></el-option>
                </el-select>
              </el-form-item>

              <el-form-item :label="$t('ip')" prop="ip">
                <el-input v-model="editObj.ip" autocomplete="off"
                          :placeholder="$t('pls_input_param_value')" class="input-element"></el-input>
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
    </el-tab-pane>
  </el-tabs>
</template>

<script>

import HttpUtil from "../../common/utils/HttpUtil"
import Vue from 'vue'
import {ipValidator, requiredValidator} from "@/common/validator/CommonValidator";
import i18n from "@/i18n/i18n";

let _ = require('lodash');

Vue.filter('endpointType', function (type) {
  let allTypes = [];
  if (!localStorage.getItem("allEndpointTypes")) {
    HttpUtil.get("endpoint/getAllEndPointType", null, (res) => {
      localStorage.setItem('allEndpointTypes', JSON.stringify(res.data));
      allTypes = res.data;
    });
  } else {
    allTypes = JSON.parse(localStorage.getItem("allEndpointTypes"));
  }
  return i18n.t(_.find(allTypes, (t) => t.value == type).name);
});
export default {
  data() {
    return {
      rules: {
        name: [
          {validator: requiredValidator, trigger: 'blur', vue: this},
        ],
        type: [
          {validator: requiredValidator, trigger: 'blur', vue: this, msg: this.$t('pls_select') + this.$t('endpoint')},
        ],
        ip: [
          {validator: ipValidator, trigger: 'blur', vue: this},
        ],
      },
      activeName: 'endpoint',
      query: {
        name: '',
        pageIndex: 1,
        pageSize: 10
      },
      tableData: [],
      multipleSelection: [],
      delList: [],
      editVisible: false,
      pageTotal: 0,
      form: {},
      idx: -1,
      id: -1,
      loading: false,
      editDialogVisible: false,
      editType: 'edit',
      editObj: {
        name: null,
        description: null,
        type: null
      },
      allOs: [],
      allOsVersion: [],
      allEndPointType: [],
      loadingList: [],
    };
  },
  mounted() {
    this.getData();
    this.getAllEndPointType();
  },
  methods: {
    onlySlave() {
      return [
        {
          "name": this.$t("从节点"),
          "value": "slave_endpoint"
        }
      ];
    },
    // 获取 easy-mock 的模拟数据
    getData() {
      this.loadingList = true;
      HttpUtil.post("/endpoint/list/" + this.query.pageIndex + "/" + this.query.pageSize, {}, (res) => {
        this.tableData = res.data.listObject;
        this.pageTotal = res.data.itemCount;
        this.loadingList = false;
      });

    },
    getAllEndPointType() {
      HttpUtil.get("/endpoint/getAllEndPointType", {}, (res) => {
        this.allEndPointType = res.data;
        localStorage.setItem("allEndpointTypes", JSON.stringify(res.data));
      });
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
    getSelectedIds: function () {
      this.delList = [].concat(this.multipleSelection);
      let ids = _.map(this.delList, (item) => item.id);
      return ids;
    },
    confirmEdit() {
      this.validateResult = true;
      this.$refs.editForm.validate(f => {
        if (!f) {
          this.validateResult = false;
        }
      });
      if (!this.validateResult) return;
      this.loading = true;
      this.editObj.brands = JSON.stringify(this.editObj.brands);
      if (this.editType == 'edit') {
        HttpUtil.post("/endpoint/update", this.editObj, (res) => {
          this.editDialogVisible = false;
          this.editObj.defaultParams = JSON.stringify(this.editObj.defaultParams);
          this.$message.success(this.$t('edit_success'));
          this.getData();
          this.loading = false;
          this.getAllEndPointType();
        })
      } else {
        HttpUtil.post("/endpoint/add", this.editObj, (res) => {
          if (res.data) {
            this.$message.success(this.$t('add_success'));
            this.editDialogVisible = false;
          } else {
            this.$message.error(this.$t('add_fail'));
          }
          this.getData();
          this.loading = false;
          this.getAllEndPointType();
        })
      }
    },
    // 多选操作
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    delAllSelection() {
      let ids = this.getSelectedIds();
      if (!ids || ids.length == 0) {
        this.$message.error(this.$t('pls_select_endpoint') + "!");
        return;
      }
      this.$confirm(this.$t('confirm_to_del_endpoint'), this.$t('tips'), {
        type: 'warning'
      }).then(() => {
        HttpUtil.post("/endpoint/del", ids, (res) => {
          if (res.success) {
            this.$message.success(this.$t('delete_success'));
          } else {
            this.$message.success(this.$t('delete_fail'));
          }
          this.getData();
        });
        this.multipleSelection = [];
      });
    },
    // 编辑操作
    handleEdit(row, type) {
      if (type == 'edit') {
        this.editDialogVisible = true;
        this.editType = type;
        this.editObj = JSON.parse(JSON.stringify(row));
        this.editObj.brands = eval(this.editObj.brands);
        this.editObj.settable = eval(this.editObj.settable);
        // this.editObj.status = eval(this.editObj.status);

      } else if (type == 'del') {
        this.$confirm(this.$t('confirm_to_del_endpoint'), this.$t('tips'), {
          type: 'warning'
        }).then(() => {
          HttpUtil.get("/endpoint/del/" + row.id, {}, (res) => {
            if (res.data) {
              this.$message.success(this.$t('delete_success'));
            } else {
              this.$message.success(this.$t('delete_fail'));
            }
            this.getData();
          });
        })
      } else if (type == 'sync') {
        HttpUtil.get("/endpoint/sync", {}, (res) => {
          if (res.data) {
            this.$message.success(this.$t('sync_success'));
          } else {
            this.$message.success(this.$t('sync_fail'));
          }
          this.getData();
        });
      } else {
        this.editDialogVisible = true;
        this.editType = type;
        this.$refs.editForm.resetFields();
        this.editObj = {
          status: "1"
        };
      }
    },
    // 分页导航
    handlePageChange(val) {
      this.$set(this.query, 'pageIndex', val);
      this.getData();
    }
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

</style>