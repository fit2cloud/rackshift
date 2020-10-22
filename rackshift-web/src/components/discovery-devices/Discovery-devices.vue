<template>
  <div class="container">

    <div class="machine-title">
      <el-button-group class="batch-button">
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

      <el-table-column prop="createTime" :label="$t('create_time')" align="left">
        <template slot-scope="scope">
          {{ scope.row.createTime | dateFormat }}
        </template>
      </el-table-column>

      <el-table-column prop="" :label="$t('opt')" align="left">
        <template slot-scope="scope">
          <RSButton @click="handleEdit(scope.row, 'edit')"></RSButton>
          <RSButton @click="handleEdit(scope.row, 'del')" type="del"></RSButton>
          <RSButton @click="handleEdit(scope.row, 'addToDevice')" type="add" :tip="$t('add_to_device')"></RSButton>
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

    <el-dialog :title="$t('add_to_device')" :visible.sync="deviceDialogVisible">
      <el-form>
        <el-row>
          <el-col :span="deviceDialogWidth">
            <el-form-item :label="$t('machine_model')" :label-width="formLabelWidth">
              <el-input v-model="curObm.machineModel" autocomplete="off"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="deviceDialogWidth">
            <el-form-item :label="$t('management_ip')" :label-width="formLabelWidth">
              <el-input v-model="curObm.managementIp" autocomplete="off"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="deviceDialogWidth">
            <el-form-item :label="$t('endpoint')" :label-width="formLabelWidth">
              <el-select v-model="curObm.endpointId" :placeholder="$t('pls_select')">
                <el-option
                    v-for="(item, key) in allEndPoints"
                    :label="item.name"
                    :value="item.id">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="deviceDialogVisible = false">{{ $t('cancel') }}</el-button>
        <el-button type="primary" @click="submitOBM" :loading="obmLoading">{{ $t('confirm') }}</el-button>
      </div>
    </el-dialog>

    <el-drawer
        :title="editType == 'edit' ? $t('edit_discovery-devices') : $t('add_discovery-devices')"
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

          <el-form-item :label="$t('ip')" prop="ip">
            <el-input v-model="editObj.ip" autocomplete="off"></el-input>
          </el-form-item>

          <el-form-item :label="$t('description')" prop="description">
            <el-input v-model="editObj.description" autocomplete="off"></el-input>
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
</template>

<script>

import HttpUtil from "../../common/utils/HttpUtil"
import {ipValidator, requiredValidator} from "@/common/validator/CommonValidator";

let _ = require('lodash');
export default {
  data() {
    return {
      rules: {
        name: [
          {validator: requiredValidator, trigger: 'blur', vue: this},
        ],
        ip: [
          {validator: ipValidator, trigger: 'blur', vue: this},
        ],
        description: [
          {validator: requiredValidator, trigger: 'blur', vue: this},
        ],
      },
      deviceDialogWidth: 10,
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
      formLabelWidth: '80px',
      columns: [
        {
          label: this.$t('ip'),
          prop: "ip",
          sort: true
        },
        {
          label: this.$t('description'),
          prop: "description",
          sort: true
        },
      ],
      editDialogVisible: false,
      deviceDialogVisible: false,
      obmLoading: false,
      curObm: {},
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
      return '/discovery-devices/upload?fileName=' + this.fileName;
    }
  },
  mounted() {
    this.getData();
    this.getAllEndPoints();
  },
  methods: {
    // 获取 easy-mock 的模拟数据
    getData() {
      this.loadingList = true;
      HttpUtil.post("/discovery-devices/list/" + this.query.pageIndex + "/" + this.query.pageSize, {}, (res) => {
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
      this.$refs.form.validate(f => {
        if (!f) {
          this.validateResult = false;
        }
      });
      if (!this.validateResult) {
        this.$message.error(this.$t('validate_error'));
        return;
      }
      this.loading = true;
      this.editObj.brands = JSON.stringify(this.editObj.brands);
      if (this.editType == 'edit') {
        HttpUtil.post("/discovery-devices/update", this.editObj, (res) => {
          this.editDialogVisible = false;
          this.$message.success(this.$t('edit_success'));
          this.getData();
          this.loading = false;
        })
      } else {
        HttpUtil.post("/discovery-devices/add", this.editObj, (res) => {
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
        this.$message.error(this.$t('pls_select_discovery-devices') + "!");
        return;
      }
      HttpUtil.post("/discovery-devices/del", ids, (res) => {
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
      } else if (type == 'del') {
        this.$confirm(this.$t('confirm_to_del'), this.$t('tips'), {
          type: 'warning'
        }).then(() => {
          HttpUtil.get("/discovery-devices/del/" + row.id, {}, (res) => {
            this.getData();
            this.$message.success(this.$t('delete_success!'));
          });
        })
      } else if (type == 'addToDevice') {
        this.deviceDialogVisible = true;
        this.curObm = {
          managementIp: row.ip,
          machineModel: row.name,
          ruleId: row.bareMetalRuleId
        }
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
    submitOBM() {
      HttpUtil.post("discovery-devices/addToBareMetal", this.curObm, (res) => {
        if (res.data) {
          this.$message.success(this.$t("opt_success"));
        } else {
          this.$message.success(this.$t("opt_fail"));
        }
        this.deviceDialogVisible = false;
      });
    },
    getAllEndPoints() {
      HttpUtil.get("/endpoint/getAllEndPoints", {}, (res) => {
        this.allEndPoints = res.data;
        localStorage.setItem("allEndPoints", JSON.stringify(res.data));
      });
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

</style>