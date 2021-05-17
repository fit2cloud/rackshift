<template>
  <el-tabs v-model="activeName" style="width:100vw;min-width: 1300px;">
    <el-tab-pane :label="$t('switch')" name="switch">
      <div class="machine-title2">
        <el-button-group class="batch-button">
          <el-button type="primary" icon="el-icon-delete" @click="delAllSelection">{{ $t('del') }}
          </el-button>
          <el-button type="primary" icon="el-icon-refresh" @click="getData">{{ $t('refresh') }}</el-button>
          <el-button type="primary" icon="el-icon-discover" @click="openDiscover">{{ $t('Discovery') }}</el-button>
        </el-button-group>
      </div>

      <el-table
          :data="tableData"
          class="table"
          ref="multipleTable"
          v-loading="loadingList"
          header-cell-class-name="table-header"
          style="width: 100%"
          @sort-change="sortChange($event)"
          @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" align="left"></el-table-column>

        <el-table-column :prop="c.prop" :label="$t(c.label)" align="left" v-for="c in columns" sortable="custom"
                         :width="resizeWith(c)">
          <template slot-scope="scope">
            <span v-if="!c.custom">{{ scope.row[c.prop] }}</span>
            <span v-if="c.custom">{{ c.formatter(scope.row[c.prop]) }}</span>
          </template>
        </el-table-column>

        <el-table-column prop="vendor" :label="$t('vendor')" align="left">
          <template slot-scope="scope">
            <el-link type="primary" @click="showVendor(scope.row.vendor)">{{ $t('show') }}</el-link>
          </template>
        </el-table-column>

        <el-table-column prop="active_ports" :label="$t('active_ports')" align="left">
          <template slot-scope="scope">
            <el-link type="primary" @click="showActivePorts(scope.row.id)">{{ $t('show') }}</el-link>
          </template>
        </el-table-column>

        <el-table-column prop="createTime" :label="$t('create_time')" align="left">
          <template slot-scope="scope">
            {{ scope.row.createTime | dateFormat }}
          </template>
        </el-table-column>


        <el-table-column prop="updateTime" :label="$t('update_time')" align="left">
          <template slot-scope="scope">
            {{ scope.row.updateTime | dateFormat }}
          </template>
        </el-table-column>

        <el-table-column prop="" :label="$t('opt')" align="left">
          <template slot="header" slot-scope="scope">
            <el-input
                v-model="queryVO.searchKey"
                size="mini"
                :placeholder="$t('input_key_search_switch')" v-on:change="getData()"/>
          </template>
          <template slot-scope="scope">
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

    </el-tab-pane>

    <el-dialog
        :title="$t('view_vendor')"
        :visible.sync="showVendorVisible"
        width="60%"
        :before-close="handleClose">
      <el-row>
        <el-col :span="24">
          <span>{{ currentVendor }} </span>
        </el-col>
      </el-row>
    </el-dialog>

    <el-drawer
        :title="editType == 'edit' ? $t('edit_discovery') : $t('add_discovery')"
        :visible.sync="discoveryVisible"
        direction="rtl"
        size="100%"
        :with-header="false"
        :wrapperClosable="false"

        :before-close="handleClose">
      <Discovery class="bare-discovery" @back="discoveryVisible =false" @queryByRuleId="queryByRuleId"
                 ref="discoveryCom"></Discovery>
    </el-drawer>


    <drawer
        :visible.sync="detailDrawer"
        direction="rtl"
        :with-header="false"
        size="50%"
        :before-close="handleClose">

      <div class="demo-drawer__content">
        <el-tabs v-model="detailShowName">
          <el-tab-pane :label="$t('port_detail')" name="portDetail" class="detail-pane">
            <table class="detail-info">
              <tr>
                <td>{{ $t('port') }}</td>
                <td>{{ $t('mac') }}</td>
                <td>{{ $t('switch_name') }}</td>
                <td>{{ $t('Bare Metal Server') + $t('machine_brand') }}</td>
                <td>{{ $t('Bare Metal Server') + $t('machine_model') }}</td>
                <td>{{ $t('Bare Metal Server') + $t('machine_sn') }}</td>
                <td>{{ $t('create_time') }}</td>
                <td>{{ $t('update_time') }}</td>
              </tr>
              <tr v-for="c in switchPorts">
                <td>{{ c.port }}</td>
                <td>{{ c.mac }}</td>
                <td>{{ c.switchName }}</td>
                <td>{{ c.machineBrand }}</td>
                <td>{{ c.machineModel }}</td>
                <td>{{ c.machineSn }}</td>
                <td>{{ c.createTime | dateFormat }}</td>
                <td>{{ c.updateTime | dateFormat }}</td>
              </tr>
            </table>
          </el-tab-pane>
        </el-tabs>
      </div>
    </drawer>
  </el-tabs>
