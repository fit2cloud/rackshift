<template>
  <el-tabs class="t100vw" v-model="activeName">
    <el-tab-pane :label="$t('template')" name="template">
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

          <el-table-column prop="id" :label="$t('id')" align="left">
            <template slot-scope="scope">
              {{ $t(scope.row.id) }}
            </template>
          </el-table-column>

          <el-table-column prop="name" :label="$t('name')" align="left">
            <template slot-scope="scope">
              {{ $t(scope.row.name) }}
            </template>
          </el-table-column>

          <!--          <el-table-column prop="content" :label="$t('content')" align="left">-->
          <!--            <template slot-scope="scope">-->
          <!--              {{ scope.row.content }}-->
          <!--            </template>-->
          <!--          </el-table-column>-->

          <el-table-column prop="type" :label="$t('type')" align="left">
            <template slot-scope="scope">
              {{ $t(scope.row.type) }}
            </template>
          </el-table-column>


          <el-table-column prop="updateTime" :label="$t('update_time')" align="left">
            <template slot-scope="scope">
              {{ scope.row.updateTime | dateFormat }}
            </template>
          </el-table-column>

          <el-table-column prop="" :label="$t('opt')" align="left">
            <template slot-scope="scope">

              <RSButton @click="handleEdit(scope.row, 'edit')"></RSButton>
              <RSButton @click="handleEdit(scope.row, 'del')" type="del" v-if="scope.row.type != 'system'"></RSButton>
              <RSButton @click=" handleEdit(scope.row,
              'copy')" type="copy" :tip="$t('copy')"></RSButton>

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
            :title="editType == 'edit' ? $t('edit_template') : $t('add_template')"
            :visible.sync="editDialogVisible"
            :wrapperClosable="false"
            size="50%"
            direction="rtl"
            :before-close="handleClose">
          <div class="demo-drawer__content">
            <el-form :model="editObj" labelPosition="top" :rules="rules" ref="editForm" label-width="80px">

              <el-form-item :label="$t('name')" prop="name">
                <el-input v-model="editObj.name" autocomplete="off" min="3" max="30"
                          :placeholder="$t('pls_input_param_value')" class="input-element"></el-input>
              </el-form-item>

              <el-form-item :label="$t('type')" prop="type" v-if="editType == 'edit'">

                <el-select v-model="editObj.type" class="input-element" disabled>
                  <el-option v-for="t in types" :label="$t(t.name)" :value="t.value"></el-option>
                </el-select>
              </el-form-item>

              <el-form-item :label="$t('content')" prop="content">
                <el-input type="textarea" v-model="editObj.content" :rows="20"></el-input>
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
import {ipValidator, requiredValidator} from "@/common/validator/CommonValidator";
import {commonConstants} from '../../common/constants/ServiceConstants'

let _ = require('lodash');

export default {
  data() {
    return {
      rules: {
        name: [
          {validator: requiredValidator, trigger: 'blur', vue: this},
        ],
        content: [
          {validator: requiredValidator, trigger: 'blur', vue: this},
        ],
      },
      activeName: 'template',
      query: {
        name: '',
        pageIndex: 1,
        pageSize: 10
      },
      types: commonConstants.types,
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
      alltemplateType: [],
      loadingList: [],
    };
  },
  mounted() {
    this.getData();
  },
  methods: {
    // 获取 easy-mock 的模拟数据
    getData() {
      this.loadingList = true;
      HttpUtil.post("/template/list/" + this.query.pageIndex + "/" + this.query.pageSize, {}, (res) => {
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
        HttpUtil.put("/template/update", this.editObj, (res) => {
          if (res.data) {
            this.editDialogVisible = false;
            this.editObj.defaultParams = JSON.stringify(this.editObj.defaultParams);
            this.$message.success(this.$t('edit_success'));
            this.getData();
            this.loading = false;
          } else {
            this.$message.error(this.$t('opt_fail'));
            this.loading = false;
          }
        })
      } else {
        HttpUtil.post("/template/add", this.editObj, (res) => {
          if (res.data) {
            this.$message.success(this.$t('add_success'));
            this.editDialogVisible = false;
          } else {
            this.$message.error(this.$t(res.message));
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
      let arr = [];
      let sel = this.delList = [].concat(this.multipleSelection);
      _.forEach(sel, s => {
        if (s.type == 'system') {
          arr.push(s);
        }
      });
      if (arr.length > 0) {
        this.$message.error(this.$t('delete_fail'));
        return;
      }
      let ids = this.getSelectedIds();
      if (!ids || ids.length == 0) {
        this.$message.error(this.$t('pls_select') + this.$t('template') + "!");
        return;
      }
      this.$confirm(this.$t('confirm_to_del'), this.$t('tips'), {
        type: 'warning'
      }).then(() => {
        HttpUtil.post("/template/del", ids, (res) => {
          if (res.data) {
            this.$message.success(this.$t('delete_success'));
          } else {
            this.$message.error(this.$t('delete_fail'));
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
        this.resetFields();
      } else if (type == 'del') {
        if (row.type == 'system') {
          this.$message.error(this.$t('delete_fail'));
          return;
        }
        this.$confirm(this.$t('confirm_to_del') + this.$t('template'), this.$t('tips'), {
          type: 'warning'
        }).then(() => {
          HttpUtil.delete("/template/del/" + row.id, {}, (res) => {
            if (res.data) {
              this.$message.success(this.$t('delete_success'));
            } else {
              this.$message.error(this.$t('delete_fail'));
            }
            this.getData();
          });
        })
      } else if (type == 'copy') {
        this.editDialogVisible = true;
        this.editType = type;
        this.resetFields();
        this.editObj = JSON.parse(JSON.stringify(row));
        this.editObj.type = "user";
        delete this.editObj.id;
        this.resetFields();
      } else {
        this.editDialogVisible = true;
        this.editType = type;
        this.resetFields();
        this.editObj = {};
      }
    },
    resetFields() {
      if (this.$refs.editForm) {
        this.$refs.editForm.resetFields();
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