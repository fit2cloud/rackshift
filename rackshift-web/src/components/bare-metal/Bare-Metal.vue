<template>
  <el-tabs v-model="activeName" style="width:100vw;min-width: 1300px;">
    <el-tab-pane :label="$t('Bare Metal Server')" name="bare-metal">
      <div class="machine-title2">
        <el-button-group class="batch-button">
          <el-button type="primary" icon="el-icon-delete" @click="delAllSelection">{{ $t('del') }}
          </el-button>
          <el-button type="primary" icon="el-icon-refresh" @click="getData">{{ $t('refresh') }}</el-button>
          <el-button type="primary" icon="el-icon-discover" @click="openDiscover">{{ $t('Discovery') }}</el-button>
          <el-button type="primary" icon="el-icon-caret-right" @click="openRunWorkflow">{{
              $t('Workflow')
            }}
          </el-button>

          <el-dropdown>
            <el-button type="primary">
              {{ $t('opt') }}<i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item @click.native="powerBatch('on')">{{ $t('power_on') }}
              </el-dropdown-item>
              <el-dropdown-item @click.native="powerBatch('off')">{{ $t('power_off') }}
              </el-dropdown-item>
              <el-dropdown-item @click.native="powerBatch('reset')">{{ $t('power_reset') }}
              </el-dropdown-item>
              <el-dropdown-item @click.native="powerBatch('pxe')">{{ $t('power_pxe') }}
              </el-dropdown-item>
              <el-dropdown-item @click.native="fillOBM('obm', 'batch')">{{ $t('OBM') + $t('info') }}
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
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

        <el-table-column prop="machine_model" :label="$t('machine_model')" align="left"
                         sortable="custom" style="overflow: scroll" width="180">
          <template slot-scope="scope">
            <el-tooltip class="item" effect="dark" :content="$t('detail')" placement="right-end">
              <el-link type="primary" @click="showDetail(scope.row)" target="_blank">
                <span class="rs-nowrap">{{ scope.row.machineModel }}</span>
              </el-link>
            </el-tooltip>
          </template>
        </el-table-column>

        <el-table-column prop="machine_sn" :label="$t('machine_sn')" align="left"
                         sortable="custom">
          <template slot-scope="scope">
            {{ scope.row.machineSn }}
          </template>
        </el-table-column>

        <el-table-column prop="management_ip" :label="$t('management_ip')" align="left"
                         sortable="custom">
          <template slot-scope="scope">
            {{ scope.row.managementIp }}
          </template>
        </el-table-column>

        <el-table-column prop="ip_array" :label="$t('IP')" align="left"
                         sortable="custom">
          <template slot-scope="scope">
            {{ scope.row.ipArray }}
          </template>
        </el-table-column>

        <el-table-column :prop="c.prop" :label="$t(c.label)" align="left" v-for="c in columns" sortable="custom"
                         :width="resizeWith(c)">
          <template slot-scope="scope">
            <span v-if="!c.custom">{{ scope.row[c.prop] }}</span>
            <span v-if="c.custom">{{ c.formatter(scope.row[c.prop]) }}</span>
          </template>
        </el-table-column>

        <el-table-column prop="status" :label="$t('machine_status')" align="left">
          <template slot-scope="scope">
            <i class="el-icon-loading" v-if="scope.row.status && scope.row.status.indexOf('ing') != -1"></i>
            <span v-html="statusFilter(scope.row)"></span>
          </template>
        </el-table-column>

        <el-table-column prop="status" :label="$t('power')" align="left"
                         :filters="[{ text: $t('power_on'), value: 'on' }, { text: $t('power_off'), value: 'off' }, { text: $t('unknown'), value: 'unknown' }]"
                         :filter-method="filterPower"
                         filter-placement="bottom-end">
          <template slot-scope="scope">
            <PowerStatus :content="$t('power_text')" :status="scope.row.power"></PowerStatus>
          </template>
        </el-table-column>

        <el-table-column prop="" :label="$t('opt')" align="left">
          <template slot="header" slot-scope="scope">
            <el-input
                v-model="queryVO.searchKey"
                size="mini"
                :placeholder="$t('input_key_search')" v-on:change="getData()"/>
          </template>
          <template slot-scope="scope">
            <el-dropdown>
              <el-button type="primary">
                {{ $t('opt') }}<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item @click.native="power('on', scope.row)">{{ $t('power_on') }}
                </el-dropdown-item>
                <el-dropdown-item @click.native="power('off', scope.row)">{{ $t('power_off') }}
                </el-dropdown-item>
                <el-dropdown-item @click.native="power('reset', scope.row)">{{ $t('power_reset') }}
                </el-dropdown-item>
                <el-dropdown-item @click.native="power('pxe', scope.row)">{{ $t('power_pxe') }}
                </el-dropdown-item>
                <el-dropdown-item @click.native="fillOBM(scope.row)"> {{ $t('OBM') + $t('info') }}
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
          size="50%"
          :title="logTitle"
          :visible.sync="executionLogDrawer"
          direction="ttb"
          min-height="40vh"
          :wrapperClosable="false"
          :before-close="handleCloseExecutionLog">
        <el-card>
          <table class="detail-info">
            <tr>
              <td>{{ $t('time') }}</td>
              <td>{{ $t('user') }}</td>
              <td>{{ $t('operation') }}</td>
              <td>{{ $t('output') }}</td>
            </tr>

            <tr v-for="l in logs">
              <td>{{ l.createTime | dateFormat }}</td>
              <td>{{ l.user }}</td>
              <td>{{ l.operation }}</td>
              <td>{{ l.outPut }}</td>
            </tr>
          </table>
        </el-card>
      </el-drawer>

      <!--obm-->
      <el-dialog :title="$t('obms')" :visible.sync="fillOutObms" width="35vw" :close-on-click-modal="false">
        <el-form :model="form">
          <el-form-item :label="$t('ip')" :label-width="formLabelWidth" v-if="fillOBMMode != 'batch'">
            <el-input v-model="curObm.ip" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item :label="$t('username')" :label-width="formLabelWidth">
            <el-input v-model="curObm.userName" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item :label="$t('pwd')" :label-width="formLabelWidth">
            <el-input v-model="curObm.pwd" autocomplete="off" show-password></el-input>
          </el-form-item>
        </el-form>
        <template v-slot:footer>
          <div class="dialog-footer">
            <el-button @click="fillOutObms = false">{{ $t('cancel') }}</el-button>
            <el-button type="primary" @click="submitOBM" :loading="obmLoading">{{ $t('confirm') }}</el-button>
          </div>
        </template>
      </el-dialog>

      <!--参数配置-->
      <el-dialog :title="currentParamConfig" :visible.sync="fillWfParams" ref="paramDialog" width="80vw"
                 :close-on-click-modal="false">
        <keep-alive>
          <component v-if="editWorkflowIndex != -1 && selectedWorkflow.length > 0"
                     :is="currentWfParamTemplate"
                     :params="selectedWorkflow[editWorkflowIndex] && selectedWorkflow[editWorkflowIndex].params"
                     :extraParams="selectedWorkflow[editWorkflowIndex] && selectedWorkflow[editWorkflowIndex].extraParams"
                     :bareMetalId="selectedWorkflow[editWorkflowIndex] && selectedWorkflow[editWorkflowIndex].bareMetalId"
                     ref="currentWfParamTemplate"></component>
        </keep-alive>
        <template v-slot:footer>
          <el-button @click="restoreParams">
            <i class="el-icon-refresh"></i>
            {{ $t('reset') }}
          </el-button>
          <el-button type="primary" @click="saveParams" :loading="fillWfParamsLoading">{{ $t('confirm') }}</el-button>
        </template>
      </el-dialog>

      <!--批量设置参数 start-->

      <el-dialog :title="$t('batch_params')" :visible.sync="batchParams" width="55vw" :close-on-click-modal="false"
                 :append-to-body="true">
        <el-form :model="form">
          <el-collapse v-model="activeNames" @change="handleChange" accordion>
            <el-collapse-item :title="$t('common_params')" name="commonOs">

              <el-form-item :label="$t('hostname')" prop="hostname" :label-width="formLabelWidth">
                <el-input v-model="bp.hostname" autocomplete="off" aria-required="true"></el-input>
              </el-form-item>

              <el-form-item :label="$t('host_increase')" prop="increase" :label-width="formLabelWidth">
                <el-col :span="12">
                  <el-tooltip :content="$t('increase_host_desc')" effect="dark">
                    <el-switch v-model="bp.increase"></el-switch>
                  </el-tooltip>
                </el-col>
              </el-form-item>

              <el-form-item :label="$t('base_number')" :label-width="formLabelWidth" v-if="bp.increase">
                <el-input v-model="bp.baseNumber" type="number"></el-input>
              </el-form-item>

              <el-form-item :label="$t('root_pwd')" prop="rootPassword" :label-width="formLabelWidth">
                <el-input v-model="bp.rootPassword" autocomplete="off"
                          show-password></el-input>
              </el-form-item>

              <el-form-item :label="$t('start_ip')" prop="startIp" :label-width="formLabelWidth">
                <el-input v-model="bp.startIp" autocomplete="off"></el-input>
              </el-form-item>

              <el-form-item :label="$t('end_ip')" prop="endIp" :label-width="formLabelWidth">
                <el-input v-model="bp.endIp" autocomplete="off"></el-input>
              </el-form-item>

              <el-form-item :label="$t('gateway')" prop="gateway" :label-width="formLabelWidth">
                <el-input v-model="bp.gateway" autocomplete="off"></el-input>
              </el-form-item>

              <el-form-item :label="$t('netmask')" prop="netmask" :label-width="formLabelWidth">
                <el-input v-model="bp.netmask" autocomplete="off"></el-input>
              </el-form-item>

              <el-form-item :label="$t('nic')" :label-width="formLabelWidth">
                <el-select v-model="bp.nicNumber" class="input-element">
                  <el-option v-for="n in allNicNumber" :label="n.name" :value="n.value">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-collapse-item>

            <el-collapse-item :title="$t('windwos_param')" name="windowsSamba">

              <el-form-item :label="$t('domain')" prop="domain" :label-width="formLabelWidth">
                <el-input v-model="bp.domain" autocomplete="off" aria-required="true"></el-input>
              </el-form-item>
              <el-form-item :label="$t('username')" prop="username" :label-width="formLabelWidth">
                <el-input v-model="bp.username" autocomplete="off" aria-required="true"></el-input>
              </el-form-item>

              <el-form-item :label="$t('samba')" prop="smbRepo" :label-width="formLabelWidth">
                <el-input v-model="bp.smbRepo"
                          placeholder="\\172.31.128.1\windowsServer2012"></el-input>
              </el-form-item>

              <el-form-item :label="$t('productkey')" prop="productkey" :label-width="formLabelWidth">
                <el-input v-model="bp.productkey"
                          placeholder="XXXX-XXXX-XXXX-XXXX-XXXX"></el-input>
              </el-form-item>

              <el-form-item :label="$t('smb_user')" prop="smbUser" :label-width="formLabelWidth">
                <el-input v-model="bp.smbUser"></el-input>
              </el-form-item>

              <el-form-item :label="$t('smb_password')" prop="smbPassword" :label-width="formLabelWidth">
                <el-input v-model="bp.smbPassword" show-password></el-input>
              </el-form-item>
            </el-collapse-item>
          </el-collapse>

        </el-form>

        <template v-slot:footer>
          <div class=" dialog-footer
              ">
            <el-button @click="batchParams = false">{{ $t('cancel') }}</el-button>
            <el-button type="primary" @click="confirmBatchParams" :loading="batchParamsLoading">{{
                $t('confirm')
              }}
            </el-button>
          </div>
        </template>
      </el-dialog>

      <!--批量设置参数 end-->

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

      <!--详情页-->
      <drawer
          :visible.sync="detailDrawer"
          direction="rtl"
          :with-header="false"
          size="50%"
          :before-close="handleClose">

        <div class="demo-drawer__content">
          <el-tabs v-model="detailShowName">
            <el-tab-pane :label="$t('detail')" name="detail">
              <el-form ref="form" :model="machine" label-position="top">
                <el-form-item :label="$t('machine_model')">
                  {{ machine.machineModel }}
                </el-form-item>
                <el-form-item :label="$t('machine_sn')">
                  {{ machine.machineSn }}
                </el-form-item>
                <el-form-item :label="$t('management_ip')">
                  {{ machine.managementIp }}
                </el-form-item>
                <el-form-item :label="$t('bmc_mac')">
                  {{ machine.bmcMac }}
                </el-form-item>
                <el-form-item :label="$t('cpu')">
                  <span v-if="machine.cpu">{{ machine.cpuType }} X {{ machine.cpu }}</span>
                  <span v-if="!machine.cpu">{{ $t('not_exists') }}</span>
                </el-form-item>
                <el-form-item :label="$t('memory')">
                  <span v-if="machine.memory">{{ machine.memory }}{{ ' ' + $t('GB') }}</span>
                  <span v-if="!machine.cpu">{{ $t('not_exists') }}</span>
                </el-form-item>
                <el-form-item :label="$t('disk')">
                  <span v-if="machine.disk">{{ machine.disk }}{{ ' ' + $t('GB') }}</span>
                  <span v-if="!machine.disk">{{ $t('not_exists') }}</span>
                </el-form-item>
              </el-form>
            </el-tab-pane>
            <el-tab-pane :label="$t('cpu_detail')" name="cpuDetail" class="detail-pane">
              <table class="detail-info">
                <tr>
                  <td>{{ $t('proc_name') }}</td>
                  <td>{{ $t('proc_socket') }}</td>
                  <td>{{ $t('proc_speed') }}</td>
                  <td>{{ $t('proc_num_cores') }}</td>
                  <td>{{ $t('proc_num_threads') }}</td>
                  <td>{{ $t('sync_time') }}</td>
                </tr>
                <tr v-for="c in cpus">
                  <td>{{ c.procName }}</td>
                  <td>{{ c.procSocket }}</td>
                  <td>{{ c.procSpeed }}</td>
                  <td>{{ c.procNumCores }}</td>
                  <td>{{ c.procNumThreads }}</td>
                  <td>{{ c.syncTime | dateFormat }}</td>
                </tr>
              </table>
            </el-tab-pane>
            <el-tab-pane :label="$t('memory_detail')" name="memoryDetail" class="detail-pane">
              <table class="detail-info">
                <tr>
                  <td>{{ $t('mem_cpu_num') }}</td>
                  <td>{{ $t('mem_mod_num') }}</td>
                  <td>{{ $t('mem_mod_size') }}</td>
                  <td>{{ $t('mem_mod_type') }}</td>
                  <td>{{ $t('mem_mod_num') }}</td>
                  <td>{{ $t('mem_mod_frequency') }}</td>
                  <td>{{ $t('mem_mod_part_num') }}</td>
                  <td>{{ $t('mem_mod_min_volt') }}</td>
                  <td>{{ $t('sync_time') }}</td>
                </tr>
                <tr v-for="c in memories">
                  <td>{{ c.memCpuNum }}</td>
                  <td>{{ c.memModNum }}</td>
                  <td>{{ c.memModSize + ' GB' }}</td>
                  <td>{{ c.memModType }}</td>
                  <td>{{ c.memModNum }}</td>
                  <td>{{ c.memModFrequency + ' MHz' }}</td>
                  <td>{{ c.memModPartNum }}</td>
                  <td>{{ c.memModMinVolt }}</td>
                  <td>{{ c.syncTime | dateFormat }}</td>
                </tr>
              </table>
            </el-tab-pane>
            <el-tab-pane :label="$t('disk_detail')" name="diskDetail" class="detail-pane">
              <table class="detail-info">
                <tr>
                  <td>{{ $t('enclosure_id') }}</td>
                  <td>{{ $t('controller_id') }}</td>
                  <td>{{ $t('drive') }}</td>
                  <td>{{ $t('type') }}</td>
                  <td>{{ $t('size') }}</td>
                  <td>{{ $t('raid') }}</td>
                  <td>{{ $t('manufactor') }}</td>
                  <td>{{ $t('sn') }}</td>
                  <td>{{ $t('sync_time') }}</td>
                </tr>
                <tr v-for="c in disks">
                  <td>{{ c.enclosureId }}</td>
                  <td>{{ c.controllerId }}</td>
                  <td>{{ c.drive }}</td>
                  <td>{{ c.type }}</td>
                  <td>{{ c.size }}</td>
                  <td>{{ c.raid }}</td>
                  <td>{{ c.manufactor }}</td>
                  <td>{{ c.sn }}</td>
                  <td>{{ c.syncTime | dateFormat }}</td>
                </tr>
              </table>
            </el-tab-pane>

            <el-tab-pane :label="$t('nic_detail')" name="nicDetail" class="detail-pane">
              <table class="detail-info">
                <tr>
                  <td>{{ $t('nic_number') }}</td>
                  <td>{{ $t('mac') }}</td>
                  <td>{{ $t('sync_time') }}</td>
                </tr>
                <tr v-for="c in nics">
                  <td>{{ c.number }}</td>
                  <td>{{ c.mac }}</td>
                  <td>{{ c.syncTime | dateFormat }}</td>
                </tr>
              </table>
            </el-tab-pane>
          </el-tabs>
        </div>
      </drawer>
    </el-tab-pane>

    <!-- 执行器 start -->
    <drawer
        :visible.sync="actionDrawer"
        direction="btt"
        :with-header="false"
        size="70%"
        :before-close="handleClose">
      <el-divider>{{ $t('Executor') }}</el-divider>
      <el-divider>{{ $t('selected') + ' ' }} <em class="selected-number">
        {{ multipleSelection.length + ' ' }}</em>
        {{ $t('tai') + $t('Bare Metal Server') }}
      </el-divider>
      <div id="control-drawer">

        <div id="workflow-selector" class="action-div">
          <div id="select-workflow">
            <div class="el-icon-menu h25 workflow-div workflow-div">
              {{ $t('Workflow') }}
            </div>
            <div class="run-splitter h25"></div>
            <el-select v-model="wfRequest.workflow" filterable :placeholder="$t('please_select')"
                       @change="getParamsTemplate">
              <el-option-group
                  v-for="group in groupedSupportedWorkflow"
                  :key="group.key"
                  :label="$t(group.label)">
                <el-option
                    v-for="item in group.items"
                    :label="$t(item.friendlyName)"
                    :value="item.id">
                </el-option>
              </el-option-group>

            </el-select>

            <el-button :disabled="this.multipleSelection.length == 0 || !wfRequest.workflow"
                       @click="addToSelectedWorkflow"
                       class="h50 ml10"><span
                class="el-icon-circle-plus"></span>{{ $t('add_to_selected_wf_list') }}
            </el-button>
          </div>
        </div>

        <div id="action-list">
          <div class="el-icon-s-operation h25 workflow-div">
            <el-badge :value="selectedWorkflow.length" class="item" type="primary" v-show="selectedWorkflow.length">
              {{ $t('selected_workflows') }}
            </el-badge>
            <span v-show="selectedWorkflow.length == 0">
              {{ $t('selected_workflows') }}
              </span>

          </div>
          <div style="display: block;">
            <el-card v-for="(w, $index) in selectedWorkflow" style="height:100%;">
              <el-row>
                <el-col :span="6">
                  <el-button @click="deleteSelectedWorkflow($index)" class="h50 ml10"><span
                      class="el-icon-remove"></span>{{ $t('del') }}
                  </el-button>
                </el-col>

                <el-col :span="10">
                  {{ w.machineModel + ' ' + w.machineSn }}
                  <br>
                  {{ w.friendlyNameInternational }}
                </el-col>

                <el-col :span="8">
                  <el-button @click="editWfParams($index)" v-if="w.settable">
                    {{ $t('set_workflow_param') }}
                  </el-button>
                  <span v-if="!w.settable">
                    {{ $t('no_nessary_to_set') }}
                   </span>
                </el-col>
              </el-row>
            </el-card>
          </div>
        </div>

        <div id="run-workflow" class="action-div">
          <div class="el-icon-caret-right h25 workflow-div">{{ $t('Run') }}
          </div>
          <div class="run-splitter h25"></div>
          <div class="center flex">
            <el-button-group>
              <el-tooltip effect="dark" :content="$t('batch_params')">
                <el-button class="el-icon-setting h50" @click="openBatchParams"></el-button>
              </el-tooltip>
              <el-tooltip effect="dark" :content="$t('Run')">
                <el-button class="el-icon-caret-right h50" @click="runWorkflow"></el-button>
              </el-tooltip>
            </el-button-group>
          </div>
        </div>
      </div>

    </drawer>
    <!-- 执行器 end -->

  </el-tabs>
