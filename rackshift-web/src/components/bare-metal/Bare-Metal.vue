<template>
    <el-tabs style="width:80vw" v-model="activeName">
        <el-tab-pane :label="$t('bare_metal')" name="bare-metal">
            <div>
                <div class="machine-title">
                    <i class="el-icon-user-solid">{{$t('Machines')}}</i>

                    <el-button-group class="batch-button">
                        <el-button type="primary" icon="el-icon-circle-plus-outline">{{$t('add')}}</el-button>
                        <el-button type="primary" icon="el-icon-delete-solid" @click="delAllSelection">{{$t('del')}}
                        </el-button>
                        <el-button type="primary" icon="el-icon-refresh" @click="getData">{{$t('refresh')}}</el-button>
                    </el-button-group>
                </div>
                <div id="control" style="display: flex;">
                    <div id="run-workflow">
                        <div class="el-icon-caret-right h25"
                             style="border-bottom: yellowgreen 1px solid;    width: 100%;">{{$t('Run')}}
                        </div>
                        <div class="run-splitter h25"></div>
                        <div>
                            <el-button class="el-icon-caret-right h50" @click="runWorkflow"></el-button>
                            <el-button class="el-icon-close h50"></el-button>
                        </div>
                    </div>

                    <div id="workflow-selector" style="display: flex;">
                        <div id="select-workflow">
                            <div class="el-icon-menu h25" style="border-bottom: yellowgreen 1px solid;    width: 100%;">
                                Workflow
                            </div>
                            <div class="run-splitter h25"></div>
                            <!--                    <el-button class="h50">Workflow <span class="el-icon-caret-bottom"></span></el-button>-->
                            <el-select v-model="wfRequest.workflow" @change="getParamsTemplate">
                                <el-option
                                        v-for="g in filterList"
                                        :label="g.injectableName"
                                        :value="g.injectableName"></el-option>
                            </el-select>

                            <el-button :disabled="this.multipleSelection.length == 0 || !wfRequest.workflow"
                                       @click="addToSelectedWorkflow"
                                       class="h50 ml10"><span
                                    class="el-icon-circle-plus"></span>{{$t('add_to_selected_wf_list')}}
                            </el-button>
                        </div>
                    </div>

                    <div id="action-list">
                        <div class="el-icon-s-operation h25"
                             style="border-bottom: yellowgreen 1px solid;    width: 100%;">
                            {{$t('selected_workflows')}}
                        </div>
                        <div>
                            <el-card v-for="(w, $index) in selectedWorkflow">
                                <div style="display: flex">
                                    <el-button @click="deleteSelectedWorkflow($index)" class="h50 ml10"><span
                                            class="el-icon-remove"></span>{{$t('del')}}
                                    </el-button>
                                    <span style="display: block;padding: 12px 20px;">
                                        {{w.machineModel + ' ' + w.machineSn}}
                                    </span>
                                    <span style="display: block;padding: 12px 20px;">
                                        {{w.workflowName}}
                                    </span>
                                    <el-button @click="editWfParams($index)">
                                        {{$t('set_workflow_param')}}
                                    </el-button>
                                </div>
                            </el-card>
                        </div>
                    </div>
                </div>

                <el-table
                        :data="tableData"
                        class="table"
                        ref="multipleTable"
                        header-cell-class-name="table-header"
                        @sort-change="sortChange($event)"
                        style="width: 100%"
                        @expand-change="expandChange"
                        @selection-change="handleSelectionChange"
                >
                    <el-table-column type="selection" align="center"></el-table-column>

                    <el-table-column prop="machineModel" :label="$t('machine_model')" align="center"
                                     sortable="custom">
                        <template slot-scope="scope">
                            <el-tooltip class="item" effect="dark" :content="$t('detail')" placement="right-end">
                                <el-link type="primary" @click="showDetail(scope.row)" target="_blank">
                                    {{scope.row.machineModel}}
                                </el-link>
                            </el-tooltip>
                        </template>
                    </el-table-column>

                    <el-table-column :prop="c.prop" :label="c.label" align="center"
                                     v-for="c in columns" sortable="custom"></el-table-column>

                    <el-table-column prop="status" :label="$t('machine_status')" align="center"
                                     sortable="custom">
                        <template slot-scope="scope">
                            <i class="el-icon-loading" v-if="scope.row.status.indexOf('ing') != -1"></i>
                            <span style="margin-left: 10px">{{ scope.row.status }}</span>
                        </template>
                    </el-table-column>

                    <el-table-column :label="$t('execution_log')" align="center" min-width="100px" type="expand">
                        <template slot-scope="scope">
                            <span style="display:block" v-for="l in scope.row.logs">{{l.outPut}}</span>
                        </template>
                        <!--                        <template slot-scope="scope" v-if="!scope.row.logs || scope.row.logs.length == 0">-->
                        <!--                            <i class="el-icon-loading"></i>-->
                        <!--                        </template>-->

                    </el-table-column>

                    <el-table-column prop="createTime" :label="$t('create_time')" align="center"
                                     sortable="custom">
                        <template slot-scope="scope">
                            {{ scope.row.createTime | dateFormat }}
                        </template>

                    </el-table-column>

                    <el-table-column prop="" :label="$t('opt')" align="center">
                        <template slot="header" slot-scope="scope">
                            <el-input
                                    v-model="search"
                                    size="mini"
                                    :placeholder="$t('input_key_search')" v-on:change="getData"/>
                        </template>
                        <template slot-scope="scope">
                            <el-dropdown>
                                <el-button type="primary">
                                    {{$t('opt')}}<i class="el-icon-arrow-down el-icon--right"></i>
                                </el-button>
                                <el-dropdown-menu slot="dropdown">
                                    <el-dropdown-item @click.native="power('on', scope.row)">{{$t('poweron')}}
                                    </el-dropdown-item>
                                    <el-dropdown-item @click.native="power('off', scope.row)">{{$t('poweroff')}}
                                    </el-dropdown-item>
                                    <el-dropdown-item @click.native="power('reset', scope.row)">{{$t('powercycle')}}
                                    </el-dropdown-item>
                                    <el-dropdown-item @click.native="power('pxe', scope.row)">{{$t('pxeboot')}}
                                    </el-dropdown-item>
                                    <el-dropdown-item @click.native="fillOBM(scope.row)">OBM信息
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

                <!--obm-->
                <el-dialog :title="$t('obms')" :visible.sync="fillOutObms">
                    <el-form :model="form">
                        <el-form-item :label="$t('ip')" :label-width="formLabelWidth">
                            <el-input v-model="curObm.ip" autocomplete="off"></el-input>
                        </el-form-item>
                        <el-form-item :label="$t('username')" :label-width="formLabelWidth">
                            <el-input v-model="curObm.userName" autocomplete="off"></el-input>
                        </el-form-item>
                        <el-form-item :label="$t('pwd')" :label-width="formLabelWidth">
                            <el-input v-model="curObm.pwd" autocomplete="off" show-password></el-input>
                        </el-form-item>
                    </el-form>
                    <div slot="footer" class="dialog-footer">
                        <el-button @click="fillOutObms = false">取 消</el-button>
                        <el-button type="primary" @click="submitOBM" :loading="obmLoading">确 定</el-button>
                    </div>
                </el-dialog>

                <!--参数配置-->
                <el-dialog :title="$t('param_config')" :visible.sync="fillWfParams" ref="paramDialog">
                    <keep-alive>
                        <component v-if="editWorkflowIndex != -1 && selectedWorkflow.length > 0"
                                   v-bind:is="currentWfParamTemplate"
                                   :params="workflowParam"
                                   :currentWorkflowIndex="editWorkflowIndex"
                                   :bareMetalId="selectedWorkflow[editWorkflowIndex].bareMetalId"
                                   :workflow="selectedWorkflow[editWorkflowIndex]"
                                   ref="currentWfParamTemplate"></component>
                    </keep-alive>
                    <div class="dialog-footer" slot="footer">
                        <el-button @click="saveParams" :loading="fillWfParamsLoading">确 定</el-button>
                    </div>
                </el-dialog>

                <!--详情页-->
                <el-drawer
                        :visible.sync="detailDrawer"
                        direction="rtl"
                        :with-header="false"
                        :before-close="handleClose">
                    <div class="demo-drawer__content">
                        <el-tabs v-model="detailShowName">
                            <el-tab-pane :label="$t('detail')" name="detail">
                                    <el-form ref="form" :model="machine">
                                        <el-form-item :label="$t('machine_model')">
                                            {{machine.machineModel}}
                                        </el-form-item>
                                        <el-form-item :label="$t('machine_sn')">
                                            {{machine.machineSn}}
                                        </el-form-item>
                                        <el-form-item :label="$t('management_ip')">
                                            {{machine.managementIp}}
                                        </el-form-item>
                                        <el-form-item :label="$t('bmc_mac')">
                                            {{machine.bmcMac}}
                                        </el-form-item>
                                        <el-form-item :label="$t('cpu')">
                                            {{machine.cpuType}} X {{machine.cpu}}
                                        </el-form-item>
                                        <el-form-item :label="$t('memory')">
                                            {{machine.memory}}{{$t('GB')}}
                                        </el-form-item>
                                        <el-form-item :label="$t('disk')">
                                            {{machine.disk}}{{$t('GB')}}
                                        </el-form-item>
                                    </el-form>
                            </el-tab-pane>
                            <el-tab-pane :label="$t('cpu_detail')" name="cpuDetail" class="detail-pane">
                                <table class="detail-info">
                                    <tr>
                                        <td>{{$t('proc_name')}}</td>
                                        <td>{{$t('proc_socket')}}</td>
                                        <td>{{$t('proc_speed')}}</td>
                                        <td>{{$t('proc_num_cores')}}</td>
                                        <td>{{$t('proc_num_threads')}}</td>
                                        <td>{{$t('sync_time')}}</td>
                                    </tr>
                                    <tr v-for="c in cpus">
                                        <td>{{c.procName}}</td>
                                        <td>{{c.procSocket}}</td>
                                        <td>{{c.procSpeed}}</td>
                                        <td>{{c.procNumCores}}</td>
                                        <td>{{c.procNumThreads}}</td>
                                        <td>{{c.syncTime | dateFormat}}</td>
                                    </tr>
                                </table>
                            </el-tab-pane>
                            <el-tab-pane :label="$t('memory_detail')" name="memoryDetail" class="detail-pane">
                                <table class="detail-info">
                                    <tr>
                                        <td>{{$t('mem_cpu_num')}}</td>
                                        <td>{{$t('mem_mod_num')}}</td>
                                        <td>{{$t('mem_mod_size')}}</td>
                                        <td>{{$t('mem_mod_type')}}</td>
                                        <td>{{$t('mem_mod_num')}}</td>
                                        <td>{{$t('mem_mod_frequency')}}</td>
                                        <td>{{$t('mem_mod_part_num')}}</td>
                                        <td>{{$t('mem_mod_min_volt')}}</td>
                                        <td>{{$t('sync_time')}}</td>
                                    </tr>
                                    <tr v-for="c in memories">
                                        <td>{{c.memCpuNum}}</td>
                                        <td>{{c.memModNum}}</td>
                                        <td>{{c.memModSize+ ' GB'}}</td>
                                        <td>{{c.memModType}}</td>
                                        <td>{{c.memModNum}}</td>
                                        <td>{{c.memModFrequency + ' MHz'}}</td>
                                        <td>{{c.memModPartNum}}</td>
                                        <td>{{c.memModMinVolt}}</td>
                                        <td>{{c.syncTime | dateFormat}}</td>
                                    </tr>
                                </table>
                            </el-tab-pane>
                            <el-tab-pane :label="$t('disk_detail')" name="diskDetail" class="detail-pane">
                                <table class="detail-info">
                                    <tr>
                                        <td>{{$t('enclosure_id')}}</td>
                                        <td>{{$t('controller_id')}}</td>
                                        <td>{{$t('drive')}}</td>
                                        <td>{{$t('type')}}</td>
                                        <td>{{$t('size')}}</td>
                                        <td>{{$t('raid')}}</td>
                                        <td>{{$t('manufactor')}}</td>
                                        <td>{{$t('sn')}}</td>
                                        <td>{{$t('sync_time')}}</td>
                                    </tr>
                                    <tr v-for="c in disks">
                                        <td>{{c.enclosureId}}</td>
                                        <td>{{c.controllerId}}</td>
                                        <td>{{c.drive}}</td>
                                        <td>{{c.type}}</td>
                                        <td>{{c.size}}</td>
                                        <td>{{c.raid}}</td>
                                        <td>{{c.manufactor}}</td>
                                        <td>{{c.sn}}</td>
                                        <td>{{c.syncTime | dateFormat}}</td>
                                    </tr>
                                </table>
                            </el-tab-pane>

                            <el-tab-pane :label="$t('nic_detail')" name="nicDetail" class="detail-pane">
                                <table class="detail-info">
                                    <tr>
                                        <td>{{$t('nic_number')}}</td>
                                        <td>{{$t('mac')}}</td>
                                        <td>{{$t('sync_time')}}</td>
                                    </tr>
                                    <tr v-for="c in nics">
                                        <td>{{c.number}}</td>
                                        <td>{{c.mac}}</td>
                                        <td>{{c.syncTime | dateFormat}}</td>
                                    </tr>
                                </table>
                            </el-tab-pane>
                        </el-tabs>
                    </div>

                </el-drawer>

            </div>
        </el-tab-pane>
        <el-tab-pane :label="$t('obm')" name="second">
            <OBM></OBM>
        </el-tab-pane>

    </el-tabs>
