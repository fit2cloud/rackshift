<template>
    <el-tabs v-model="activeName" style="width:100vw">
        <el-tab-pane label="物理机" name="bare-metal">
            <div>
                <!--        <div class="handle-box">-->
                <!--            <el-button-group>-->
                <!--                <el-button-->
                <!--                        type="primary"-->
                <!--                        icon="el-icon-delete"-->
                <!--                        class="handle-del mr10"-->
                <!--                        @click="delAllSelection"-->
                <!--                >{{$t('batch_del')}}-->
                <!--                </el-button>-->
                <!--                <el-button-->
                <!--                        type="primary"-->
                <!--                        icon="el-icon-delete"-->
                <!--                        class="handle-del mr10"-->
                <!--                        @click="handleEdit({}, 'add')"-->
                <!--                >{{$t('add')}}-->
                <!--                </el-button>-->
                <!--            </el-button-group>-->
                <!--        </div>-->
                <div class="machine-title">
                    <i class="el-icon-user-solid">Machines</i>

                    <el-button-group class="batch-button">
                        <el-button type="primary" icon="el-icon-edit"></el-button>
                        <el-button type="primary" icon="el-icon-share"></el-button>
                        <el-button type="primary" icon="el-icon-delete"></el-button>
                    </el-button-group>
                </div>
                <div id="control" style="display: flex;">
                    <div id="run-workflow">
                        <div class="el-icon-caret-right h25"
                             style="border-bottom: yellowgreen 1px solid;    width: 100%;">Run
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
                                <el-option v-for="g in allGraphDefinitions" :label="g.injectableName"
                                           :value="g.injectableName"></el-option>
                            </el-select>
                            <!--                    <el-button class="el-icon-close h50"></el-button>-->
                        </div>
                        <div id="select-params">
                            <div class="el-icon-s-operation h25"
                                 style="border-bottom: yellowgreen 1px solid;    width: 100%;">
                                Params
                            </div>
                            <div class="run-splitter h25"></div>
                            <el-button class="h50 ml10" :disabled="!paramEditable">{{$t('Set')}}</el-button>
                            <!--                    <el-button class="h50 ml10">{{$t('Set')}} <span class="el-icon-caret-bottom"></span></el-button>-->

                        </div>

                        <div id="workflow-todo">
                            <div class="el-icon-s-operation h25"
                                 style="border-bottom: yellowgreen 1px solid;    width: 100%;">
                                Add to List
                            </div>
                            <div class="run-splitter h25"></div>
                            <el-button class="h50 ml10" @click="addToSelectedWorkflow" :disabled="this.multipleSelection.length == 0"><span
                                    class="el-icon-circle-plus"></span>{{$t('add_to_action_list')}}
                            </el-button>

                        </div>
                    </div>

                    <div id="action-list">
                        <div class="el-icon-s-operation h25"
                             style="border-bottom: yellowgreen 1px solid;    width: 100%;">
                            {{$t('selected_workflows')}}
                        </div>
                        <div>
                            <el-card v-for="w in selectedWorkflow">
                                <div>
                                    {{w}}
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
                        @selection-change="handleSelectionChange"
                >
                    <el-table-column type="selection" align="center"></el-table-column>

                    <el-table-column prop="machineModel" :label="$t('machine_model')" align="center"
                                     sortable="custom">
                        <template slot-scope="scope">
                            <el-tooltip class="item" effect="dark" :content="$t('detail')" placement="right-end">
                                <el-link type="primary" @click="detailDrawer = true" target="_blank">
                                    {{scope.row.machineModel}}
                                </el-link>
                            </el-tooltip>
                        </template>
                    </el-table-column>

                    <el-table-column :prop="c.prop" :label="c.label" align="center"
                                     v-for="c in columns" sortable="custom"></el-table-column>

                    <el-table-column prop="" :label="$t('opt')" align="center">

                        <template slot-scope="scope">
                            <el-dropdown>
                                <el-button type="primary">
                                    {{$t('opt')}}<i class="el-icon-arrow-down el-icon--right"></i>
                                </el-button>
                                <el-dropdown-menu slot="dropdown">
                                    <el-dropdown-item @click.native="power('on', scope.row)">poweron</el-dropdown-item>
                                    <el-dropdown-item @click.native="power('off', scope.row)">poweroff
                                    </el-dropdown-item>
                                    <el-dropdown-item @click.native="power('reset', scope.row)">powercycle
                                    </el-dropdown-item>
                                    <el-dropdown-item @click.native="power('pxe', scope.row)">pxeboot
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

                <!--详情页-->
                <el-drawer
                        :visible.sync="detailDrawer"
                        direction="rtl"
                        :with-header="false"
                        :before-close="handleClose">
                    <div class="demo-drawer__content">
                        <el-tabs>
                            <el-tab-pane :label="$t('detail')" name="detail">
                                <el-card class="box-card">
                                    <div>

                                    </div>
                                </el-card>
                            </el-tab-pane>
                            <el-tab-pane :label="$t('cpu')" name="cpu">

                            </el-tab-pane>
                        </el-tabs>
                        <div class="demo-drawer__footer">
                            <el-button @click="cancelForm">{{$t('cancel')}}</el-button>
                            <el-button type="primary" @click="confirmEdit" :loading="loading">{{ loading ?
                                $t('submitting') +
                                '...' : $t('confirm')
                                }}
                            </el-button>
                        </div>
                    </div>
                </el-drawer>

            </div>
        </el-tab-pane>
        <el-tab-pane label="配置管理" name="second">配置管理</el-tab-pane>

    </el-tabs>
