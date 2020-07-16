<template>
    <div class="container">
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

        <div id="control" style="display: flex;">
            <div id="run-workflow">
                <div class="el-icon-caret-right h25" style="border-bottom: yellowgreen 1px solid;    width: 100%;">Run
                </div>
                <div class="run-splitter h25"></div>
                <el-button class="el-icon-caret-right h50"></el-button>
                <el-button class="el-icon-close h50"></el-button>
            </div>
            <div id="workflow-selector" style="display: flex;">
                <div id="select-workflow">
                    <div class="el-icon-menu h25" style="border-bottom: yellowgreen 1px solid;    width: 100%;">Workflow
                    </div>
                    <div class="run-splitter h25"></div>
                    <!--                    <el-button class="h50">Workflow <span class="el-icon-caret-bottom"></span></el-button>-->
                    <el-select v-model="wfRequest.workflow">
                        <el-option v-for="g in allGraphDefinitions" :label="g.injectableName" :value="g.id"></el-option>
                    </el-select>
                    <el-button class="el-icon-close h50"></el-button>
                </div>
            </div>
        </div>

        <el-table
                :data="tableData"
                class="table"
                ref="multipleTable"
                header-cell-class-name="table-header"
                style="width: 100%"
                @selection-change="handleSelectionChange"
        >
            <el-table-column type="selection" align="center"></el-table-column>
            <el-table-column :prop="c.prop" :label="c.label" align="center"
                             v-for="c in columns"></el-table-column>

            <el-table-column prop="" :label="$t('opt')" align="center">
                <template slot-scope="scope">
                    <el-dropdown>
                        <el-button type="primary">
                            操作<i class="el-icon-arrow-down el-icon--right"></i>
                        </el-button>
                        <el-dropdown-menu slot="dropdown">
                            <el-dropdown-item>poweron</el-dropdown-item>
                            <el-dropdown-item>poweroff</el-dropdown-item>
                            <el-dropdown-item>powercycle</el-dropdown-item>
                            <el-dropdown-item>nextbootpxe</el-dropdown-item>
                            <el-dropdowKn-item>nextbootdisk</el-dropdowKn-item>
                        </el-dropdown-menu>
                    </el-dropdown>
                </template>
            </el-table-column>
        </el-table>
        <div class="pagination">
            <el-pagination
                    background
                    layout="total, prev, pager, next"
                    :current-page="query.pageIndex"
                    :page-size="query.pageSize"
                    :total="pageTotal"
                    @current-change="handlePageChange"
            ></el-pagination>
        </div>

        <el-drawer
                :title="editType == 'edit' ? '编辑用户' : '新增用户'"
                :visible.sync="drawer"
                direction="rtl"
                :before-close="handleClose">
            <div class="demo-drawer__content">
                <el-form :model="editObj">
                    <el-form-item label="ID" :label-width="formLabelWidth">
                        <el-input v-model="editObj.id" autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item :label="$t('name')" :label-width="formLabelWidth">
                        <el-input v-model="editObj.name" autocomplete="off"
                                  :placeholder="$t('pls_input_name')"></el-input>
                    </el-form-item>
                    <el-form-item :label="$t('email')" :label-width="formLabelWidth">
                        <el-input v-model="editObj.email" autocomplete="off"
                                  :placeholder="$t('pls_input_email')"></el-input>
                    </el-form-item>
                    <el-form-item :label="$t('phone')" :label-width="formLabelWidth">
                        <el-input v-model="editObj.phone" autocomplete="off"
                                  :placeholder="$t('pls_input_phone')"></el-input>
                    </el-form-item>
                    <el-form-item :label="$t('role')" :label-width="formLabelWidth">
                        <el-select v-model="editObj.rolesIds" multiple :placeholder="$t('pls_select')">
                            <el-option
                                    v-for="(item, key) in allRoles"
                                    :key="item.id"
                                    :label="item.name"
                                    :value="item.id">
                            </el-option>
                        </el-select>
                    </el-form-item>
                </el-form>
                <div class="demo-drawer__footer">
                    <el-button @click="cancelForm">{{$t('cancel')}}</el-button>
                    <el-button type="primary" @click="confirmEdit" :loading="loading">{{ loading ? $t('submitting') +
                        '...' : $t('confirm')
                        }}
                    </el-button>
                </div>
            </div>
        </el-drawer>
    </div>
</template>

<script>
    import HttpUtil from "../../common/utils/HttpUtil";

    let _ = require('lodash');
    export default {
        data() {
            return {
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
                    {
                        label: this.$t('machine_model'),
                        prop: "machineModel"
                    },
                    {
                        label: this.$t('machine_sn'),
                        prop: "machineSn"
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
                drawer: false,
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
                wfRequest: {}
            };
        },
        mounted() {
            this.getData();
            this.getAllGraphDefinitions();
        },
        methods: {
            c(e) {

            },
            getData() {
                HttpUtil.post("/bare-metal/list/" + this.query.pageIndex + "/" + this.query.pageSize, {}, (res) => {
                    this.tableData = res.data.listObject;
                    this.pageTotal = res.data.itemCount;
                });
            },
            handleClose() {
                this.drawer = false;
            },
            cancelForm() {
                this.loading = false;
                this.drawer = false;
            },
            add() {
                this.drawer = true;
                this.editType = 'add';
            },
            confirmEdit() {
                this.loading = true;
                if (this.editType == 'edit') {
                    HttpUtil.post("/bare-metal/update", this.editObj, (res) => {
                        this.drawer = false;
                        this.$message.success('编辑成功');
                        this.cancelForm();
                        this.getData();
                    })
                } else {
                    HttpUtil.post("/bare-metal/add", this.editObj, (res) => {
                        this.drawer = false;
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
            delAllSelection() {
                const length = this.multipleSelection.length;
                let str = '';
                this.delList = this.delList.concat(this.multipleSelection);
                for (let i = 0; i < length; i++) {
                    str += this.multipleSelection[i].name + ' ';
                }
                let ids = _.map(this.delList, (item) => item.id);
                HttpUtil.post("/bare-metal/del", ids, (res) => {
                    this.$message.success(`删除成功！删除了${str}！`);
                    this.getData();
                });
                this.multipleSelection = [];
            },
            // 编辑操作
            handleEdit(row, type) {
                if (type == 'edit') {
                    this.drawer = true;
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
                    this.drawer = true;
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
        width: 141px;
        height: 120px;
        padding: 10px 10px 20px 10px;
        border-radius: 5px;
    }

    #workflow-selector {
        border: solid #d7d2d2 1px;
        width: 100%;
        height: 120px;
        padding: 10px 0 20px 10px;
        border-radius: 5px;
        margin-left: 10px;
    }

    .h25 {
        height: 25%;
    }

    .h50 {
        height: 50%;
    }

</style>