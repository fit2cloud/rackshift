<template>
  <el-tabs class="t100vw" v-model="activeName">
    <el-tab-pane :label="$t('Network')" name="network">
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
            header-cell-class-name="table-header"
            v-loading="loadingList"
            style="width: 100%"
            @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" align="left"></el-table-column>
          <el-table-column prop="endpointId" :label="$t('endpoint')" align="left">
            <template slot-scope="scope">
              {{ scope.row.endpointId | endpointFormat }}
            </template>
          </el-table-column>

          <el-table-column :prop="c.prop" :formatter="getValidProText" :label="c.label" align="left"
                           v-for="c in columns" :sortable="c.sort"></el-table-column>

          <el-table-column :label="$t('dhcp_enable')" align="left">
            <template slot-scope="scope">
              {{ $t("enabled") }}
            </template>
          </el-table-column>

          <el-table-column prop="pxeEnable" :label="$t('pxe_enable')" align="left">
            <template slot-scope="scope">
              {{ getValidProText(null, null, scope.row.pxeEnable) }}
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
              <RSButton icon="el-icon-delete" @click="handleEdit(scope.row, 'del')" type="del"></RSButton>
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
            :title="editType == 'edit' ? $t('edit_network') : $t('add_network')"
            :visible.sync="editDialogVisible"
            direction="rtl"
            :wrapperClosable="false"
            :before-close="handleClose">
          <div class="demo-drawer__content">
            <el-form :model="editObj" :rules="rules" ref="form" label-position="top">

              <el-form-item :label="$t('endpoint')" prop="endpointId">
                <el-select v-model="editObj.endpointId" :placeholder="$t('pls_select')">
                  <el-option
                      v-for="(item, key) in allEndPoints"
                      :label="item.name"
                      :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>

              <el-form-item :label="$t('startIp')" prop="startIp">
                <el-input v-model="editObj.startIp" autocomplete="off"
                          :placeholder="$t('pls_input_start_ip')"></el-input>
              </el-form-item>

              <el-form-item :label="$t('endIp')" prop="endIp">
                <el-input v-model="editObj.endIp" autocomplete="off"
                          :placeholder="$t('pls_input_end_ip')"></el-input>
              </el-form-item>

              <el-form-item :label="$t('netmask')" prop="netmask">
                <el-input v-model="editObj.netmask" autocomplete="off"
                          :placeholder="$t('pls_input_netmask')"></el-input>
              </el-form-item>

              <el-form-item :label="$t('pxe_enable')" prop="pxeEnable">
                <el-switch
                    v-model="editObj.pxeEnable"
                    active-color="#13ce66"
                    @change="changePXE"
                    inactive-color="#ff4949">
                </el-switch>
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
import {ipValidator, requiredSelectValidator} from "@/common/validator/CommonValidator";

let _ = require('lodash');
export default {
  data() {
    return {
      activeName: 'network',
      rules: {
        endpointId: [
          {validator: requiredSelectValidator, trigger: 'blur', vue: this},
        ],
        startIp: [
          {validator: ipValidator, trigger: 'blur', vue: this},
        ],
        endIp: [
          {validator: ipValidator, trigger: 'blur', vue: this},
        ],
        netmask: [
          {validator: ipValidator, trigger: 'blur', vue: this},
        ],
      },
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
      idx: -1,
      id: -1,
      loading: false,
      enables: [
        {"id": "enable", "name": this.$t("enable")},
        {"id": "disable", "name": this.$t("disable")},
      ],
      columns: [
        {
          label: this.$t('name'),
          prop: "name",
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
      allOsVersion: [],
      allEndPoints: [],
      loadingList: false,
    };
  },
  mounted() {
    this.getData();
    this.getAllEndPoints();
  },
  methods: {
    changePXE(e) {
      if (this.editObj.pxeEnable) {
        this.editObj.dhcpEnable = true;
      }
    },
    getAllEndPoints() {
      HttpUtil.get("/endpoint/getAllEndPoints", {}, (res) => {
        this.allEndPoints = res.data;
        localStorage.setItem("allEndPoints", JSON.stringify(res.data));
      });
    },
    // 获取 easy-mock 的模拟数据
    getValidProText(row, column, cellValue, index) {
      if (cellValue === true) {
        return this.$t("enabled");
      } else if (cellValue === false) {
        return this.$t("disabled");
      }
      return cellValue;
    },
    getData() {
      this.loadingList = true;
      HttpUtil.post("/network/list/" + this.query.pageIndex + "/" + this.query.pageSize, {}, (res) => {
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

      this.validateResult = true;
      this.$refs['form'].validate((valid) => {
        if (!valid) {
          this.validateResult = false;
        }
      });
      if (!this.validateResult) {
        this.$message.error(this.$t('validate_error'));
        return;
      }
      this.loading = true;
      if (this.editType == 'edit') {
        HttpUtil.post("/network/update", this.editObj, (res) => {
          this.editDialogVisible = false;
          this.$message.success(this.$t('edit_success'));
          this.getData();
          this.loading = false;
        })
      } else {
        HttpUtil.post("/network/add", this.editObj, (res) => {
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
      let ids = this.getSelectedIds();
      if (!ids || ids.length == 0) {
        this.$message.error(this.$t('pls_select_network') + "!");
        return;
      }
      this.$confirm(this.$t('confirm_to_del'), this.$t('tips'), {
        type: 'warning'
      }).then(() => {
        HttpUtil.post("/network/del", ids, (res) => {
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
        this.$confirm(this.$t('confirm_to_del'), this.$t('tips'), {
          type: 'warning'
        }).then(() => {
          HttpUtil.get("/network/del/" + row.id, {}, (res) => {
            this.getData();
            this.$message.success(this.$t('delete_success!'));
          });
        })
      } else {
        this.editDialogVisible = true;
        this.editType = type;
        this.editObj = {
          pxeEnable: false,
          dhcpEnable: false,
          netmask: '255.255.255.0'
        };
      }
    },
    // 分页导航
    handlePageChange(val) {
      this.$set(this.query, 'pageIndex', val);
      this.getData();
    },
    getSelectedIds: function () {
      this.delList = [].concat(this.multipleSelection);
      let ids = _.map(this.delList, (item) => item.id);
      return ids;
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