<template>
    <div class="container">
        <div class="handle-box">
            <el-button
                    type="primary"
                    icon="el-icon-delete"
                    class="handle-del mr10"
                    @click="delAllSelection"
            >{{$t('batch_del')}}
            </el-button>
            <el-button
                    type="primary"
                    icon="el-icon-delete"
                    class="handle-del mr10"
                    @click="handleEdit({}, 'add')"
            >{{$t('add')}}
            </el-button>
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
                    <el-button
                            type="text"
                            icon="el-icon-edit"
                            @click="handleEdit(scope.row, 'edit')"
                    >{{$t('edit')}}
                    </el-button>
                    <!--                    <el-button-->
                    <!--                            type="text"-->
                    <!--                            icon="el-icon-delete"-->
                    <!--                            @click="handleEdit(scope.row, 'del')"-->
                    <!--                    >{{$t('del')}}-->
                    <!--                    </el-button>-->

                    <el-button
                            type="text"
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
                <el-form :model="form">
                    <el-form-item label="活动名称" :label-width="formLabelWidth">
                        <el-input v-model="form.name" autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="活动区域" :label-width="formLabelWidth">
                        <el-select v-model="form.region" placeholder="请选择活动区域">
                            <el-option label="区域一" value="shanghai"></el-option>
                            <el-option label="区域二" value="beijing"></el-option>
                        </el-select>
                    </el-form-item>
                </el-form>
                <div class="demo-drawer__footer">
                    <el-button @click="cancelForm">取 消</el-button>
                    <el-button type="primary" @click="$refs.drawer.closeDrawer()" :loading="loading">{{ loading ? '提交中 ...' : '确 定' }}</el-button>
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
                        label: this.$t('id'),
                        prop: "id"
                    },
                    {
                        label: this.$t('name'),
                        prop: "name"
                    },
                    {
                        label: this.$t('email'),
                        prop: "email"
                    },
                ],
                drawer: false,
                editType: 'edit',
                editRole: {
                    name: null,
                    description: null,
                    type: null
                }
            };
        },
        mounted() {
            this.getData();
        },
        methods: {
            // 获取 easy-mock 的模拟数据
            getData() {
                HttpUtil.post("/user/list/" + this.query.pageIndex + "/" + this.query.pageSize, {}, (res) => {
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
                if (this.editType == 'edit') {
                    HttpUtil.post("/user/update", this.editRole, (res) => {
                        this.drawer = false;
                        this.$message.success('编辑成功');
                        this.getData();
                    })
                } else {
                    HttpUtil.post("/user/add", this.editRole, (res) => {
                        this.drawer = false;
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
                HttpUtil.post("/user/del", ids, (res) => {
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
                    this.editRole = JSON.parse(JSON.stringify(row));
                } else if (type == 'del') {
                    this.$confirm('确定要删除吗？', '提示', {
                        type: 'warning'
                    }).then(() => {
                        HttpUtil.get("/user/del/" + row.id, {}, (res) => {
                            this.getData();
                            this.$message.success('删除成功');
                        });
                    })
                } else {
                    this.drawer = true;
                    this.editType = type;
                    this.editRole = {};
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