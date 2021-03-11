<template>
  <el-tabs class="t100vw" v-model="activeName">
    <el-tab-pane :label="$t('Workflow')" name="workflow">
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

          <el-table-column prop="friendlyName" :label="$t('friendly_name')" align="left">
            <template slot-scope="scope">
              <!--              <el-tooltip class="item" effect="dark" :content="scope.row.friendlyName" placement="right-end">-->
              <span class="rs-nowrap">{{ $t(scope.row.friendlyName) }}</span>
              <!--              </el-tooltip>-->
            </template>

          </el-table-column>

          <el-table-column :prop="c.prop" :label="c.label" align="left"
                           v-for="c in columns" :sortable="c.sort" :formatter="getValidProText"
                           width="100"></el-table-column>

          <el-table-column prop="eventType" :label="$t('event_type')" align="left">
            <template slot-scope="scope">
              <span class="rs-nowrap">{{ scope.row.eventType | eventFormat }}</span>
            </template>
          </el-table-column>

          <el-table-column prop="brands" :label="$t('brands')" align="left">
            <template slot-scope="scope">
              {{ scope.row.brands | brandsFormat }}
            </template>
          </el-table-column>

          <el-table-column prop="createTime" :label="$t('create_time')" align="left">
            <template slot-scope="scope">
              {{ scope.row.createTime | dateFormat }}
            </template>
          </el-table-column>

          <el-table-column prop="" :label="$t('opt')" align="left">
            <template slot-scope="scope">
              <RSButton @click="handleEdit(scope.row, 'edit')"></RSButton>
              <RSButton v-if="scope.row.type != 'system'" icon="el-icon-delete" @click="handleEdit(scope.row, 'del')"
                        type="del"></RSButton>
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
            :wrapperClosable="false"
            :before-close="handleClose">
          <div class="demo-drawer__content">
            <el-form :model="editObj" :label-position="labelPosition" :rules="rules" ref="form" label-width="80px">

              <el-form-item :label="$t('injectable_name')" prop="injectableName">
                <el-select filterable v-model="editObj.injectableName" :placeholder="$t('pls_select')"
                           :disabled="editObj.type == 'system'" v-on:change="changeFriendlyName" class="input-element">
                  <el-option
                      v-for="item in allRackHDWorkflows"
                      :label="item.injectableName"
                      :value="item.injectableName">
                  </el-option>
                </el-select>
              </el-form-item>

              <el-form-item :label="$t('friendly_name')" prop="friendlyName">
                <el-input v-model="editObj.friendlyName" class="input-element"></el-input>
              </el-form-item>

              <el-form-item :label="$t('event_type')" prop="eventType">
                <el-select v-model="editObj.eventType" :placeholder="$t('pls_select')"
                           :disabled="editObj.type == 'system'" class="input-element">
                  <el-option
                      v-for="(item, key) in allEventType"
                      :label="$t(item.name)"
                      :value="item.value">
                  </el-option>
                </el-select>
              </el-form-item>

              <el-form-item :label="$t('brands')" prop="brands">
                <el-select v-model="editObj.brands" :placeholder="$t('pls_select')" multiple class="input-element">
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

              <el-form-item :label="$t('status')" :disabled="editObj.type == 'system'" prop="status">
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
    </el-tab-pane>
  </el-tabs>

</template>

<script>

import HttpUtil from "../../common/utils/HttpUtil"
import Vue from "vue"
import i18n from "@/i18n/i18n";
import {requiredSelectValidator, requiredValidator} from "@/common/validator/CommonValidator";

Vue.filter('eventFormat', function (name) {
  if (!name || name.length == 0) {
    return "";
  }
  let allEventType = [];
  if (!localStorage.getItem("allEventType")) {
    HttpUtil.get("workflow/listallEventType", null, (res) => {
      localStorage.setItem('allEventType', JSON.stringify(res.data));
      allEventType = res.data;
    });
  } else {
    allEventType = JSON.parse(localStorage.getItem("allEventType"));
  }
  return i18n.t(_.find(allEventType, (t) => t.value == name).name);
});

