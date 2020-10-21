<template>
  <el-tabs style="width:80vw;" v-model="activeName" @tab-click="refreshChildData">
    <el-tab-pane :label="$t('discovery')" name="discovery">
      <div class="container-discovery">

        <div class="machine-title">
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
            v-loading="loadingList"
            header-cell-class-name="table-header"
            style="width: 100%"
            @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" align="left"></el-table-column>

          <el-table-column :prop="c.prop" :label="c.label" align="left"
                           v-for="c in columns" sortable></el-table-column>
          <el-table-column prop="syncStatus" :label="$t('sync_status')" align="left">
            <template slot-scope="scope">
              <i class="el-icon-loading" v-if="scope.row.syncStatus.indexOf('ING') != -1"></i>
              {{ scope.row.syncStatus }}
            </template>
          </el-table-column>

          <el-table-column prop="lastSyncTimestamp" :label="$t('last_sync_timestamp')" align="left">
            <template slot-scope="scope">
              {{ scope.row.lastSyncTimestamp | dateFormat }}
            </template>
          </el-table-column>

          <el-table-column prop="" :label="$t('opt')" align="left">
            <template slot-scope="scope">
              <el-dropdown>
                <el-button type="primary">
                  {{ $t('opt') }}<i class="el-icon-arrow-down el-icon--right"></i>
                </el-button>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item @click.native="handleEdit(scope.row, 'edit')">{{ $t('edit') }}
                  </el-dropdown-item>
                  <el-dropdown-item v-if="scope.row.syncStatus.indexOf('ING') == -1"
                                    @click.native="handleEdit(scope.row, 'del')">{{ $t('del') }}
                  </el-dropdown-item>
                  <el-dropdown-item v-if="scope.row.syncStatus.indexOf('ING') == -1"
                                    @click.native="handleEdit(scope.row, 'sync')">{{ $t('sync') }}
                  </el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
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
            :title="editType == 'edit' ? $t('edit_discovery') : $t('add_discovery')"
            :visible.sync="editDialogVisible"
            direction="rtl"
            size="40%"
            :wrapperClosable="false"
            :before-close="handleClose">
          <div class="demo-drawer__content">
            <el-form :model="editObj" :rules="rules" ref="form" label-width="50px" :label-position="labelPosition">
              <el-form-item :label="$t('name')" prop="name">
                <el-input v-model="editObj.name" autocomplete="off"></el-input>
              </el-form-item>

              <el-form-item :label="$t('start_ip')" prop="startIp">
                <el-input v-model="editObj.startIp" autocomplete="off"></el-input>
              </el-form-item>

              <el-form-item :label="$t('end_ip')" prop="endIp">
                <el-input v-model="editObj.endIp" autocomplete="off"></el-input>
              </el-form-item>

              <el-form-item :label="$t('mask')" prop="mask">
                <el-input v-model="editObj.mask" autocomplete="off"></el-input>
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
    <el-tab-pane :label="$t('discoveryed_devices')" name="devices">
      <Devices ref="device"></Devices>
    </el-tab-pane>
  </el-tabs>
</template>

<script>

import HttpUtil from "../../common/utils/HttpUtil"
import {requiredValidator, ipValidator} from "@/common/validator/CommonValidator";
import {WebSocketUtil} from "@/common/utils/WebSocket";
import Devices from "../discovery-devices/Discovery-devices"

