<template>
  <div class="container">
    <div class="machine-title">
      <i class="el-icon-user-solid">{{ $t('User') }}</i>
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
      <el-table-column type="selection" align="center"></el-table-column>
      <el-table-column :prop="c.prop" :label="c.label" align="center"
                       v-for="c in columns"></el-table-column>
      <el-table-column prop="updateTime" :label="$t('update_time')" align="center">
        <template slot-scope="scope">
          {{ scope.row.updateTime | dateFormat }}
        </template>
      </el-table-column>
      <el-table-column prop="" :label="$t('opt')" align="center">
        <template slot-scope="scope">
          <el-button
              type="text"
              icon="el-icon-edit"
              @click="handleEdit(scope.row, 'edit')"
          >{{ $t('edit') }}
          </el-button>
          <el-button
              type="text"
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

    <el-drawer
        :title="editType == 'edit' ? '编辑用户' : '新增用户'"
        :visible.sync="drawer"
        direction="rtl"
        :before-close="handleClose">
      <div class="demo-drawer__content">
        <el-form :model="editObj">
          <el-form-item label="ID" :label-width="formLabelWidth">
            <el-input v-model="editObj.id" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item :label="$t('name')" :label-width="formLabelWidth">
            <el-input v-model="editObj.name" autocomplete="off"
                      :placeholder="$t('pls_input_name')"></el-input>
          </el-form-item>
          <el-form-item :label="$t('email')" :label-width="formLabelWidth">
            <el-input v-model="editObj.email" autocomplete="off"
                      :placeholder="$t('pls_input_email')"></el-input>
          </el-form-item>
          <el-form-item :label="$t('phone')" :label-width="formLabelWidth">
            <el-input v-model="editObj.phone" autocomplete="off"
                      :placeholder="$t('pls_input_phone')"></el-input>
          </el-form-item>
          <el-form-item :label="$t('role')" :label-width="formLabelWidth">
            <el-select v-model="editObj.rolesIds" multiple :placeholder="$t('pls_select')">
              <el-option
                  v-for="(item, key) in allRoles"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id">
              </el-option>
            </el-select>
          </el-form-item>
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
</template>

<script>
import HttpUtil from "../../common/utils/HttpUtil";

let _ = require('lodash');
export default {
  data() {
    return {
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
    this.getAllRoles();
  },
  methods: {
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
    add() {
      this.drawer = true;
      this.editType = 'add';
    },
    confirmEdit() {
      this.loading = true;
      if (this.editType == 'edit') {
        HttpUtil.post("/user/update", this.editObj, (res) => {
          this.drawer = false;
          this.$message.success('编辑成功');
          this.cancelForm();
          this.getData();
        })
      } else {
        HttpUtil.post("/user/add", this.editObj, (res) => {
          this.drawer = false;
          this.$message.success('新增成功');
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
      this.delList = this.delList.concat(this.multipleSelection);
      for (let i = 0; i < length; i++) {
        str += this.multipleSelection[i].name + ' ';
      }
      let ids = _.map(this.delList, (item) => item.id);
      HttpUtil.post("/user/del", ids, (res) => {
        this.$message.success(`删除成功！删除了${str}！`);
        this.getData();
      });
      this.multipleSelection = [];
    },
    // 编辑操作
    handleEdit(row, type) {
      if (type == 'edit') {
        this.drawer = true;
        this.editType = type;
        this.editObj = JSON.parse(JSON.stringify(row));
        this.editObj.rolesIds = _.map(this.editObj.roles, (item) => item.id);

      } else if (type == 'del') {
        this.$confirm('确定要删除吗？', '提示', {
          type: 'warning'
        }).then(() => {
          HttpUtil.get("/user/del/" + row.id, {}, (res) => {
            this.getData();
            this.$message.success('删除成功');
          });
        })
      } else {
        this.drawer = true;
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