</template>

<script>
import HttpUtil from "../../common/utils/HttpUtil";
import OBM from "../obm/Obm"
import Discovery from "../switch-discovery/SwitchDiscovery"
import Vue from "vue"
import PowerStatus from '../../common/powerstatus/Power-Status'
import {ipValidator, maskValidator, requiredValidator} from "@/common/validator/CommonValidator";

let _ = require('lodash');
export default {
  data() {
    return {
      switchPorts: [],
      currentVendor: null,
      showVendorVisible: false,
      directCom: true,
      ipmipwdLoading: false,
      changeObm: false,
      rules: {
        gateway: [
          {validator: ipValidator, trigger: 'blur', vue: this},
        ],
        startIp: [
          {validator: ipValidator, trigger: 'blur', vue: this},
        ],
        endIp: [
          {validator: ipValidator, trigger: 'blur', vue: this},
        ],
        netmask: [
          {validator: maskValidator, trigger: 'blur', vue: this},
        ],
        pwd: [
          {validator: requiredValidator, trigger: 'blur', vue: this},
        ],
        confirmPwd: [
          {validator: requiredValidator, trigger: 'blur', vue: this},
        ],
      },
      fillOBMMode: 'single',
      activeNames: 'commonOs',
      batchParamsLoading: false,
      allNicNumber: [],
      batchParams: false,
      bp: {
        hostname: null,
        increase: null,
        baseNumber: null,
        rootPassword: null,
        startIp: null,
        endIp: null,
        gateway: null,
        netmask: null,
        nicNumber: null,
        domain: null,
        username: null,
        smbRepo: null,
      },
      accurate: false,
      groupedSupportedWorkflow: [],
      discoveryVisible: false,
      fillWfParams: false,
      executionLogDrawer: false,
      activeName: 'switch',
      logTitle: '',
      loadingLog: false,
      currentMachine: {},
      form: {},
      logs: [],
      query: {
        address: '',
        name: '',
        pageIndex: 1,
        pageSize: 10
      },
      tableData: [],
      multipleSelection: [],
      delList: [],
      editVisible: false,
      formLabelWidth: '160px',
      pageTotal: 0,
      loading: false,
      columns: [
        {
          label: 'ip',
          prop: "ip"
        },
        {
          label: 'name',
          prop: "name"
        },
        {
          label: 'snmp_community',
          prop: "snmpCommunity"
        },
        {
          label: 'snmp_port',
          prop: "snmpPort"
        },
      ],
      detailDrawer: false,
      actionDrawer: false,
      editType: 'edit',
      editObj: {
        name: null,
        id: null,
        email: null,
        phone: null,
        roles: [],
        rolesIds: []
      },
      allGraphDefinitions: [],
      supportedWorkflow: [],
      wfRequest: {},
      workflowParamList: [],
      paramEditable: false,
      selectedWorkflow: [],
      selectedWorkflowComponent: {},
      fillOutObms: false,
      curObm: {
        ip: null,
        userName: null,
        pwd: null,
        bareMetalId: null
      },
      obmLoading: false,
      fillWfParamsLoading: false,
      machine: {},
      detailShowName: 'portDetail',
      cpuLoading: false,
      cpus: [],
      memories: [],
      disks: [],
      nics: [],
      paramComponent: {},
      currentWfParamTemplate: {},
      editWorkflowIndex: -1,
      queryVO: {
        searchKey: null
      },
      logPoller: null,
      currentParamConfig: '',
      loadingList: false,
      websocket: null,
      websocketInterval: null
    }
  },
  components: {
    OBM, Discovery, PowerStatus
  },
  computed: {},
  destroyed() {
    if (this.websocket) {
      this.websocket.close();
    }
    if (this.websocketInterval) {
      clearInterval(this.websocketInterval);
    }
  },
  mounted() {
    this.getData();
  }
  ,
  methods: {
    queryByRuleId(e) {
      if (e) {
        this.queryVO.searchKey = e;
        this.getData();
      }
    },
    openDiscover() {
      this.discoveryVisible = true;
      if (this.$refs.discoveryCom)
        this.$refs.discoveryCom.getData();
    },
    showVendor(vendor) {
      this.showVendorVisible = true;
      this.currentVendor = vendor;
    },
    getSwitchPorts(id) {
      HttpUtil.get("/switch/" + id + "/ports/", null, (res) => {
        this.switchPorts = res.data;
      })
    },
    showActivePorts(switchId) {
      this.detailDrawer = true;
      this.getSwitchPorts(switchId);
    },
    handleChange(val) {

    },
    resizeWith(c) {
      return (c.expandLanguage && c.expandLanguage == localStorage.getItem('lang')) ? '130px' : '140px';
    }
    ,
    sortChange(val) {
      if (val.order) {
        this.queryVO.sort = val.prop + " " + val.order.replace("ending", "");
      } else {
        delete this.queryVO.sort;
      }
      this.getData();
    }
    ,
    getData() {
      this.loadingList = true;
      HttpUtil.post("/switch/list/" + this.query.pageIndex + "/" + this.query.pageSize, this.queryVO, (res) => {
        this.tableData = res.data.listObject;
        this.pageTotal = res.data.itemCount;
        this.loadingList = false;
      });
    }
    ,
    handleSizeChange(val) {
      this.query.pageSize = val;
      this.handlePageChange(this.query.pageIndex);
    }
    ,
    handleClose() {
      this.detailDrawer = false;
      this.actionDrawer = false;
      this.discoveryVisible = false;
      this.showVendorVisible = false;
    }
    ,
    handleCloseExecutionLog() {
      this.executionLogDrawer = false;
      window.clearInterval(this.logPoller);
    }
    ,
    cancelForm() {
      this.loading = false;
      this.detailDrawer = false;
    }
    ,
    // 多选操作
    handleSelectionChange(val) {
      this.multipleSelection = val;
    }
    ,
    getSelectedIds: function () {
      this.delList = [].concat(this.multipleSelection);
      let ids = _.map(this.delList, (item) => item.id);
      return ids;
    }
    ,
    delAllSelection() {
      let ids = this.getSelectedIds();
      if (!ids || ids.length == 0) {
        this.$message.error(this.$t('pls_select_') + this.$t('Bare Metal Server') + "!");
        return;
      }
      this.$confirm(this.$t('confirm_to_del'), this.$t('tips'), {
        type: 'warning'
      }).then(() => {
        this.loadingList = true;
        HttpUtil.post("/switch/del", ids, (res) => {
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
      })
    }
    ,
    // 编辑操作
    handleEdit(row, type) {
      if (type == 'edit') {
        this.detailDrawer = true;
        this.editType = type;
        this.editObj = JSON.parse(JSON.stringify(row));
        this.editObj.rolesIds = _.map(this.editObj.roles, (item) => item.id);

      } else if (type == 'del') {
        this.$confirm(this.$t('confirm_to_del'), this.$t('tips'), {
          type: 'warning'
        }).then(() => {
          HttpUtil.post("/switch/del/" + row.id, {}, (res) => {
            this.getData();
            this.$message.success(this.$t('delete_success!'));
          });
        })
      } else {
        this.detailDrawer = true;
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
    ,
    getAllGraphDefinitions(name) {
      HttpUtil.get("/workflow/listall", {name: name}, (res) => {
        if (res.data && res.data.length > 0) {
          res.data.forEach(w => {
            w.brands = eval(w.brands);
            w.settable = eval(w.settable);
            if (w.defaultParams) w.defaultParams = JSON.parse(w.defaultParams);
          });
        }
        this.supportedWorkflow = res.data;
        this.groupedSupportedWorkflow = this.groupWorkflow(res.data);
      });
    }
    ,
    groupWorkflow(data) {
      let map = _.groupBy(data, 'eventType');
      let r = [];
      let that = this;
      Object.keys(map).forEach(o => {
        r.push({
              key: o,
              label: that.$t(o),
              items: map[o]
            }
        );
      });
      return r;
    }
    ,
    openRunWorkflow() {
      if (!this.multipleSelection.length) {
        this.$message.error(this.$t('pls_select_bare_metal') + "!");
        return;
      }
      this.actionDrawer = true;
    }
    ,
    runWorkflow() {
      if (!this.selectedWorkflow.length) {
        this.$message.error(this.$t('pls_select_workflow') + "!");
        return;
      }
      for (let i = 0; i < this.selectedWorkflow.length; i++) {
        let obj = _.find(this.tableData, (o) => o.id == this.selectedWorkflow[i].bareMetalId);
        if (!obj.outBandList.length) {
          this.$message.error(obj.machineModel + '' + obj.machineSn + ' ' + this.$t('obm_not_exists') + "!");
          return;
        }
      }
      let that = this;
      let reqList = _.map(this.copy(this.selectedWorkflow), (wf) => {
        return that.buildRequest(wf);
      });
      if (reqList.length == 0) {
        this.$message.error(this.$t('pls_select_node') + "!");
        return;
      }
      if (_.findIndex(reqList, r => r.settable && !r.params) != -1) {
        this.$message.error(this.$t('pls_set_params') + "!");
        return;
      }
      HttpUtil.post("/workflow/run", reqList, (res) => {
        if (res.success) {
          this.selectedWorkflow = [];
          this.$notify({
            title: this.$t('server_message'),
            message: this.$t('workflow_submitted'),
          });
        }
      });
    }
    ,
    getWorkflowById() {
      return _.find(this.supportedWorkflow, (o) => o.id == this.wfRequest.workflow);
    }
    ,
    getParamsTemplate() {
      HttpUtil.get("/workflow/params/" + this.getWorkflowById().injectableName, {}, (res) => {
        if (res.data[0]) {
          this.workflowParamList = res.data;
        } else {
          this.workflowParamList = [];
        }
      })
    }
    ,
    /**
     * 异步方式会因为有时网络问题加载不出来导致显示出 bug 的问题
     * @param workflowParam
     */
    createWorkflowParamComponent(workflowParam) {
      //动态异步
      if (!this.paramComponent[workflowParam.componentId]) {
        const component = () => ({
          component: import("./../../rackparams/" + workflowParam.workflowName),
          delay: 200,
          timeout: 3000
        })
        let comPointer = Vue.component(workflowParam.componentId, component);
        this.paramComponent[workflowParam.componentId] = comPointer;
      }
    }
    ,
    addToSelectedWorkflow() {
      let that = this;
      if (that.getWorkflowById().injectableName) {
        let originWf = _.find(that.supportedWorkflow, s => s.injectableName == that.getWorkflowById().injectableName);
        for (let k = 0; k < that.multipleSelection.length; k++) {
          if (!that.multipleSelection[k].serverId) {
            that.$message.warning(that.multipleSelection[k].machineModel + ' [' + that.multipleSelection[k].machineSn + ']' + that.$t('not_discoveryed'));
            continue;
          }
          let duplicated = false;
          let componentId = that.getWorkflowById().injectableName + "-" + that.multipleSelection[k].id;
          for (let j = 0; j < that.selectedWorkflow.length; j++) {
            if (that.selectedWorkflow[j].componentId == componentId) {
              duplicated = true;
            }
          }

          if (duplicated) continue;
          if (_.findIndex(originWf.brands, w => w == that.multipleSelection[k].machineBrand) == -1) {
            that.$message.error(originWf.friendlyName + that.$t('not_supported_brand!') + ' ' + that.multipleSelection[k].machineBrand);
            continue;
          }

          let params = {};
          let extraParams = null;
          if (that.workflowParamList.length) {
            let paramTemplate = _.find(that.workflowParamList, function (p) {
              return p.bareMetalId == that.multipleSelection[k].id;
            });
            if (paramTemplate == null) {
              params = _.cloneDeep(originWf.defaultParams);
            } else {
              params = JSON.parse(paramTemplate.paramsTemplate);
              extraParams = JSON.parse(paramTemplate.extraParams);
            }
          } else {
            params = _.cloneDeep(originWf.defaultParams);
          }

          that.selectedWorkflow.push(
              {
                componentId: that.getWorkflowById().injectableName + "-" + that.multipleSelection[k].id,
                bareMetalId: that.multipleSelection[k].id,
                machineModel: that.multipleSelection[k].machineModel,
                machineSn: that.multipleSelection[k].machineSn,
                workflowName: that.getWorkflowById().injectableName,
                workflowId: that.getWorkflowById().id,
                friendlyName: originWf.friendlyName,
                friendlyNameInternational: this.$t(originWf.friendlyName),
                settable: originWf.settable,
                params: params,
                extraParams: extraParams,
              }
          );
        }
      }
    }
    ,
    deleteSelectedWorkflow(index) {
      this.editWorkflowIndex = index;
      this.selectedWorkflow.splice(index, 1);
    }
    ,
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

.action-div {
  border: solid #d7d2d2 1px;
  height: 120px;
  padding: 10px 10px 15px 10px;
  border-radius: 5px;
}

#run-workflow {
  min-width: 220px;
  margin-right: 20px;
}

#workflow-selector {
  min-width: 450px;
  margin-left: 10px;
}

#action-list {
  width: 100%;
  margin: 0 10px;
  /*overflow: auto;*/
  border: solid #d7d2d2 1px;
  min-height: 120px;
  padding: 10px 10px 15px 10px;
  border-radius: 5px;
}

.h25 {
  height: 30px;
}

#control {
  display: flex;
  padding-top: 10px;
  padding-bottom: 20px;
  /*border: solid #d7d2d2 1px;*/
  padding-left: 10px;
  border-left: none;
}

#control-drawer {
  display: flex;
  padding-top: 10px;
  padding-bottom: 20px;
  /*border: solid #d7d2d2 1px;*/
  padding-left: 10px;
  border-left: none;
}


.ml10 {
  margin-left: 10px;
}

.demo-table-expand {
  font-size: 0;
}

.demo-table-expand label {
  width: 90px;
  color: #99a9bf;
}

.demo-table-expand .el-form-item {
  margin-right: 0;
  margin-bottom: 0;
  width: 50%;
}

.detail-pane {
  color: #303133;
}

.detail-info {
  border: 1px solid #EBEEF5;
  border-spacing: 0 !important;
  width: 100%;
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

.el-tabs__nav-wrap {
  padding-left: 10px;
}

.bare-discovery {
  padding: 20px;
}

.selected-number {
  font-size: 17px;
  color: red;
}

.workflow-div {
  border-bottom: yellowgreen 1px solid;
  width: 100%;
}
</style>