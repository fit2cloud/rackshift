<template>
  <el-tabs style="width:100vw;" v-model="activeName">
    <el-tab-pane :label="$t('role')" name="role">
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
              background
              layout="total, prev, pager, next"
              :current-page="query.pageIndex"
              :page-size="query.pageSize"
              :total="pageTotal"
              @current-change="handlePageChange"
          ></el-pagination>
        </div>
        <el-dialog
            :title="$t('edit_role')"
            :visible.sync="editDialogVisible"
            width="30%"
            :before-close="handleClose">

          <el-form label-width="110px" :model="editObj" label-position="right" :rules="rules" ref="editForm">

            <el-form-item :label="$t('name')" prop="name">
              <el-input v-model="editObj.name"></el-input>
            </el-form-item>

            <el-form-item :label="$t('desc')" prop="description">
              <el-input v-model="editObj.description"></el-input>
            </el-form-item>

            <el-form-item :label="$t('type')" prop="type">
              <el-select v-model="editObj.type" :placeholder="$t('pls_select')">
                <el-option :label="$t('admin')" value="admin"></el-option>
                <el-option :label="$t('user')" value="user"></el-option>
              </el-select>
            </el-form-item>
          </el-form>

          <template v-slot:footer>
            <div class="dialog-footer">
              <el-button @click="editDialogVisible = false">{{ $t('cancel') }}</el-button>
              <el-button type="primary" @click="confirmEdit">{{ $t('confirm') }}</el-button>
            </div>
          </template>
        </el-dialog>

      </div>
    </el-tab-pane>
  </el-tabs>


</template>

<script>

import HttpUtil from "../../common/utils/HttpUtil"
import {requiredValidator} from "@/common/validator/CommonValidator";

let _ = require('lodash');
export default {
  data() {
    return {
      rules: {
        name: [
          {validator: requiredValidator, trigger: 'blur', vue: this},
        ],
        description: [
          {validator: requiredValidator, trigger: 'blur', vue: this},
        ],
        type: [
          {validator: requiredValidator, trigger: 'blur', vue: this, msg: this.$t('pls_select') + this.$t('type')},
        ],
      },
      activeName: 'role',
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
      columns: [
        {
          label: this.$t('name'),
          prop: "name"
        },
        {
          label: this.$t('desc'),
          prop: "description"
        },
      ],
      editDialogVisible: false,
      editType: 'edit',
      editObj: {
        name: null,
        description: null,
        type: null
      },
      loadingList: false
    };
  },
  mounted() {
    this.getData();
  },
  methods: {
    // 获取 easy-mock 的模拟数据
    getData() {
      this.loadingList = true;
      HttpUtil.post("/role/list/" + this.query.pageIndex + "/" + this.query.pageSize, {}, (res) => {
        this.tableData = res.data.listObject;
        this.pageTotal = res.data.itemCount;
        this.loadingList = false;
      });

    },
    handleClose() {
      this.editDialogVisible = false;
    },
    add() {
      this.editDialogVisible = true;
      this.editType = 'add';
    },
    confirmEdit() {
      this.validateResult = true;
      this.$refs.editForm.validate(f => {
        if (!f) {
          this.validateResult = false;
        }
      });
      if (!this.validateResult) return;

      if (this.editType == 'edit') {
        HttpUtil.post("/role/update", this.editObj, (res) => {
          this.editDialogVisible = false;
          this.$message.success('编辑成功');
          this.getData();
        })
      } else {
        HttpUtil.post("/role/add", this.editObj, (res) => {
          this.editDialogVisible = false;
          this.$message.success('新增成功');
          this.getData();
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
      this.$confirm(this.$t('confirm_to_del'), this.$t('tips'), {
        type: 'warning'
      }).then(() => {
        let ids = this.getSelectedIds();
        if (!ids || ids.length == 0) {
          this.$message.error(this.$t('pls_select') + "!");
          return;
        }
        HttpUtil.post("/role/del", ids, (res) => {
          this.$message.success(this.$t('delete_success'));
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
      } else if (type == 'del') {
        this.$confirm('确定要删除吗？', '提示', {
          type: 'warning'
        }).then(() => {
          HttpUtil.get("/role/del/" + row.id, {}, (res) => {
            this.getData();
            this.$message.success('删除成功');
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