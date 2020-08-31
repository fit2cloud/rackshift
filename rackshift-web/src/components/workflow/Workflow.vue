<template>
  <div class="container">

    <div class="machine-title">
      <i class="el-icon-user-solid">{{ $t('Workflow') }}</i>

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
        header-cell-class-name="table-header"
        style="width: 100%"
        @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" align="center"></el-table-column>

      <el-table-column :prop="c.prop" :label="c.label" align="center"
                       v-for="c in columns" sortable></el-table-column>

      <el-table-column prop="settable" :label="$t('user_settable')" align="center">
        <template slot-scope="scope">
          {{ scope.row.settable }}
        </template>
      </el-table-column>

      <el-table-column prop="settable" :label="$t('event_type')" align="center">
        <template slot-scope="scope">
          {{ scope.row.eventType | eventFormat }}
        </template>
      </el-table-column>

      <el-table-column prop="brands" :label="$t('brands')" align="center">
        <template slot-scope="scope">
          {{ scope.row.brands | brandsFormat }}
        </template>
      </el-table-column>

      <el-table-column prop="status" :label="$t('status')" align="center">
        <template slot-scope="scope">
          {{ scope.row.status }}
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
              v-if="scope.row.type != 'system'"
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

          <el-form-item :label="$t('injectable_mame')">
            <el-select v-model="editObj.injectableName" :placeholder="$t('pls_select')"
                       :disabled="editObj.type == 'system'" v-on:change="changeFriendlyName">
              <el-option
                  v-for="item in allRackHDWorkflows"
                  :label="item.injectableName"
                  :value="item.injectableName">
              </el-option>
            </el-select>
          </el-form-item>

          <el-form-item :label="$t('friendly_name')">
            <el-input v-model="editObj.friendlyName"></el-input>
            <!--            <el-input v-model="editObj.friendlyName" autocomplete="off" :disabled="editObj.type == 'system'"></el-input>-->
          </el-form-item>

          <el-form-item :label="$t('event_type')">
            <el-select v-model="editObj.eventType" :placeholder="$t('pls_select')" :disabled="editObj.type == 'system'">
              <el-option
                  v-for="(item, key) in allEventType"
                  :label="item.name"
                  :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item :label="$t('brands')">
            <el-select v-model="editObj.brands" :placeholder="$t('pls_select')" multiple>
              <el-option
                  v-for="(item, key) in allBrands"
                  :label="item"
                  :value="item">
              </el-option>
            </el-select>
          </el-form-item>

          <el-form-item :label="$t('settable')" :disabled="editObj.type == 'system'">
            <el-switch v-model="editObj.settable"></el-switch>
          </el-form-item>

          <el-form-item :label="$t('default_params')" :disabled="editObj.type == 'system'">
            <el-input type="textarea" v-model="editObj.defaultParams" :rows="5"></el-input>
          </el-form-item>

          <el-form-item :label="$t('status')" :disabled="editObj.type == 'system'">
            <el-switch v-model="editObj.status" active-value="enable"
                       inactive-value="disable"></el-switch>
          </el-form-item>

        </el-form>
        <div class="demo-drawer__footer">
          <el-button @click="editDialogVisible = false">{{ $t('cancel') }}</el-button>
          <el-button type="primary" @click="confirmEdit" :loading="loading" :disabled="editObj.type == 'system'">{{
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
import Vue from "vue"

Vue.filter('eventFormat', function (name) {
  let allEventType = [];
  if (!localStorage.getItem("allEventType")) {
    HttpUtil.get("workflow/listallEventType", null, (res) => {
      localStorage.setItem('allEventType', JSON.stringify(res.data));
      allEventType = res.data;
    });
  } else {
    allEventType = JSON.parse(localStorage.getItem("allEventType"));
  }
  return _.find(allEventType, (t) => t.value == name).name;
});

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
          label: this.$t('type'),
          prop: "type",
          sort: true
        },
        {
          label: this.$t('injectable_name'),
          prop: "injectableName",
          sort: true
        },
      ],
      editDialogVisible: false,
      editType: 'edit',
      editObj: {
        injectableName: null,
        friendlyName: null,
        settable: false,
        brands: [],
        defaultParams: null
      },
      allEventType: [],
      allBrands: [
        'DELL',
        'HP',
        'Inspur'
      ],
      allRackHDWorkflows: [],
      workflowMap: {}
    };
  },
  mounted() {
    this.getData();
    this.getAllRackHDWorkflows();
    this.getAllEventType();
  },
  methods: {
    getAllRackHDWorkflows: function () {
      HttpUtil.get("workflow/listallRackHDWorkflows", null, (res) => {
        this.allRackHDWorkflows = res.data;
        if (res.data && res.data.length) {
          res.data.forEach(d => {
            this.workflowMap[d.injectableName] = d;
          });
        }
      });
    },
    getAllEventType: function () {
      if (!localStorage.getItem("allEventType")) {
        HttpUtil.get("workflow/listallEventType", null, (res) => {
          localStorage.setItem('allEventType', JSON.stringify(res.data));
          this.allEventType = res.data;
        });
      } else {
        this.allEventType = JSON.parse(localStorage.getItem("allEventType"));
      }
    },
    changeFriendlyName: function () {
      let copyObj = JSON.parse(JSON.stringify(this.workflowMap[this.editObj.injectableName]));
      this.editObj.friendlyName = copyObj.friendlyName;
      this.editObj.settable = copyObj.options ? true : false;
      this.editObj.defaultParams = JSON.stringify(copyObj.options);
    },
    getSelectedIds: function () {
      this.delList = [].concat(this.multipleSelection);
      let ids = _.map(this.delList, (item) => item.id);
      return ids;
    },
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
    confirmEdit() {
      this.loading = true;
      this.editObj.brands = JSON.stringify(this.editObj.brands);
      if (this.editType == 'edit') {
        HttpUtil.post("/workflow/update", this.editObj, (res) => {
          this.editDialogVisible = false;
          this.editObj.defaultParams = JSON.stringify(this.editObj.defaultParams);
          this.$message.success(this.$t('edit_success'));
          this.getData();
          this.loading = false;
        })
      } else {
        HttpUtil.post("/workflow/add", this.editObj, (res) => {
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
    },
    delAllSelection() {
      const length = this.multipleSelection.length;
      let str = '';
      for (let i = 0; i < length; i++) {
        str += this.multipleSelection[i].machineModel + ' ';
      }
      let ids = this.getSelectedIds();
      if (!ids || ids.length == 0) {
        this.$notify.error(this.$t('pls_select_workflow') + "!");
        return;
      }
      HttpUtil.post("/workflow/del", ids, (res) => {
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
          HttpUtil.get("/workflow/del/" + row.id, {}, (res) => {
            this.getData();
            this.$message.success(this.$t('delete_success!'));
          });
        })
      } else {
        this.editDialogVisible = true;
        this.editType = type;
        this.editObj = {
          injectableName: null,
          friendlyName: null,
          settable: false,
          status: false,
          brands: [],
          defaultParams: null
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

.el-button + .el-button {
  margin-left: 0px;
}
</style>