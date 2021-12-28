<template>
  <el-tabs class="t100vw" v-model="activeName">
    <el-tab-pane :label="$t('image')" name="image">
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

          <el-table-column prop="name" :label="$t('name')" align="left">
            <template slot-scope="scope">
              {{ scope.row.name }}
            </template>
          </el-table-column>

          <el-table-column prop="endpointId" :label="$t('endpoint')" align="left">
            <template slot-scope="scope">
              {{ scope.row.endpointId | endpointFormat }}
            </template>
          </el-table-column>

          <el-table-column prop="url" :label="$t('url')" align="left">
            <template slot-scope="scope">
              <el-link type="primary" @click="showUrl(scope.row.url)">{{ $t('show') }}</el-link>
            </template>
          </el-table-column>

          <el-table-column :prop="c.prop" :label="$t(c.label)" align="left"
                           v-for="c in columns" :sortable="c.sort">
            <template slot-scope="scope">
              <span v-if="!c.custom">{{ scope.row[c.prop] }}</span>
              <span v-if="c.custom">{{ c.formatter(scope.row[c.prop]) }}</span>
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
              <RSButton @click="handleEdit(scope.row, 'del')" type="del"></RSButton>
            </template>
          </el-table-column>
        </el-table>

        <el-drawer
            :title="editType == 'edit' ? $t('edit_image') : $t('add_image')"
            :visible.sync="editDialogVisible"
            direction="rtl"
            :wrapperClosable="false"
            :before-close="handleClose">
          <div class="demo-drawer__content">

            <el-form :model="editObj" :rules="rules" ref="form" label-width="50px" :label-position="labelPosition">
              <el-form-item :label="$t('name')" prop="name">
                <el-input v-model="editObj.name" autocomplete="off"></el-input>
              </el-form-item>

              <el-form-item :label="$t('os')" prop="os">
                <el-select v-model="editObj.os" :placeholder="$t('pls_select')" v-on:change="changeOsVersion"
                           class="input-element">
                  <el-option
                      v-for="(item, key) in allOs"
                      :key="item.id"
                      :label="$t(item.name)"
                      :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="$t('os_version')" prop="osVersion">
                <el-select v-model="editObj.osVersion" :placeholder="$t('pls_input_os_version')" class="input-element">
                  <el-option
                      v-for="(item, key) in allOsVersion"
                      :key="item.name"
                      :label="item.name"
                      :value="item.name">
                  </el-option>
                </el-select>
              </el-form-item>

              <el-form-item :label="$t('profile') + $t('not_required')" prop="profile">
                <el-select v-model="editObj.profileId" :placeholder="$t('pls_select') + $t('profile')"
                           class="input-element">
                  <el-option
                      v-for="(item, key) in allProfiles"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>

              <el-form-item :label="$t('template') + $t('not_required')" prop="template">
                <el-select v-model="editObj.templateId" :placeholder="$t('pls_select') + $t('template')"
                           class="input-element">
                  <el-option
                      v-for="(item, key) in allTemplates"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>

              <el-tabs v-model="activeTypeName" @tab-click="handleChangeType" v-if="editType != 'edit'">
                <el-tab-pane :label="$t('front_upload')" name="front">
                  <el-form-item :label="$t('image')" v-if="editType != 'edit'">
                    <el-upload
                        class="upload-demo"
                        drag
                        ref="uploader"
                        :before-upload="beforeUpload"
                        :on-success="afterUploadSuccess"
                        action="/image/upload"
                        style="margin-bottom: 20px;"
                        :multiple=false
                        :limit="1"
                        :disabled="editType == 'edit'"
                    >
                      <i class="el-icon-upload"></i>
                      <div class="el-upload__text">{{ $t('drag_file_into_or') }}<em>{{ $t('click_to_upload') }}</em>
                      </div>
                      <div class="el-upload__tip" slot="tip">{{ $t('only_iso_no_more_than_10g') }}
                        <em class="upload-tip">{{ $t('upload_success_submit') }}</em>
                      </div>
                    </el-upload>
                  </el-form-item>
                </el-tab-pane>
                <el-tab-pane :label="$t('backend_upload')" name="backend">

                  <el-form-item :label="$t('url')" prop="name">
                    <div class="el-upload__text">{{ $t('mount_text') }}
                    </div>
                    <el-input v-model="editObj.url" autocomplete="off"></el-input>
                  </el-form-item>

                </el-tab-pane>

              </el-tabs>
              <el-form-item :label="$t('url')" prop="name" v-if="editType == 'edit'">
                <div class="el-upload__text">{{ $t('mount_text') }}
                </div>
                <el-input v-model="editObj.url" autocomplete="off"></el-input>
              </el-form-item>

            </el-form>
            <div class="demo-drawer__footer">
              <el-button @click="editDialogVisible = false">{{ $t('cancel') }}</el-button>
              <el-button type="primary" @click="confirmEdit" :loading="loading" :disabled="!canConfirm"
                         v-if="editType != 'edit'">{{
                  loading ? $t('submitting') +
                      '...' : $t('SUBMIT')
                }}
              </el-button>

              <el-button type="primary" @click="confirmEdit" :loading="loading" v-if="editType == 'edit'">{{
                  loading ? $t('submitting') +
                      '...' : $t('SUBMIT')
                }}
              </el-button>
            </div>
          </div>
        </el-drawer>

        <el-dialog
            :title="$t('view_url')"
            :visible.sync="showUrlVisible"
            width="60%"
            :before-close="handleClose">
          <el-row>
            <el-col :span="24">
              <span>{{ currentUrl }} </span>
              <el-link type="primary" :underline="false" @click="copyUrl($event)"><i class="el-icon-copy-document"></i>
              </el-link>
            </el-col>
          </el-row>
        </el-dialog>

      </div>
    </el-tab-pane>
  </el-tabs>

