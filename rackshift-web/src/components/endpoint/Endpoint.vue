<template>
  <div class="container">

    <div class="machine-title">
      <i class="el-icon-user-solid">{{ $t('Endpoint') }}</i>

      <el-button-group class="batch-button">
        <el-button type="primary" icon="el-icon-circle-plus-outline" @click="handleEdit({}, 'add')">{{
            $t('add')
          }}
        </el-button>
        <el-button type="primary" icon="el-icon-delete-solid" @click="delAllSelection">{{ $t('del') }}
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
      <el-table-column type="selection" align="center"></el-table-column>

      <el-table-column :prop="c.prop" :label="c.label" align="center"
                       v-for="c in columns" sortable></el-table-column>

      <el-table-column prop="type" :label="$t('type')" align="center">
        <template slot-scope="scope">
          {{ scope.row.type | endpointType }}
        </template>
      </el-table-column>

      <el-table-column prop="ip" :label="$t('ip')" align="center">
        <template slot-scope="scope">
          {{ scope.row.ip }}
        </template>
      </el-table-column>

      <el-table-column prop="createTime" :label="$t('create_time')" align="center">
        <template slot-scope="scope">
          {{ scope.row.createTime | dateFormat }}
        </template>
      </el-table-column>

      <el-table-column prop="" :label="$t('opt')" align="center">
        <template slot-scope="scope">
          <el-button
              type="button"
              icon="el-icon-edit"
              @click="handleEdit(scope.row, 'edit')"
          >{{ $t('edit') }}
          </el-button>

          <el-button
              type="button"
              icon="el-icon-delete"
              class="red"
              @click="handleEdit(scope.row, 'del')"
          >{{ $t('del') }}
          </el-button>
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
        direction="rtl"
        :before-close="handleClose">
      <div class="demo-drawer__content">
        <el-form :model="editObj">

          <el-form-item :label="$t('name')">
            <el-input v-model="editObj.name" autocomplete="off"
                      :placeholder="$t('pls_input_param_value')"></el-input>
          </el-form-item>

          <el-form-item :label="$t('type')">

            <el-select v-model="editObj.type">
              <el-option v-for="t in allEndPointType" :label="t.name" :value="t.value"></el-option>
            </el-select>
          </el-form-item>

          <el-form-item :label="$t('ip')">
            <el-input v-model="editObj.ip" autocomplete="off"
                      :placeholder="$t('pls_input_param_value')"></el-input>
          </el-form-item>

          <el-form-item :label="$t('status')">
            <el-switch v-model="editObj.status" :active-value="'1'" :inactive-value="'0'"
                       :disabled="editType != 'add' && editObj.type == 'main_endpoint'"></el-switch>
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
</template>

<script>

import HttpUtil from "../../common/utils/HttpUtil"
import Vue from 'vue'

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
  return _.find(allTypes, (t) => t.value == type).name;
});
export default {
  data() {
    return {
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
      columns: [
        {
          label: this.$t('name'),
          prop: "name",
          sort: true
        },
      ],
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
      this.loading = true;
      this.editObj.brands = JSON.stringify(this.editObj.brands);
      if (this.editType == 'edit') {
        HttpUtil.post("/endpoint/update", this.editObj, (res) => {
          this.editDialogVisible = false;
          this.editObj.defaultParams = JSON.stringify(this.editObj.defaultParams);
          this.$message.success(this.$t('edit_success'));
          this.getData();
          this.loading = false;
        })
      } else {
        HttpUtil.post("/endpoint/add", this.editObj, (res) => {
          this.editDialogVisible = false;
          if (res.data) {
            this.$message.success(this.$t('add_success'));
          } else {
            this.$message.success(this.$t('add_fail'));
          }
          this.getData();
          this.loading = false;
        })
      }
    },
    // 多选操作
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    delAllSelection() {
      const length = this.multipleSelection.length;
      let str = '';
      for (let i = 0; i < length; i++) {
        str += this.multipleSelection[i].machineModel + ' ';
      }
      let ids = this.getSelectedIds();
      if (!ids || ids.length == 0) {
        this.$notify.error(this.$t('pls_select_endpoint') + "!");
        return;
      }
      HttpUtil.post("/endpoint/del", ids, (res) => {
        if (res.data) {
          this.$message.success(this.$t('delete_success'));
        } else {
          this.$message.success(this.$t('delete_fail'));
        }
        this.getData();
      });
      this.multipleSelection = [];
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
        this.editObj.rolesIds = _.map(this.editObj.roles, (item) => item.id);

      } else if (type == 'del') {
        this.$confirm(this.$t('confirm_to_del'), this.$t('tips'), {
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
      } else {
        this.editDialogVisible = true;
        this.editType = type;
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