let _ = require('lodash');
export default {
  data() {
    return {
      activeName: 'discovery',
      rules: {
        name: [
          {validator: requiredValidator, trigger: 'blur', vue: this},
        ],
        startIp: [
          {validator: ipValidator, trigger: 'blur', vue: this},
        ],
        endIp: [
          {validator: ipValidator, trigger: 'blur', vue: this},
        ],
        mask: [
          {validator: ipValidator, trigger: 'blur', vue: this},
        ],
      },
      query: {
        name: '',
        pageIndex: 1,
        pageSize: 10
      },
      tableData: [],
      loadingList: [],
      multipleSelection: [],
      delList: [],
      editVisible: false,
      labelPosition: 'top',
      pageTotal: 0,
      idx: -1,
      id: -1,
      loading: false,
      columns: [
        {
          label: this.$t('name'),
          prop: "name",
          sort: true
        },
        {
          label: this.$t('start_ip'),
          prop: "startIp",
          sort: true
        },
        {
          label: this.$t('end_ip'),
          prop: "endIp",
          sort: true
        },
        {
          label: this.$t('mask'),
          prop: "mask",
          sort: true
        },
      ],
      editDialogVisible: false,
      editType: 'edit',
      editObj: {
        name: null,
      },
      allOs: [],
      allOsVersion: [],
      allEndPoints: [],
      fileName: null,
    };
  },
  computed: {
    action: function () {
      return '/discovery/upload?fileName=' + this.fileName;
    }
  },
  components: {
    Devices
  },
  mounted() {
    this.getData();
  },
  methods: {
    // 获取 easy-mock 的模拟数据
    getData() {
      this.loadingList = true;
      HttpUtil.post("/discovery/list/" + this.query.pageIndex + "/" + this.query.pageSize, {}, (res) => {
        this.tableData = res.data.listObject;
        this.pageTotal = res.data.itemCount;
        this.loadingList = false;
        WebSocketUtil.checkDoingThings(res.data.listObject, 'syncStatus', 'discovery', this.getData);
      });
    },
    refreshChildData() {
      this.$refs.device.getData();
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
      this.$refs.form.validate(f => {
        if (!f) {
          this.validateResult = false;
        }
      });
      if (!this.validateResult) {
        this.$notify.error("validate_error");
        return;
      }
      this.loading = true;
      this.editObj.brands = JSON.stringify(this.editObj.brands);
      if (this.editType == 'edit') {
        HttpUtil.post("/discovery/update", this.editObj, (res) => {
          this.editDialogVisible = false;
          this.$message.success(this.$t('edit_success'));
          this.getData();
          this.loading = false;
        })
      } else {
        HttpUtil.post("/discovery/add", this.editObj, (res) => {
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
    getSelectedIds: function () {
      this.delList = [].concat(this.multipleSelection);
      let ids = _.map(this.delList, (item) => item.id);
      return ids;
    },
    delAllSelection() {
      const length = this.multipleSelection.length;
      let str = '';
      for (let i = 0; i < length; i++) {
        str += this.multipleSelection[i].machineModel + ' ';
      }
      let ids = this.getSelectedIds();
      if (!ids || ids.length == 0) {
        this.$notify.error(this.$t('pls_select_discovery') + "!");
        return;
      }
      HttpUtil.post("/discovery/del", ids, (res) => {
        if (res.success) {
          this.$message.success(this.$t('delete_success'));
          this.getData();
        } else {
          this.$message.success(this.$t('delete_fail'));
        }
      });
      this.multipleSelection = [];
    },
    // 编辑操作
    handleEdit(row, type) {
      if (type == 'edit') {
        this.editDialogVisible = true;
        this.editType = type;
        this.editObj = JSON.parse(JSON.stringify(row));
        this.changeOsVersion();
      } else if (type == 'del') {
        this.$confirm(this.$t('confirm_to_del'), this.$t('tips'), {
          type: 'warning'
        }).then(() => {
          HttpUtil.get("/discovery/del/" + row.id, {}, (res) => {
            this.getData();
            this.$message.success(this.$t('delete_success!'));
          });
        })
      } else if (type == 'sync') {
        HttpUtil.post("/discovery/sync",
            [row.id]
            , (res) => {
              this.$message.success(this.$t('opt_success!'));
              this.getData();
            });
      } else {
        this.editDialogVisible = true;
        this.editType = type;
        this.editObj = {
          endpointId: _.find(this.allEndPoints, e => e.type == 'main_endpoint').id,
          url: null
        };
      }
    },
    // 分页导航
    handlePageChange(val) {
      this.$set(this.query, 'pageIndex', val);
      this.getData();
    },
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

.container-discovery {
  background: #fff;
  width: 100%;
  height: calc(100vh - 125px);
}

</style>