<template>
  <el-tabs class="t100vw" v-model="activeName">
    <el-tab-pane :label="$t('user')" name="user">
      <div class="container">
        <div class="machine-title">
          <div class="el-button-group batch-button">
            <el-button
                type="primary"
                icon="el-icon-circle-plus-outline"
                class="handle-del mr10"
                @click="handleEdit({}, 'add')"
            >{{ $t('add') }}
            </el-button>
            <el-button
                type="primary"
                icon="el-icon-delete"
                class="handle-del mr10"
                @click="delAllSelection"
            >{{ $t('delete') }}
            </el-button>
            <el-button type="primary" icon="el-icon-refresh" @click="getData">{{ $t('refresh') }}</el-button>
          </div>
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
                           v-for="c in columns"></el-table-column>
          <el-table-column prop="updateTime" :label="$t('update_time')" align="left">
            <template slot-scope="scope">
              {{ scope.row.updateTime | dateFormat }}
            </template>
          </el-table-column>
          <el-table-column prop="" :label="$t('opt')" align="left">
            <template slot-scope="scope">
              <RSButton @click="handleEdit(scope.row, 'edit')"></RSButton>
              <RSButton @click="handleEdit(scope.row, 'del')" type="del"></RSButton>
            </template>
          </el-table-column>
        </el-table>
        <div class="pagination">
          <el-pagination
              layout="total, sizes, prev, pager, next, jumper"
              :current-page="query.pageIndex"
              :page-sizes="[10, 20, 50, 100]"
              :page-size="10"
              :total="pageTotal"
              @size-change="handleSizeChange"
              @current-change="handlePageChange"
          ></el-pagination>
        </div>

        <el-drawer
            :title="editType == 'edit' ? $t('edit')+$t('user') :  $t('add')+$t('user')"
            :visible.sync="drawer"
            direction="rtl"
            :wrapperClosable="false"
            :before-close="handleClose">
          <div class="demo-drawer__content">
            <el-form :model="editObj" label-position="top" :rules="rules" ref="editForm">
              <el-form-item label="ID" :label-width="formLabelWidth" prop="id">
                <el-input v-model="editObj.id" autocomplete="off"></el-input>
              </el-form-item>
              <el-form-item :label="$t('name')" :label-width="formLabelWidth" prop="name">
                <el-input v-model="editObj.name" autocomplete="off"
                          :placeholder="$t('pls_input_name')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('email')" :label-width="formLabelWidth" prop="email">
                <el-input v-model="editObj.email" autocomplete="off"
                          :placeholder="$t('pls_input_email')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('phone')" :label-width="formLabelWidth" prop="phone">
                <el-input v-model="editObj.phone" autocomplete="off"
                          :placeholder="$t('pls_input_phone')"></el-input>
              </el-form-item>

              <el-form-item :label="$t('password')" :label-width="formLabelWidth" prop="password"
                            v-if="editType != 'edit'">
                <el-input v-model="editObj.password" autocomplete="off"
                          :placeholder="$t('pls_input_pwd')" show-password></el-input>
              </el-form-item>

              <!--              <el-form-item :label="$t('role')" :label-width="formLabelWidth" prop="rolesIds">-->
              <!--                <el-select v-model="editObj.rolesIds" multiple :placeholder="$t('pls_select')">-->
              <!--                  <el-option-->
              <!--                      v-for="(item, key) in allRoles"-->
              <!--                      :key="item.id"-->
              <!--                      :label="item.name"-->
              <!--                      :value="item.id">-->
              <!--                  </el-option>-->
              <!--                </el-select>-->
              <!--              </el-form-item>-->
            </el-form>
            <div class="demo-drawer__footer">
              <el-button @click="cancelForm">{{ $t('cancel') }}</el-button>
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
import HttpUtil from "../../common/utils/HttpUtil";
import {emailValidator, phoneValidator, requiredValidator} from "@/common/validator/CommonValidator";

