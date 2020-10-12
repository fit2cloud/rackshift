<template>
  <div class="container">
    <div class="machine-title">
      <div class="el-button-group batch-button">
        <el-button
            type="primary"
            icon="el-icon-delete"
            class="handle-del mr10"
            @click="delAllSelection"
        >{{ $t('batch_del') }}
        </el-button>
        <el-button
            type="primary"
            icon="el-icon-delete"
            class="handle-del mr10"
            @click="handleEdit({}, 'add')"
        >{{ $t('add') }}
        </el-button>
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
      <el-form labelPosition="top">
        <el-form-item :label="$t('name')">
          <el-input v-model="editObj.name"></el-input>
        </el-form-item>
        <el-form-item :label="$t('desc')">
          <el-input v-model="editObj.description"></el-input>
        </el-form-item>
        <el-form-item :label="$t('type')">
          <el-select v-model="editObj.type" :placeholder="$t('pls_select')">
            <el-option label="管理员" value="admin"></el-option>
            <el-option label="普通用户" value="user"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
    <el-button @click="editDialogVisible = false">{{ $t('cancel') }}</el-button>
    <el-button type="primary" @click="confirmEdit">{{ $t('confirm') }}</el-button>
            </span>
    </el-dialog>

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
    delAllSelection() {
      const length = this.multipleSelection.length;
      let str = '';
      this.delList = this.delList.concat(this.multipleSelection);
      for (let i = 0; i < length; i++) {
        str += this.multipleSelection[i].name + ' ';
      }
      let ids = _.map(this.delList, (item) => item.id);
      HttpUtil.post("/role/del", ids, (res) => {
        this.$message.success(`删除成功！删除了${str}！`);
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