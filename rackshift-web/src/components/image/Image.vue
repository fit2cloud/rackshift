<template>
  <div class="container">

    <div class="machine-title">
      <i class="el-icon-user-solid">{{ $t('Images') }}</i>

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
      <el-table-column type="selection" align="center"></el-table-column>
      <el-table-column prop="endpointId" :label="$t('endpoint')" align="center">
        <template slot-scope="scope">
          {{ scope.row.endpointId | endpointFormat }}
        </template>
      </el-table-column>

      <el-table-column :prop="c.prop" :label="c.label" align="center"
                       v-for="c in columns" sortable></el-table-column>
      <el-table-column prop="updateTime" :label="$t('update_time')" align="center">
        <template slot-scope="scope">
          {{ scope.row.updateTime | dateFormat }}
        </template>
      </el-table-column>

      <el-table-column prop="" :label="$t('opt')" align="center">
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
        :title="editType == 'edit' ? $t('edit_image') : $t('add_image')"
        :visible.sync="editDialogVisible"
        direction="rtl"
        :before-close="handleClose">
      <div class="demo-drawer__content">
        <el-form :model="editObj" :rules="rules" ref="form">
          <el-form-item :label="$t('name')" prop="name">
            <el-input v-model="editObj.name" autocomplete="off"></el-input>
          </el-form-item>

          <!--          <el-form-item :label="$t('endpoint')">-->
          <!--            <el-select v-model="editObj.endpointId" :placeholder="$t('pls_select')">-->
          <!--              <el-option-->
          <!--                  v-for="(item, key) in allEndPoints"-->
          <!--                  :label="item.name"-->
          <!--                  :value="item.id">-->
          <!--              </el-option>-->
          <!--            </el-select>-->
          <!--          </el-form-item>-->

          <el-form-item :label="$t('url')" prop="url">
            <el-input v-model="editObj.url" autocomplete="off"
                      :placeholder="$t('pls_input_url')"></el-input>
          </el-form-item>

          <el-form-item :label="$t('os')" prop="os">
            <el-select v-model="editObj.os" :placeholder="$t('pls_select')" v-on:change="changeOsVersion">
              <el-option
                  v-for="(item, key) in allOs"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item :label="$t('os_version')" prop="osVersion">
            <el-select v-model="editObj.osVersion" :placeholder="$t('pls_input_os_version')">
              <el-option
                  v-for="(item, key) in allOsVersion"
                  :key="item.name"
                  :label="item.name"
                  :value="item.name">
              </el-option>
            </el-select>
          </el-form-item>

          <el-upload
              class="upload-demo"
              drag
              :on-success="afterUploadSuccess"
              action="/image/upload"
              accept="application/x-iso9660-image"
              :multiple=false
              style="margin-bottom: 20px;"
          >
            <i class="el-icon-upload"></i>
            <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
            <div class="el-upload__tip" slot="tip">只能上传ISO文件，且不超过10GB</div>
          </el-upload>

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
import {requiredValidator, requiredSelectValidator} from "@/common/validator/CommonValidator";

let _ = require('lodash');
export default {
  data() {
    return {
      rules: {
        name: [
          {validator: requiredValidator, trigger: 'blur', vue: this},
        ],
        os: [
          {validator: requiredSelectValidator, trigger: 'blur', vue: this},
        ],
        osVersion: [
          {validator: requiredSelectValidator, trigger: 'blur', vue: this},
        ],
        url: [
          {validator: requiredValidator, trigger: 'blur', vue: this, msg: this.$t('please_input_mounted_or_upload')},
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
          label: this.$t('url'),
          prop: "url"
        },
        {
          label: this.$t('os'),
          prop: "os"
        },
        {
          label: this.$t('os_vesion'),
          prop: "osVersion"
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
      return '/image/upload?fileName=' + this.fileName;
    }
  },
  mounted() {
    this.getData();
    this.getAllOsAndVersion();
    this.getAllEndPoints();
  },
  methods: {
    getAllEndPoints() {
      HttpUtil.get("/endpoint/getAllEndPoints", {}, (res) => {
        this.allEndPoints = [].concat(_.find(res.data, (e) => e.type == 'main_endpoint'));

        localStorage.setItem("allEndPoints", JSON.stringify(this.allEndPoints));
      });
    },
    // 获取 easy-mock 的模拟数据
    getData() {
      this.loadingList = true;
      HttpUtil.post("/image/list/" + this.query.pageIndex + "/" + this.query.pageSize, {}, (res) => {
        this.tableData = res.data.listObject;
        this.pageTotal = res.data.itemCount;
        this.loadingList = false;
      });

    },
    getAllOsAndVersion() {
      HttpUtil.get("/rackhd/allOsAndVersion", {}, (res) => {
        this.allOs = res.data;
      });
    },
    handleSizeChange(val) {
      this.query.pageSize = val;
    },
    changeOsVersion() {
      this.allOsVersion = _.find(this.allOs, {"id": this.editObj.os}).versions;
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
        HttpUtil.post("/image/update", this.editObj, (res) => {
          this.editDialogVisible = false;
          this.$message.success(this.$t('edit_success'));
          this.getData();
          this.loading = false;
        })
      } else {
        HttpUtil.post("/image/add", this.editObj, (res) => {
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
        this.$notify.error(this.$t('pls_select_image') + "!");
        return;
      }
      HttpUtil.post("/image/del", ids, (res) => {
        this.$message.success(this.$t('delete_success'));
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
        this.changeOsVersion();
      } else if (type == 'del') {
        this.$confirm(this.$t('confirm_to_del'), this.$t('tips'), {
          type: 'warning'
        }).then(() => {
          HttpUtil.get("/image/del/" + row.id, {}, (res) => {
            this.getData();
            this.$message.success(this.$t('delete_success!'));
          });
        })
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
    afterUploadSuccess(response) {
      this.editObj.url = response.data.url;
      this.editObj.mountPath = response.data.mountPath;
      this.editObj.filePath = response.data.filePath;
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