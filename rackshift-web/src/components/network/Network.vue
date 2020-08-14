<template>
    <div class="container">

        <div class="machine-title">
            <i class="el-icon-user-solid">{{$t('Networks')}}</i>
            <div class="el-button-group batch-button">
                <button type="button" class="el-button el-button--primary" @click="delAllSelection"><i
                        class="el-icon-delete"></i>{{$t('batch_del')}}
                </button>
                <button type="button" class="el-button el-button--primary" @click="handleEdit({}, 'add')"><i
                        class="el-icon-document-add"></i>{{$t('add')}}
                </button>
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
            <el-table-column :prop="c.prop" :formatter="getValidProText" :label="c.label" align="center"
                             v-for="c in columns" sortable></el-table-column>
            <el-table-column prop="createTime" :label="$t('create_time')" align="center">
                <template slot-scope="scope">
                    {{scope.row.createTime | dateFormat}}
                </template>
            </el-table-column>
            <el-table-column prop="" :label="$t('opt')" align="center">
                <template slot-scope="scope">
                    <el-button
                            type="button"
                            icon="el-icon-edit"
                            @click="handleEdit(scope.row, 'edit')"
                    >{{$t('edit')}}
                    </el-button>

                    <el-button
                            type="button"
                            icon="el-icon-delete"
                            class="red"
                            @click="handleEdit(scope.row, 'del')"
                    >{{$t('del')}}
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
                :title="editType == 'edit' ? $t('edit_network') : $t('add_network')"
                :visible.sync="editDialogVisible"
                direction="rtl"
                :before-close="handleClose">
            <div class="demo-drawer__content">
                <el-form :model="editObj">
                    <el-form-item :label="$t('name')">
                        <el-input v-model="editObj.name" autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item :label="$t('vlanId')">
                        <el-input v-model="editObj.vlanId" autocomplete="off" type="number"
                                  :placeholder="$t('pls_input_vlan_id')"></el-input>
                    </el-form-item>

                    <el-form-item :label="$t('startIp')">
                        <el-input v-model="editObj.startIp" autocomplete="off"
                                  :placeholder="$t('pls_input_start_ip')"></el-input>
                    </el-form-item>

                    <el-form-item :label="$t('endIp')">
                        <el-input v-model="editObj.endIp" autocomplete="off"
                                  :placeholder="$t('pls_input_end_ip')"></el-input>
                    </el-form-item>

                    <el-form-item :label="$t('netmask')">
                        <el-input v-model="editObj.netmask" autocomplete="off"
                                  :placeholder="$t('pls_input_netmask')"></el-input>
                    </el-form-item>

                    <el-form-item :label="$t('dhcp_enable')">
                        <el-switch
                                v-model="editObj.dhcpEnable"
                                active-color="#13ce66"
                                inactive-color="#ff4949">
                        </el-switch>
                    </el-form-item>

                    <el-form-item :label="$t('pxe_enable')">
                        <el-switch
                                v-model="editObj.pxeEnable"
                                active-color="#13ce66"
                                inactive-color="#ff4949">
                        </el-switch>
                    </el-form-item>
                </el-form>
                <div class="demo-drawer__footer">
                    <el-button @click="editDialogVisible = false">{{$t('cancel')}}</el-button>
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

    import HttpUtil from "../../common/utils/HttpUtil"

    let _ = require('lodash');
    export default {
        data() {
            return {
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
                form: {},
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
                    },
                    {
                        label: this.$t('vlan_id'),
                        prop: "vlanId"
                    },
                    {
                        label: this.$t('dhcp_enable'),
                        prop: "dhcpEnable"
                    },
                    {
                        label: this.$t('pxe_enable'),
                        prop: "pxeEnable"
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
                allOsVersion: []
            };
        },
        mounted() {
            this.getData();
        },
        methods: {
            // 获取 easy-mock 的模拟数据
            getValidProText(row, column, cellValue, index) {
                if (cellValue == true) {
                    return this.$t("enabled");
                } else if (cellValue && cellValue == false) {
                    return this.$t("disabled");
                }
                return cellValue;
            },
            getData() {
                HttpUtil.post("/network/list/" + this.query.pageIndex + "/" + this.query.pageSize, {}, (res) => {
                    this.tableData = res.data.listObject;
                    this.pageTotal = res.data.itemCount;
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
                if (this.editType == 'edit') {
                    HttpUtil.post("/network/update", this.editObj, (res) => {
                        this.editDialogVisible = false;
                        this.$message.success('编辑成功');
                        this.getData();
                    })
                } else {
                    HttpUtil.post("/network/add", this.editObj, (res) => {
                        this.editDialogVisible = false;
                        this.$message.success('新增成功');
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
                HttpUtil.post("/network/del", ids, (res) => {
                    this.$message.success(`删除成功！删除了${str}！`);
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
                } else if (type == 'del') {
                    this.$confirm('确定要删除吗？', '提示', {
                        type: 'warning'
                    }).then(() => {
                        HttpUtil.get("/network/del/" + row.id, {}, (res) => {
                            this.getData();
                            this.$message.success('删除成功');
                        });
                    })
                } else {
                    this.editDialogVisible = true;
                    this.editType = type;
                    this.editObj = {};
                }
            },
            // 分页导航
            handlePageChange(val) {
                this.$set(this.query, 'pageIndex', val);
                this.getData();
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