let _ = require('lodash');
export default {
  data() {
    return {
      rules: {
        id: [
          {validator: requiredValidator, trigger: 'blur', vue: this},
        ],
        name: [
          {validator: requiredValidator, trigger: 'blur', vue: this},
        ],
        email: [
          {validator: emailValidator, trigger: 'blur', vue: this},
        ],
        phone: [
          {validator: phoneValidator, trigger: 'blur', vue: this},
        ],
        password: [
          {validator: requiredValidator, trigger: 'blur', vue: this},
        ],
      },
      activeName: 'user',
      query: {
        address: '',
        name: '',
        pageIndex: 1,
        pageSize: 10
      },
      tableData: [],
      multipleSelection: [],
      delList: [],
      loadingList: false,
      editVisible: false,
      formLabelWidth: '80px',
      pageTotal: 0,
      form: {},
      loading: false,
      idx: -1,
      id: -1,
      columns: [
        {
          label: this.$t('id'),
          prop: "id"
        },
        {
          label: this.$t('name'),
          prop: "name"
        },
        {
          label: this.$t('email'),
          prop: "email"
        },
        {
          label: this.$t('phone'),
          prop: "phone"
        },
      ],
      drawer: false,
      editType: 'edit',
      editObj: {
        name: null,
        id: null,
        email: null,
        phone: null,
        roles: [],
        rolesIds: []
      },
      allRoles: []
    };
  },
  mounted() {
    this.getData();
    // this.getAllRoles();
  },
  methods: {
    handleSizeChange(val) {
      this.query.pageSize = val;
      this.handlePageChange(this.query.pageIndex);
    },
    getData() {
      this.loadingList = true;
      HttpUtil.post("/user/list/" + this.query.pageIndex + "/" + this.query.pageSize, {}, (res) => {
        this.tableData = res.data.listObject;
        this.pageTotal = res.data.itemCount;
        this.loadingList = false;
      });
    },
    getAllRoles() {
      HttpUtil.post("/role/list/" + 1 + "/" + 10000, {}, (res) => {
        this.allRoles = res.data.listObject;
      });
    },
    handleClose() {
      this.drawer = false;
    },
    cancelForm() {
      this.loading = false;
      this.drawer = false;
    },
    resetFields() {
      if (this.$refs.editForm) {
        this.$refs.editForm.resetFields();
      }
    },
    add() {
      this.drawer = true;
      this.editType = 'add';
      this.resetFields();
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
      if (this.editType == 'edit') {
        HttpUtil.post("/user/update", this.editObj, (res) => {
          this.drawer = false;
          this.$message.success(this.$t('edit_success'));
          this.cancelForm();
          this.getData();
        })
      } else {
        HttpUtil.post("/user/add", this.editObj, (res) => {
          if (res.data) {
            this.drawer = false;
            this.$message.success(this.$t('opt_success'));
            this.cancelForm();
            this.getData();
          } else {
            this.$message.error(this.$t('opt_fail'));
            this.loading = false;
          }
        })
      }
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
        this.$message.error(this.$t('pls_select') + "!");
        return;
      }
      this.$confirm(this.$t('confirm_to_del'), this.$t('tips'), {
        type: 'warning'
      }).then(() => {
        HttpUtil.post("/user/del", ids, (res) => {
          if (res.data) {
            this.$message.success(this.$t('delete_success'));
            this.getData();
          } else {
            this.$message.error(this.$t('delete_fail'));
          }
        });
        this.multipleSelection = [];
      });
    },
    // 编辑操作
    handleEdit(row, type) {
      if (type == 'edit') {
        this.drawer = true;
        this.editType = type;
        this.editObj = JSON.parse(JSON.stringify(row));
        this.editObj.rolesIds = _.map(this.editObj.roles, (item) => item.id);
        this.resetFields();
      } else if (type == 'del') {
        this.$confirm(this.$t('confirm_to_del'), this.$t('tips'), {
          type: 'warning'
        }).then(() => {
          HttpUtil.get("/user/del/" + encodeURIComponent(row.id), {}, (res) => {
            if (res.data) {
              this.$message.success(this.$t('delete_success'));
              this.getData();
            } else {
              this.$message.error(this.$t('delete_fail'));
            }
          });
        })
      } else {
        this.drawer = true;
        this.editType = type;
        this.resetFields();
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