</template>

<script>
    import HttpUtil from "../../common/utils/HttpUtil";
    import {isAnyBlank} from "../../common/utils/CommonUtil";

    let _ = require('lodash');
    export default {
        data() {
            return {
                activeName: 'bare-metal',
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
                form: {},
                loading:false,
                idx: -1,
                id: -1,
                columns: [
                    // {
                    //     label: this.$t('machine_model'),
                    //     prop: "machineModel"
                    // },
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
                    {
                        label: this.$t('status'),
                        prop: "status"
                    },
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
                workflowParam: {},
                paramEditable: false,
                selectedWorkflow: [],
                fillOutObms: false,
                curObm: {
                    ip: null,
                    userName: null,
                    pwd: null
                },
                obmLoading: false
            };
        },
        mounted() {
            this.getData();
            this.getAllGraphDefinitions();
        },
        methods: {
            power(opt, row) {
                HttpUtil.get("/bare-metal/power/" + row.id + "/" + opt, null, (res) => {
                    this.$message.success($t('success!'));
                });
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
                console.log(val.order);
                alert(val.prop);
            },
            getData() {
                HttpUtil.post("/bare-metal/list/" + this.query.pageIndex + "/" + this.query.pageSize, {}, (res) => {
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
                    ;
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
                })
            },
            runWorkflow() {
                let ids = this.getSelectedIds();
                if (!this.wfRequest.workflow) {
                    this.$notify.error(this.$t('pls_select_workflow') + "!");
                    return;
                }
                let selList = this.multipleSelection;


                HttpUtil.post("/workflow/run", reqList, (res) => {
                    this.getData();
                })
            },
            getParamsTemplate() {
                HttpUtil.get("/workflow/params/" + this.wfRequest.workflow, {}, (res) => {
                    this.workflowParam = res.data;
                    if (this.workflowParam.length > 0) {
                        this.paramEditable = true;
                    } else {
                        this.paramEditable = false;
                    }
                })
            },
            addToSelectedWorkflow() {
                if (this.wfRequest.workflow) {
                    this.selectedWorkflow.push(this.wfRequest.workflow);
                    // this.wfRequest.workflow = null;
                }
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
    }

    #workflow-selector {
        border: solid #d7d2d2 1px;
        /*width: 100%;*/
        height: 120px;
        padding: 10px 10px 15px 10px;
        border-radius: 5px;
        margin-left: 10px;
    }

    #action-list {
        border: solid #d7d2d2 1px;
        width: 400px;
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

</style>