let _ = require('lodash');
export default {
  data() {
    return {
      rules: {
        injectableName: [
          {validator: requiredSelectValidator, trigger: 'blur', vue: this},
        ],
        friendlyName: [
          {validator: requiredValidator, trigger: 'blur', vue: this},
        ],
        eventType: [
          {validator: requiredSelectValidator, trigger: 'blur', vue: this},
        ],
        brands: [
          {validator: requiredSelectValidator, trigger: 'blur', vue: this, name: this.$t('brands')},
        ],
        type: [
          {validator: requiredValidator, trigger: 'blur', vue: this},
        ]
      },
      activeName: 'workflow',
      query: {
        name: '',
        pageIndex: 1,
        pageSize: 10
      },
      labelPosition: 'top',
      tableData: [],
      multipleSelection: [],
      delList: [],
      editVisible: false,
      loadingList: false,
      pageTotal: 0,
      form: {},
      idx: -1,
      id: -1,
      loading: false,
      columns: [
        {
          label: this.$t('type'),
          prop: "type"
        }
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
  filters: {
    enabled: function (v) {
      if (v) {
        return i18n.t("enabled");
      } else {
        return i18n.t("disabled");
      }
    }
  },
  methods: {
    i18n(item) {
      return this.$t(item);
    },
    getValidProText(row, column, cellValue, index) {
      if (column.property == 'type') {
        return this.$t(cellValue);
      }
      if (cellValue === true) {
        return this.$t("enabled");
      } else if (cellValue === false) {
        return this.$t("disabled");
      }
      return cellValue;
    },
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
      this.loadingList = true;
      HttpUtil.post("/workflow/list/" + this.query.pageIndex + "/" + this.query.pageSize, {}, (res) => {
        this.tableData = res.data.listObject;
        this.pageTotal = res.data.itemCount;
        this.loadingList = false;
      });

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
      if (!this.validateResult) return;

      this.loading = true;
      this.editObj.brands = JSON.stringify(this.editObj.brands);
      if (this.editType == 'edit') {
        HttpUtil.put("/workflow/update", this.editObj, (res) => {
          this.editDialogVisible = false;
          this.editObj.defaultParams = JSON.stringify(this.editObj.defaultParams);
          this.$message.success(this.$t('edit_success'));
          this.getData();
          this.loading = false;
        })
      } else {
        HttpUtil.post("/workflow/add", this.editObj, (res) => {
          if (res.success) {
            this.editDialogVisible = false;
            this.$message.success(this.$t('add_success'));
            this.getData();
            this.loading = false;
          } else {
            this.$message.error(this.$t('add_fail'));
          }
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
        this.$message.error(this.$t('pls_select_workflow') + "!");
        return;
      }
      this.$confirm(this.$t('confirm_to_del'), this.$t('tips'), {
        type: 'warning'
      }).then(() => {
        this.loadingList = true;
        HttpUtil.post("/workflow/del", ids, (res) => {
          if (res.success) {
            this.$message.success(this.$t('delete_success'));
            this.getData();
            this.multipleSelection = [];
            this.loadingList = false;
          } else {
            this.$message.success(this.$t('delete_fail'));
          }
        }, (e) => {
          this.$message.success(this.$t('delete_fail'));
          this.loadingList = false;
        });
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
        this.editObj.friendlyName = this.$t(this.editObj.friendlyName);
        this.resetFields();
      } else if (type == 'del') {
        this.$confirm(this.$t('confirm_to_del'), this.$t('tips'), {
          type: 'warning'
        }).then(() => {
          this.loadingList = true;
          HttpUtil.delete("/workflow/del/" + row.id, {}, (res) => {
            this.getData();
            this.$message.success(this.$t('delete_success!'));
            this.loadingList = false;
          }, (res) => {
            this.$message.error(this.$t('delete_fail!'));
            this.loadingList = false;
          });
        })
      } else {
        this.editDialogVisible = true;
        this.editType = type;
        this.resetFields();
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