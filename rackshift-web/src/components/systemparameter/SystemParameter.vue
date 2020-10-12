<template>
  <div class="container">

    <div class="machine-title">
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
      <el-table-column type="selection" align="left"></el-table-column>
      <el-table-column :prop="c.prop" :label="c.label" align="left"
                       v-for="c in columns" sortable></el-table-column>

      <el-table-column prop="" :label="$t('opt')" align="left">
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
        :title="editType == 'edit' ? $t('edit_system_parameter') : $t('add_system_parameter')"
        :visible.sync="editDialogVisible"
        direction="rtl"
        :wrapperClosable="false"
        :before-close="handleClose">
      <div class="demo-drawer__content">
        <el-form :model="editObj">

          <el-form-item :label="$t('param_key')">

            <el-select v-model="editObj.paramKey">
              <el-option v-for="t in allEndPointType" :label="t.name" :value="t.value"></el-option>
            </el-select>

          </el-form-item>


          <el-form-item :label="$t('param_value')">
            <el-input v-model="editObj.paramValue" autocomplete="off"
                      :placeholder="$t('pls_input_param_value')"></el-input>
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

let _ = require('lodash');
export default {
  data() {
    return {
      query: {
        name: '',
        pageIndex: 1,
        pageSize: 10
      },
      tableData: [],
      loadingList: false,
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
          label: this.$t('param_key'),
          prop: "paramKey",
          sort: true
        },
        {
          label: this.$t('param_value'),
          prop: "paramValue"
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
      HttpUtil.post("/system_parameter/list/" + this.query.pageIndex + "/" + this.query.pageSize, {}, (res) => {
        this.tableData = res.data.listObject;
        this.pageTotal = res.data.itemCount;
        this.loadingList = false;
      });

    },
    getAllEndPointType() {
      HttpUtil.get("/system_parameter/getAllEndPointType", {}, (res) => {
        this.allEndPointType = res.data;
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
        HttpUtil.post("/system_parameter/update", this.editObj, (res) => {
          this.editDialogVisible = false;
          this.editObj.defaultParams = JSON.stringify(this.editObj.defaultParams);
          this.$message.success(this.$t('edit_success'));
          this.getData();
          this.loading = false;
        })
      } else {
        HttpUtil.post("/system_parameter/add", this.editObj, (res) => {
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
        this.$notify.error(this.$t('pls_select_system_parameter') + "!");
        return;
      }
      HttpUtil.post("/system_parameter/del", ids, (res) => {
        this.$message.success(this.$t('delete_success'));
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
          HttpUtil.get("/system_parameter/del/" + row.id, {}, (res) => {
            this.getData();
            this.$message.success(this.$t('delete_success!'));
          });
        })
      } else {
        this.editDialogVisible = true;
        this.editType = type;
        this.editObj = {};
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