<template>

  <el-tabs style="width:80vw;" v-model="activeName">
    <el-tab-pane :label="$t('ExecutionLog')" name="execution-log">
      <div class="container">

        <div class="machine-title">
          <div class="el-button-group batch-button">
            <button type="button" class="el-button el-button--primary" @click="delAllSelection"><i
                class="el-icon-delete"></i>{{ $t('delete') }}
            </button>
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
          <el-table-column :prop="c.prop" :formatter="getValidProText" :label="c.label" align="left"
                           v-for="c in columns" :sortable="c.sort"></el-table-column>
          <el-table-column prop="status" :label="$t('status')" align="left">
            <template slot-scope="scope">
              {{ $t(scope.row.status) }}
            </template>
          </el-table-column>
          <el-table-column prop="createTime" :label="$t('create_time')" align="left">
            <template slot-scope="scope">
              {{ scope.row.createTime | dateFormat }}
            </template>
          </el-table-column>
          <el-table-column prop="" :label="$t('opt')" align="left">
            <template slot-scope="scope">
              <RSButton @click="viewDetail(scope.row)" :tip="$t('view')"></RSButton>
              <RSButton @click="handleEdit(scope.row, 'del')" type="del"></RSButton>
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
            size="50%"
            :title="editType == 'edit' ? $t('view_execution_log') : $t('add_execution_log')"
            :visible.sync="editDialogVisible"
            :wrapperClosable="false"
            direction="ttb"
            :before-close="handleClose">
          <div class="demo-drawer__content">
            <el-card>
              <table class="detail-info">
                <tr>
                  <td>{{ $t('time') }}</td>
                  <td>{{ $t('user') }}</td>
                  <td>{{ $t('status') }}</td>
                  <td>{{ $t('output') }}</td>
                </tr>
                <tr v-for="l in detailLogs">
                  <td>{{ l.createTime | dateFormat }}</td>
                  <td>{{ l.user }}</td>
                  <td>{{ $t(l.operation) }}</td>
                  <td>{{ l.outPut }}</td>
                </tr>
              </table>
            </el-card>
            <div class="demo-drawer__footer fr">
              <el-button @click="editDialogVisible = false">{{ $t('close') }}</el-button>
            </div>
          </div>
        </el-drawer>

      </div>
    </el-tab-pane>
  </el-tabs>


</template>

<script>

import HttpUtil from "../../common/utils/HttpUtil"

let _ = require('lodash');
export default {
  data() {
    return {
      activeName: 'execution-log',
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
      loading: false,
      enables: [
        {"id": "enable", "name": this.$t("enable")},
        {"id": "disable", "name": this.$t("disable")},
      ],
      columns: [
        {
          label: this.$t('user'),
          prop: "user",
          sort: true
        }
      ],
      editDialogVisible: false,
      editType: 'edit',
      editObj: {
        name: null,
        description: null,
        type: null
      },
      allOs: [],
      detailLogs: [],
      allOsVersion: []
    };
  },
  mounted() {
    this.getData();
  },
  methods: {
    // 获取 easy-mock 的模拟数据
    getValidProText(row, column, cellValue, index) {
      if (cellValue == true) {
        return this.$t("enabled");
      } else if (cellValue && cellValue == false) {
        return this.$t("disabled");
      }
      return cellValue;
    },
    getData() {
      this.loadingList = true;
      HttpUtil.post("/execution-log/list/" + this.query.pageIndex + "/" + this.query.pageSize, {}, (res) => {
        this.tableData = res.data.listObject;
        this.pageTotal = res.data.itemCount;
        this.loadingList = false;
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
      this.editDialogVisible = false;
    },
    // 多选操作
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },

    delAllSelection() {
      this.$confirm(this.$t('confirm_to_del'), this.$t('tips'), {
        type: 'warning'
      }).then(() => {
        let ids = this.getSelectedIds();
        if (!ids || ids.length == 0) {
          this.$message.error(this.$t('pls_select_network') + "!");
          return;
        }
        HttpUtil.post("/execution-log/del", ids, (res) => {
          this.$message.success(this.$t('delete_success'));
          this.getData();
        });
        this.multipleSelection = [];
      });
    },

    viewDetail(row) {
      HttpUtil.get("/execution-log/getDetailById/" + row.id, {}, (res) => {
        this.detailLogs = res.data;
        this.editDialogVisible = true;
      });
    },
    getSelectedIds: function () {
      this.delList = [].concat(this.multipleSelection);
      let ids = _.map(this.delList, (item) => item.id);
      return ids;
    },
// 编辑操作
    handleEdit(row, type) {
      if (type == 'edit') {
        this.editDialogVisible = true;
        this.editType = type;
        this.editObj = JSON.parse(JSON.stringify(row));
      } else if (type == 'del') {
        this.$confirm(this.$t('confirm_to_del'), this.$t('tips'), {
          type: 'warning'
        }).then(() => {
          HttpUtil.get("/execution-log/del/" + row.id, {}, (res) => {
            this.getData();
            this.$message.success(this.$t('delete_success!'));
          });
        })
      } else {
        this.editDialogVisible = true;
        this.editType = type;
        this.editObj = {};
      }
    }
    ,
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

.detail-info {
  border: 1px solid #EBEEF5;
  text-align: center;
  border-spacing: 0px !important;
}

.detail-info td {
  border: 1px solid #EBEEF5;
  border-bottom: none;
  border-right: none;
  padding: 12px 0;
  min-width: 0;
  box-sizing: border-box;
  text-overflow: ellipsis;
  vertical-align: middle;
  position: relative;
  text-align: center;
}

.fr {
  float: right;
}
</style>