</template>

<script>

import HttpUtil from "../../common/utils/HttpUtil"
import {requiredSelectValidator, requiredValidator} from "@/common/validator/CommonValidator";

let _ = require('lodash');

export default {
  data() {
    return {
      allTemplates: [],
      allProfiles: [],
      activeName: 'image',
      activeTypeName: 'front',
      canConfirm: false,
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
      },
      query: {
        name: '',
        pageIndex: 1,
        pageSize: 10
      },
      showUrlVisible: false,
      currentUrl: null,
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
          label: 'os',
          prop: "os"
        },
        {
          label: 'os_vesion',
          prop: "osVersion"
        },
        // {
        //   label: 'status',
        //   prop: "status",
        //   custom: true,
        //   statusMap: {
        //     "not_detected": this.$t('not_detected'),
        //     "detected": this.$t('detected'),
        //     "error": this.$t('error'),
        //   },
        //   formatter: function (item) {
        //     if (item) {
        //       return this.statusMap[item];
        //     }
        //     return item;
        //   }
        // },
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
    this.getAllProfiles();
    this.getAllTemplates();
  },
  methods: {
    copyUrl(e) {
      e.preventDefault();
      var content = this.currentUrl;
      let oInput = document.createElement('input')
      oInput.value = content;
      document.body.appendChild(oInput);
      oInput.select();
      document.execCommand("Copy");
      oInput.style.display = 'none';
      document.body.removeChild(oInput);
      this.$message.success(this.$t('copy_success'));
    },
    showUrl(url) {
      this.showUrlVisible = true;
      this.currentUrl = url;
    },
    getAllEndPoints() {
      HttpUtil.get("/endpoint/getAllEndPoints", {}, (res) => {
        this.allEndPoints = [].concat(_.find(res.data, (e) => e.type == 'main_endpoint'));

        localStorage.setItem("allEndPoints", JSON.stringify(this.allEndPoints));
      });
    },
    getAllProfiles() {
      HttpUtil.get("/profile/getAllProfiles", {}, (res) => {
        this.allProfiles = res.data;

        localStorage.setItem("allProfiles", JSON.stringify(this.allProfiles));
      });
    },
    getAllTemplates() {
      HttpUtil.get("/template/getAllTemplates", {}, (res) => {
        this.allTemplates = res.data;

        localStorage.setItem("allTemplates", JSON.stringify(this.allTemplates));
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
      this.handlePageChange(this.query.pageIndex);
    },
    changeOsVersion() {
      this.allOsVersion = _.find(this.allOs, {"id": this.editObj.os}).versions;
      this.editObj.osVersion = null;
    },
    handleChangeType() {
      if (this.activeTypeName == 'backend') {
        this.canConfirm = true;
      } else {
        this.canConfirm = false;
      }
    },
    handleClose() {
      this.editDialogVisible = false;
      this.showUrlVisible = false;
    },
    add() {
      this.editDialogVisible = true;
      this.editType = 'add';
      this.canConfirm = false;
    },
    confirmEdit() {
      this.validateResult = true;
      this.$refs.form.validate((f) => {
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
        HttpUtil.put("/image/update", this.editObj, (res) => {
          this.editDialogVisible = false;
          this.canConfirm = false;
          this.$message.success(this.$t('edit_success'));
          this.getData();
          this.loading = false;
        })
      } else {
        HttpUtil.post("/image/add", this.editObj, (res) => {
          if (res.success) {
            this.editDialogVisible = false;
            this.$message.success(this.$t('add_success'));
            this.getData();
          } else {
            this.$message.error(this.$t('upload') + this.$t('failed'));
          }
          this.$refs.uploader.clearFiles();
          this.loading = false;
          this.canConfirm = true;
        }, null, (e) => {
          this.$message.error(this.$t('opt_fail'));
          this.loading = false;
          this.canConfirm = true;
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
        this.$message.error(this.$t('pls_select_image') + "!");
        return;
      }
      this.$confirm(this.$t('confirm_to_del'), this.$t('tips'), {
        type: 'warning'
      }).then(() => {
        this.loadingList = true;
        HttpUtil.post("/image/del", ids, (res) => {
          if (res.success) {
            this.$message.success(this.$t('delete_success'));
            this.getData();
            this.multipleSelection = [];
            this.loadingList = false;
          } else {
            this.$message.success(this.$t('delete_fail'));
          }
        }, (e) => {
          this.$message.success(this.$t('delete_fail'));
          this.loadingList = false;
        });

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
        this.$confirm(this.$t('confirm_to_del'), this.$t('tips'), {
          type: 'warning'
        }).then(() => {
          this.loadingList = true;
          HttpUtil.delete("/image/del/" + row.id, {}, (res) => {
            this.getData();
            this.$message.success(this.$t('delete_success!'));
            this.loadingList = false;
          }, (res) => {
            this.$message.error(this.$t('delete_fail!'));
            this.loadingList = false;
          });
        })
      } else {
        this.editDialogVisible = true;
        this.editType = type;
        if (this.$refs.form) {
          this.$refs.form.resetFields();
        }
        let endpoint = _.find(this.allEndPoints, e => e.type == 'main_endpoint');
        this.editObj = {
          endpointId: endpoint ? endpoint.id : null,
          url: null,
          os: null,
          osVersion: null
        };
      }
    },
    resetFields() {
      if (this.$refs.form) {
        this.$refs.form.resetFields();
      }
    },
    // 分页导航
    handlePageChange(val) {
      this.$set(this.query, 'pageIndex', val);
      this.getData();
    },
    afterUploadSuccess(response) {
      this.editObj.originalName = response.originalName;
      this.editObj.filePath = response.filePath;
      this.canConfirm = true;
    },
    beforeUpload(file) {
      this.canConfirm = false;
      return true;
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

.upload-tip {
  color: red;
  font-style: normal;
}
</style>