</template>

<script>
import HttpUtil from "../../common/utils/HttpUtil";
import {isAnyBlank} from "../../common/utils/CommonUtil";
import OBM from "../obm/Obm"
import Discovery from "../discovery/Discovery"
import Vue from "vue"
import i18n from "@/i18n/i18n";
import {WebSocketUtil} from "@/common/utils/WebSocket";
import PowerStatus from '../../common/powerstatus/Power-Status'
import axios from 'axios'
import {getIPRange} from 'get-ip-range'
import bus from '../../common/bus/bus'
import paramMap from '../../rackparams/params'

Vue.filter('statusFilter', function (row) {
  return i18n.t('PXE') + ' ' + i18n.t(row.status);
});
let _ = require('lodash');
export default {
  data() {
    return {
      fillOBMMode: 'single',
      activeNames: 'commonOs',
      batchParamsLoading: false,
      allNicNumber: [],
      batchParams: false,
      bp: {},
      accurate: false,
      groupedSupportedWorkflow: [],
      discoveryVisible: false,
      fillWfParams: false,
      executionLogDrawer: false,
      activeName: 'bare-metal',
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
          label: 'CPU',
          prop: "cpu"
        },
        {
          label: 'memory',
          prop: "memory",
          expandLanguage: "en_US",
          custom: true,
          formatter: function (item) {
            if (item) {
              return parseInt(item) + ' GB'
            }
            return item;
          }
        },
        {
          label: 'disk',
          prop: "disk",
          custom: true,
          formatter: function (item) {
            if (item) {
              return parseInt(item) / 1000 + ' TB'
            }
            return item;
          }
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
      detailShowName: 'detail',
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
      websocket: null
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
  },
  mounted() {
    if (!this.websocket) {
      this.websocket = new WebSocketUtil();
      this.websocket.openSocket('lifecycle', this.notify);
      this.getData();
    }
    this.getAllGraphDefinitions();
    bus.$on('refresh_workflow', this.refreshWorkflow);
  }
  ,
  methods: {
    refreshWorkflow() {
      if (this.selectedWorkflow.length) {
        let that = this;
        _.map(this.selectedWorkflow, w => {
          w.friendlyNameInternational = that.$t(w.friendlyName)
        })
      }
    },
    handleChange(val) {

    },
    setParam(test) {
      switch (test) {
        case "ip":
          let rangeIp = getIPRange(this.bp.startIp, this.bp.endIp);
          let j = 0;
          for (let i = 0; i < this.selectedWorkflow.length; i++) {
            if (this.selectedWorkflow[i].params && this.selectedWorkflow[i].params.options.defaults && this.selectedWorkflow[i].params.options.defaults.networkDevices) {
              if (rangeIp[j++])
                this.$set(this.selectedWorkflow[i].params.options.defaults.networkDevices[0].ipv4, 'ipAddr', rangeIp[j]);
            }
          }
          break;
        case "gateway":
          this.setNetworkParam('gateway', this.bp.gateway);
          break;
        case "netmask":
          this.setNetworkParam('netmask', this.bp.netmask);
          break;
        case "hostname":
          for (let i = 0; i < this.selectedWorkflow.length; i++) {
            if (this.selectedWorkflow[i].params && this.selectedWorkflow[i].params.options.defaults && this.selectedWorkflow[i].params.options.defaults) {
              if (this.bp.baseNumber) {
                this.$set(this.selectedWorkflow[i].params.options.defaults, 'hostname', this.bp.hostname + (parseInt(this.bp.baseNumber) + i));
              } else {
                this.$set(this.selectedWorkflow[i].params.options.defaults, 'hostname', this.bp.hostname + (i + 1));
              }
            }
          }
          break;
        default:
          break;
      }
    },
    setNetworkParam(prop, value) {
      for (let i = 0; i < this.selectedWorkflow.length; i++) {
        if (this.selectedWorkflow[i].params && this.selectedWorkflow[i].params.options.defaults && this.selectedWorkflow[i].params.options.defaults.networkDevices) {
          if (this.bp[prop])
            this.$set(this.selectedWorkflow[i].params.options.defaults.networkDevices[0].ipv4, prop, value);
        }
      }
    },
    setCommonParam(prop, value) {
      for (let i = 0; i < this.selectedWorkflow.length; i++) {
        if (this.selectedWorkflow[i].params && this.selectedWorkflow[i].params.options.defaults) {
          if (this.bp[prop])
            this.$set(this.selectedWorkflow[i].params.options.defaults, prop, value);
        }
      }
    },
    setNicNumber() {
      if (!this.bp.nicNumber) {
        return;
      }
      let that = this;
      for (let i = 0; i < this.selectedWorkflow.length; i++) {
        if (that.selectedWorkflow[i].params && that.selectedWorkflow[i].params.options.defaults && that.selectedWorkflow[i].params.options.defaults.networkDevices) {
          that.getHardWarePromise(that.selectedWorkflow[i].bareMetalId).then(d => {
            if (d.data.data.nics.length) {
              if (_.find(d.data.data.nics, n => n.number == that.bp.nicNumber))
                that.$set(that.selectedWorkflow[i].params.options.defaults.networkDevices[0], 'device', _.find(d.data.data.nics, n => n.number == that.bp.nicNumber).mac);
            }
          });
        }
      }
    },
    confirmBatchParams() {
      this.batchParamsLoading = true;
      if (this.bp.startIp && this.bp.endIp) {
        this.setParam('ip');
      }
      if (this.bp.gateway) {
        this.setParam('gateway', this.bp.gateway);
      }
      if (this.bp.netmask) {
        this.setParam('netmask', this.bp.netmask);
      }
      if (this.bp.nicNumber) {
        this.setNicNumber();
      }
      if (this.bp.hostname) {
        if (this.bp.increase) {
          this.setParam('hostname', this.bp.hostname);
        } else {
          this.setCommonParam('hostname', this.bp.hostname);
        }
      }
      if (this.bp.rootPassword) {
        this.setCommonParam('rootPassword', this.bp.rootPassword);
      }
      if (this.bp.domain) {
        this.setCommonParam('domain', this.bp.domain);
      }
      if (this.bp.username) {
        this.setCommonParam('username', this.bp.username);
      }
      if (this.bp.smbUser) {
        this.setCommonParam('smbUser', this.bp.smbUser);
      }
      if (this.bp.smbPassword) {
        this.setCommonParam('smbPassword', this.bp.smbPassword);
      }
      if (this.bp.smbRepo) {
        this.setCommonParam('smbRepo', this.bp.smbRepo);
      }
      this.batchParamsLoading = false;
      this.batchParams = false;
      localStorage.setItem("batchParams", JSON.stringify(this.bp));
    },
    getHardWarePromise(bareMetalId) {
      return axios.get("/bare-metal/hardwares/" + bareMetalId);
    },
    /**
     * 从所有选中的物理机中找到拥有最少网卡的数量 因为要批量选择某一块网卡
     */
    getMinNicNumber() {
      if (this.selectedWorkflow.length) {
        let bareMetalIds = _.map(this.selectedWorkflow, (w) => w.bareMetalId);
        let conReq = [];
        let that = this;
        bareMetalIds.forEach(id => {
          conReq.push(that.getHardWarePromise(id))
        })
        let minNicNumber = 0;
        Promise.all(conReq).then(function (data) {
          if (data.length > 0) {
            minNicNumber = data[0].data.data.nics.length;
            data.forEach(d => {
              if (d.data.data.nics.length && d.data.data.nics.length < minNicNumber) {
                minNicNumber = d.data.nics.length;
              }
            })

            let arr = [];
            for (let i = 0; i < minNicNumber; i++) {
              arr.push({
                name: "eth" + i,
                value: "eth" + i
              });
            }

            that.allNicNumber = arr;
          }
        });
      }
    },
    openBatchParams() {
      this.batchParams = true;
      if (localStorage.getItem("batchParams")) {
        this.bp = JSON.parse(localStorage.getItem("batchParams"));
      }
      this.getMinNicNumber();
    },
    filterPower(tag, row) {
      return row.power == tag;
    },
    notify(msg) {
      this.getData();
      if (msg != "") {
        this.$notify({
          title: this.$t('server_message'),
          message: msg,
        });
      }
    },
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
    statusFilter(row) {
      if (row.status.indexOf("ing") == -1) {
        if (row.serverId)
          return '<span style="display: inline-block;white-space: nowrap;">' +
              this.$t('PXE') + ' ' + this.$t(row.status) + '<i class="el-icon-check" style="color:#55BA23;margin-left:5px;"></i><br>'
              + this.$t('OBM') + ' ' + this.$t('info') + (row.outBandList.length > 0 ? '<i class="el-icon-check" style="color:#55BA23;margin-left:5px;"></i>' : '<i class="el-icon-close" style="margin-left:5px;color: red;"></i>') + '</span>';
        else
          return '<span style="display: inline-block;white-space: nowrap;">' +
              this.$t('PXE') + ' ' + this.$t('status') + '<i class="el-icon-close" style="margin-left:5px;color: red;"></i><br>'
              + this.$t('OBM') + ' ' + this.$t('info') + (row.outBandList.length > 0 ? '<i class="el-icon-check" style="color:#55BA23;margin-left:5px;"></i>' : '<i class="el-icon-close" style="margin-left:5px;color: red;"></i>') + '</span>';
      } else {
        return '<span style="display: inline-block;white-space: nowrap;">' +
            this.$t('PXE') + ' ' + this.$t(row.status) + '<br>';
      }
    },
    resizeWith(c) {
      return (c.expandLanguage && c.expandLanguage == localStorage.getItem('lang')) ? '100px' : '90px';
    },
    restoreParams() {
      this.$refs.currentWfParamTemplate.restoreParams();
    },
    expandChange(a) {
      this.executionLogDrawer = true;
      this.currentMachine = a;
      this.logTitle = this.$t('execution_log') + ' ' + this.currentMachine.machineModel + ' ' + this.currentMachine.machineSn + ' ' + this.currentMachine.managementIp;
      this.logPoller = window.setInterval(this.loadLogs, 1000);
    },
    loadLogs() {
      HttpUtil.post("/execution-log/detaillist/" + 1 + "/" + 1000, {bareMetalId: this.currentMachine.id}, (res) => {
        this.$set(this, 'logs', res.data.listObject);
      });
    },
    buildRequest(workflow) {
      let request = JSON.parse(JSON.stringify(workflow));
      delete request.componentId;
      delete request.machineModel;
      delete request.machineSn;
      delete request.settable;
      delete request.brands;
      delete request.friendlyName;
      delete request.friendlyNameInternational;
      return request;
    },
    saveParams() {
      if (this.$refs.currentWfParamTemplate.valid()) {
        this.selectedWorkflow[this.editWorkflowIndex].params = this.$refs.currentWfParamTemplate.payLoad;
        this.selectedWorkflow[this.editWorkflowIndex].extraParams = this.$refs.currentWfParamTemplate.extraParams;
        this.fillWfParamsLoading = true;
        HttpUtil.post("/workflow/params", this.buildRequest(this.selectedWorkflow[this.editWorkflowIndex]), (res) => {
          this.fillWfParamsLoading = false;
          this.fillWfParams = false;
        });
      }
    },
    copy(obj) {
      return JSON.parse(JSON.stringify(obj));
    },
    editWfParams(index) {
      this.editWorkflowIndex = index;
      let comId = this.selectedWorkflow[index].workflowName + this.selectedWorkflow[index].machineSn;
      if (paramMap[this.selectedWorkflow[index].workflowName]) {
        if (!this.selectedWorkflowComponent[comId]) {
          this.currentWfParamTemplate = _.cloneDeep(paramMap[this.selectedWorkflow[index].workflowName]);
          this.selectedWorkflowComponent[comId] = this.currentWfParamTemplate;
        }
        this.currentParamConfig = this.selectedWorkflow[index].machineModel + ' ' + this.$t(this.selectedWorkflow[index].friendlyName) + " " + this.$t('param_config');
        this.fillWfParams = true;
      }
    },
    power(opt, row) {
      this.$confirm(this.$t('confirm') + this.$t('power_' + opt) + '?', this.$t('tips'), {
        type: "warning"
      }).then(() => {
        let that = this;
        that.loadingList = true;
        HttpUtil.get("/bare-metal/power/" + row.id + "/" + opt, null, (res) => {
          if (res.success) {
            this.$message.success(this.$t('success'));
            this.getData();
          } else {
            this.$message.error(this.$t('error'));
          }
          that.loadingList = false;
        }, (msg) => {
          that.$alert(msg);
          that.loadingList = false;
        });
      });
    },
    powerBatch(opt,) {
      if (opt == 'obm') {

      } else {
        this.$confirm(this.$t('confirm') + this.$t('power_' + opt) + '?', this.$t('tips'), {
          type: "warning"
        }).then(() => {
          let that = this;
          let ids = that.getSelectedIds();
          if (!ids.length) {
            this.$message.error(this.$t('pls_select_') + this.$t('Bare Metal Server') + "!");
            return;
          }
          that.loadingList = true;
          HttpUtil.post("/bare-metal/power/" + opt, ids, (res) => {
            if (res.success) {
              this.$message.success(this.$t('success'));
            } else {
              this.$message.error(res.message);
            }
            that.loadingList = false;
          }, (msg) => {
            that.$alert(msg);
            that.loadingList = false;
          });
        });
      }
    },
    getHardware() {
      HttpUtil.get("/bare-metal/hardwares/" + this.machine.id, null, (res) => {
        this.cpus = res.data.cpus;
        this.memories = res.data.memories;
        this.disks = res.data.disks;
        this.nics = res.data.nics;
      })
    },
    showDetail(machine) {
      this.machine = machine;
      this.detailDrawer = true;
      this.getHardware();
    },
    fillOBM(val, mode) {
      if (mode != 'batch') {
        if (val.outBandList.length > 0) {
          this.curObm = {
            ip: val.outBandList[0].ip,
            userName: val.outBandList[0].userName,
            pwd: val.outBandList[0].pwd,
            bareMetalId: val.id,
          };
        } else {
          this.curObm = {
            ip: val.managementIp,
            userName: null,
            pwd: null,
            bareMetalId: val.id,
          };
        }
      } else {
        if (this.getSelectedIds().length == 0) {
          this.$message.error(this.$t('pls_select_') + this.$t('Bare Metal Server') + "!");
          return;
        }
        this.curObm = {
          userName: null,
          pwd: null,
        };
      }
      this.fillOBMMode = mode;
      this.fillOutObms = true;
    },
    submitOBM() {
      this.obmLoading = true;
      if (this.fillOBMMode == 'single') {
        if (isAnyBlank(this.curObm.ip)) {
          this.$message.error(this.$t('pls_fill_in_blanks'));
          this.obmLoading = false;
          return;
        }
      }
      if (isAnyBlank(this.curObm.userName, this.curObm.pwd)) {
        this.$message.error(this.$t('pls_fill_in_blanks'));
        this.obmLoading = false;
        return;
      }
      if (this.fillOBMMode != 'batch') {
        HttpUtil.post("/outband/save?bareMetalId=" + this.curObm.bareMetalId, this.curObm, (res) => {
          if (res.success) {
            this.obmLoading = false;
            this.curObm = {};
            this.fillOutObms = false;
            this.$message.success(this.$t('opt_success'));
            this.getData();
          } else {
            this.$alert.error(this.$t('opt_fail_pxe'));
          }
        }, (res) => {
          this.obmLoading = false;
        });
      } else {
        let req = {
          obm: this.curObm,
          ids: this.getSelectedIds()
        };
        HttpUtil.post("/outband/saveBatch", req, (res) => {
          if (res.success) {
            this.obmLoading = false;
            this.curObm = {};
            this.fillOutObms = false;
            this.$message.success(this.$t('opt_success'));
            this.getData();
          } else {
            this.$alert.error(this.$t('opt_fail_pxe'));
          }

          this.fillOBMMode = 'single';
        }, (res) => {
          this.obmLoading = false;
          this.fillOBMMode = 'single';
        });
      }
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
      HttpUtil.post("/bare-metal/list/" + this.query.pageIndex + "/" + this.query.pageSize, this.queryVO, (res) => {
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
    confirmEdit() {
      this.loading = true;
      if (this.editType == 'edit') {
        HttpUtil.post("/bare-metal/update", this.editObj, (res) => {
          this.detailDrawer = false;
          this.$message.success(this.$t('edit_success'));
          this.cancelForm();
          this.getData();
        })
      } else {
        HttpUtil.post("/bare-metal/add", this.editObj, (res) => {
          this.detailDrawer = false;
          this.$message.success(this.$t('add_success'));
          this.cancelForm();
          this.getData();
        })
      }
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
        HttpUtil.post("/bare-metal/del", ids, (res) => {
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
          HttpUtil.get("/bare-metal/del/" + row.id, {}, (res) => {
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
          let extraParams = {};
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
  border-spacing: 0px !important;
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

.el-tabs__nav-wrap .is-top {
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