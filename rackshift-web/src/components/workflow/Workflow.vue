<template>
  <div class="container">

    <div class="machine-title">
      <i class="el-icon-user-solid">{{ $t('Workflow') }}</i>

      <el-button-group class="batch-button">
        <el-button type="primary" icon="el-icon-circle-plus-outline">{{ $t('add') }}</el-button>
        <el-button type="primary" icon="el-icon-delete-solid" @click="delAllSelection">{{ $t('del') }}
        </el-button>
        <el-button type="primary" icon="el-icon-refresh" @click="getData">{{ $t('refresh') }}</el-button>
      </el-button-group>
    </div>

    <el-table
        :data="tableData"
        class="table"
        ref="multipleTable"
        header-cell-class-name="table-header"
        style="width: 100%"
        @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" align="center"></el-table-column>
      <el-table-column :prop="c.prop" :label="c.label" align="center"
                       v-for="c in columns" sortable></el-table-column>
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
        :title="editType == 'edit' ? $t('edit_workflow') : $t('add_workflow')"
        :visible.sync="editDialogVisible"
        direction="rtl"
        :before-close="handleClose">
      <div class="demo-drawer__content">
        <el-form :model="editObj">
          <el-form-item :label="$t('friendly_name')">
            <el-input v-model="editObj.friendlyName" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item :label="$t('injectable_mame')">
            <el-input v-model="editObj.injectableName" autocomplete="off"></el-input>
          </el-form-item>

          <el-form-item :label="$t('event_type')">
            <el-select v-model="editObj.eventType" :placeholder="$t('pls_select')">
              <el-option
                  v-for="(item, key) in allEventType"
                  :key="item.id"
                  :value="item.name">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item :label="$t('brands')">
            <el-select v-model="editObj.brands" :placeholder="$t('pls_select')" multiple>
              <el-option
                  v-for="(item, key) in allBands"
                  :key="item.name"
                  :value="item.name">
              </el-option>
            </el-select>
          </el-form-item>

          <el-form-item :label="$t('settable')">
            <el-switch v-model="editObj.settable"></el-switch>
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
          label: this.$t('friendly_name'),
          prop: "friendlyName",
          sort: true
        },
        {
          label: this.$t('event_type'),
          prop: "eventType"
        },
        {
          label: this.$t('brands'),
          prop: "brands"
        },
        {
          label: this.$t('settable'),
          prop: "settable"
        },
        {
          label: this.$t('default_params'),
          prop: "defaultParams"
        },
        {
          label: this.$t('status'),
          prop: "status"
        },
      ],
      editDialogVisible: false,
      editType: 'edit',
      editObj: {
        name: null,
        description: null,
        type: null
      },
      allEventType: [],
    };
  },
  mounted() {
    this.getData();
  },
  methods: {
    // 获取 easy-mock 的模拟数据
    getData() {
      HttpUtil.post("/workflow/list/" + this.query.pageIndex + "/" + this.query.pageSize, {}, (res) => {
        this.tableData = res.data.listObject;
        this.pageTotal = res.data.itemCount;
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
    confirmEdit() {
      this.loading = true;
      if (this.editType == 'edit') {
        HttpUtil.post("/workflow/update", this.editObj, (res) => {
          this.editDialogVisible = false;
          this.$message.success(this.$t('edit_success'));
          this.cancelForm();
          this.getData();
        })
      } else {
        HttpUtil.post("/workflow/add", this.editObj, (res) => {
          this.editDialogVisible = false;
          this.$message.success(this.$t('add_success'));
          this.cancelForm();
          this.getData();
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
        str += this.multipleSelection[i].name + ' ';
      }
      let ids = this.getSelectedIds();
      if (!this.ids || !this.ids.length == 0) {
        this.$notify.error(this.$t('pls_select_bare_metal') + "!");
        return;
      }
      HttpUtil.post("/workflow/del", ids, (res) => {
        this.$message.success(this.$t('delete_success') + this.$t('deleted') + '${str}！');
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
        this.editObj.rolesIds = _.map(this.editObj.roles, (item) => item.id);

      } else if (type == 'del') {
        if (!this.selectedWorkflow.length) {
          this.$notify.error(this.$t('pls_select_workflow') + "!");
          return;
        }
        this.$confirm(this.$t('confirm_to_del'), this.$t('tips'), {
          type: 'warning'
        }).then(() => {
          HttpUtil.get("/workflow/del/" + row.id, {}, (res) => {
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