</template>

<script>
    import HttpUtil from "../../common/utils/HttpUtil";
    import {isAnyBlank, toLine} from "../../common/utils/CommonUtil";
    import Vue from 'vue';
    import OBM from "../obm/Obm"

    let _ = require('lodash');
    export default {
        data() {
            return {
                search: null,
                fillWfParams: false,
                activeName: 'bare-metal',
                form: {},
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
                formLabelWidth: '80px',
                pageTotal: 0,
                loading: false,
                form: {},
                columns: [
                    {
                        label: this.$t('machine_sn'),
                        prop: "machineSn"
                    },
                    {
                        label: this.$t('management_ip'),
                        prop: "managementIp"
                    },
                    {
                        label: this.$t('cpu'),
                        prop: "cpu"
                    },
                    {
                        label: this.$t('memory'),
                        prop: "memory"
                    },
                    {
                        label: this.$t('disk'),
                        prop: "disk"
                    },
                    // {
                    //     label: this.$t('status'),
                    //     prop: "status"
                    // },
                ],
                detailDrawer: false,
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
                wfRequest: {},
                workflowParamList: [],
                workflowParam: {},
                paramEditable: false,
                selectedWorkflow: [],
                fillOutObms: false,
                curObm: {
                    ip: null,
                    userName: null,
                    pwd: null
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
                    searchKey: this.search
                }
            }
        },
        components: {
            OBM
        },
        computed: {
            filterList: function () {
                return this.allGraphDefinitions.filter(function (item) {
                        return item.injectableName == 'Graph.InstallCentOS';
                    }
                )
            },
        },
        mounted() {
            this.getData();
            this.getAllGraphDefinitions();
        }
        ,
        methods: {
            load(tree, treeNode, resolve) {
                HttpUtil.post("/execution-log/detaillist/" + 1 + "/" + 1000, {bareMetalId: tree.id}, (res) => {
                    // that.$set(a, 'children', res.data.listObject);
                    // that.$set(a, 'logs', res.data.listObject);
                    resolve(res.data.listObject);
                });
            },
            expandChange(a) {
                let that = this;
                HttpUtil.post("/execution-log/detaillist/" + 1 + "/" + 1000, {bareMetalId: a.id}, (res) => {
                    // that.$set(a, 'children', res.data.listObject);
                    that.$set(a, 'logs', res.data.listObject);
                });
            },
            buildRequest(workflow) {
                let request = JSON.parse(JSON.stringify(workflow));
                delete request.componentId;
                delete request.machineModel;
                delete request.machineSn;
                return request;
            },
            saveParams() {
                this.selectedWorkflow[this.editWorkflowIndex].params = this.$refs.currentWfParamTemplate.payLoad;
                this.fillWfParamsLoading = true;
                HttpUtil.post("/workflow/params", this.buildRequest(this.selectedWorkflow[this.editWorkflowIndex]), (res) => {
                    this.fillWfParamsLoading = false;
                    this.fillWfParams = false;
                });
            },
            copy(obj) {
                return JSON.parse(JSON.stringify(obj));
            },
            editWfParams(index) {
                this.editWorkflowIndex = index;
                this.fillWfParams = true;
                this.currentWfParamTemplate = this.selectedWorkflow[index].componentId;
                if (this.workflowParamList.length) {
                    this.workflowParam = this.selectedWorkflow[index].params;
                }
            },
            power(opt, row) {
                HttpUtil.get("/bare-metal/power/" + row.id + "/" + opt, null, (res) => {
                    this.$message.success($t('success!'));
                });
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
            fillOBM(val) {
                this.curObm.ip = val.managementIp;
                this.curObm.userName = null;
                this.curObm.pwd = null;
                if (val.outBandList.length > 0) {
                    this.curObm.userName = val.outBandList[0].userName;
                    this.curObm.pwd = val.outBandList[0].pwd;
                }
                this.curObm.bareMetalId = val.id;
                this.fillOutObms = true;
            },
            submitOBM() {
                this.obmLoading = true;
                if (isAnyBlank(this.curObm.ip, this.curObm.userName, this.curObm.pwd)) {
                    this.$notify.error(this.$t('pls_fill_in_blanks'));
                    this.obmLoading = false;
                    return;
                }
                HttpUtil.post("/outband/save?bareMetalId=" + this.curObm.bareMetalId, this.curObm, (res) => {
                    this.obmLoading = false;
                    this.curObm = {};
                    this.fillOutObms = false;
                    this.getData();
                }, (res) => {
                    this.obmLoading = false;
                });
            },
            sortChange(val) {
                console.log(val);
                console.log(val.order);
                if (val.order) {
                    this.queryVO = {
                        searchKey: '%' + this.search + '%',
                        sort: toLine(val.prop) + " " + val.order.replace("ending", "")
                    }
                } else {
                    delete this.queryVO.sort;
                }
                this.getData();
            },
            getData() {
                if (this.search) {
                    this.queryVO.searchKey = '%' + this.search + '%';
                } else {
                    this.queryVO.searchKey = null;
                }
                HttpUtil.post("/bare-metal/list/" + this.query.pageIndex + "/" + this.query.pageSize, this.queryVO, (res) => {
                    this.tableData = res.data.listObject;
                    this.pageTotal = res.data.itemCount;
                });
            },
            handleSizeChange(val) {
                this.query.pageSize = val;
                this.handlePageChange(this.query.pageIndex);
            },
            handleClose() {
                this.detailDrawer = false;
            },
            cancelForm() {
                this.loading = false;
                this.detailDrawer = false;
            },
            add() {
                this.detailDrawer = true;
                this.editType = 'add';
            },
            confirmEdit() {
                this.loading = true;
                if (this.editType == 'edit') {
                    HttpUtil.post("/bare-metal/update", this.editObj, (res) => {
                        this.detailDrawer = false;
                        this.$message.success('编辑成功');
                        this.cancelForm();
                        this.getData();
                    })
                } else {
                    HttpUtil.post("/bare-metal/add", this.editObj, (res) => {
                        this.detailDrawer = false;
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
            getSelectedIds: function () {
                this.delList = this.delList.concat(this.multipleSelection);
                let ids = _.map(this.delList, (item) => item.id);
                return ids;
            },
            delAllSelection() {
                const length = this.multipleSelection.length;
                let str = '';
                for (let i = 0; i < length; i++) {
                    str += this.multipleSelection[i].name + ' ';
                }
                let ids = this.getSelectedIds();
                HttpUtil.post("/bare-metal/del", ids, (res) => {
                    this.$message.success(`删除成功！删除了${str}！`);
                    this.getData();
                });
                this.multipleSelection = [];
            },
            // 编辑操作
            handleEdit(row, type) {
                if (type == 'edit') {
                    this.detailDrawer = true;
                    this.editType = type;
                    this.editObj = JSON.parse(JSON.stringify(row));
                    this.editObj.rolesIds = _.map(this.editObj.roles, (item) => item.id);

                } else if (type == 'del') {
                    this.$confirm('确定要删除吗？', '提示', {
                        type: 'warning'
                    }).then(() => {
                        HttpUtil.get("/bare-metal/del/" + row.id, {}, (res) => {
                            this.getData();
                            this.$message.success('删除成功');
                        });
                    })
                } else {
                    this.detailDrawer = true;
                    this.editType = type;
                    this.editObj = {};
                }
            },
            // 分页导航
            handlePageChange(val) {
                this.$set(this.query, 'pageIndex', val);
                this.getData();
            },
            getAllGraphDefinitions(name) {
                HttpUtil.get("/rackhd/graphdefinitions/1/1000", {name: name}, (res) => {
                    this.allGraphDefinitions = res.data.listObject;
                });
            },
            runWorkflow() {

                if (!this.selectedWorkflow.length) {
                    this.$notify.error(this.$t('pls_select_workflow') + "!");
                    return;
                }
                let that = this;
                let reqList = _.map(this.copy(this.selectedWorkflow), (wf) => {
                    return that.buildRequest(wf);
                });
                if (reqList.length == 0) {
                    this.$notify.error(this.$t('pls_select_node') + "!");
                    return;
                }

                HttpUtil.post("/workflow/run", reqList, (res) => {
                    this.getData();
                });
            }
            ,
            getParamsTemplate() {
                HttpUtil.get("/workflow/params/" + this.wfRequest.workflow, {}, (res) => {
                    if (res.data[0]) {
                        this.workflowParamList = res.data;
                    } else {
                        this.workflowParamList = [];
                        this.workflowParam = null;
                    }
                })
            }
            ,
            createWorkflowParamComponent(workflowParam) {
                //动态异步
                if (!this.paramComponent[workflowParam.componentId]) {
                    let comPointer = Vue.component(workflowParam.componentId, function (resolve) {
                        require(["./../../rackparams/" + workflowParam.workflowName], resolve)
                    });
                    this.paramComponent[workflowParam.componentId] = comPointer;
                }
            },
            addToSelectedWorkflow() {
                if (this.wfRequest.workflow) {
                    for (let i = 0; i < this.multipleSelection.length; i++) {
                        let componentId = this.wfRequest.workflow + "-" + this.multipleSelection[i].id;
                        for (let j = 0; j < this.selectedWorkflow.length; j++) {
                            if (this.selectedWorkflow[j].componentId == componentId) {
                                this.$notify.error(this.$t('same_workflow_node'));
                                return;
                            }
                        }
                    }
                    for (let i = 0; i < this.multipleSelection.length; i++) {
                        this.selectedWorkflow.push(
                            {
                                componentId: this.wfRequest.workflow + "-" + this.multipleSelection[i].id,
                                bareMetalId: this.multipleSelection[i].id,
                                machineModel: this.multipleSelection[i].machineModel,
                                machineSn: this.multipleSelection[i].machineSn,
                                workflowName: this.wfRequest.workflow,
                            }
                        );

                        this.createWorkflowParamComponent(this.selectedWorkflow[this.selectedWorkflow.length - 1]);
                        if (this.workflowParamList.length) {
                            let that = this;
                            let paramTemplate = _.find(that.workflowParamList, function (p) {
                                return p.bareMetalId == that.selectedWorkflow[that.selectedWorkflow.length - 1].bareMetalId;
                            });
                            if (paramTemplate == null)
                                that.$set(that, 'workflowParam', null);
                            else
                                that.$set(that, 'workflowParam', JSON.parse(paramTemplate.paramsTemplate));

                            that.selectedWorkflow[that.selectedWorkflow.length - 1].params = that.workflowParam;
                        }
                    }
                    this.$refs.multipleTable.clearSelection();
                }
            }
            ,
            deleteSelectedWorkflow(index) {
                if (this.$refs.currentWfParamTemplate)
                    this.$refs.currentWfParamTemplate.$destroy(true);
                this.selectedWorkflow.splice(index, 1);
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

    #run-workflow {
        border: solid #d7d2d2 1px;
        height: 120px;
        padding: 10px 10px 15px 10px;
        border-radius: 5px;
        min-width: 120px;
    }

    #workflow-selector {
        border: solid #d7d2d2 1px;
        min-width: 420px;
        height: 120px;
        padding: 10px 10px 15px 10px;
        border-radius: 5px;
        margin-left: 10px;
    }

    #action-list {
        border: solid #d7d2d2 1px;
        width: 100%;
        height: 120px;
        padding: 10px 10px 15px 10px;
        border-radius: 5px;
        margin-left: 10px;
        overflow: auto;
    }

    .h25 {
        height: 25%;
    }

    #control {
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
        overflow-x: scroll;
        color: #303133;
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

</style>