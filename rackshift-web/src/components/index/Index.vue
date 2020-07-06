<template>
    <el-container style=" border: 1px solid #eee">
        <el-header style="text-align: right; font-size: 12px">
            <el-dropdown>
                <i class="el-icon-setting" style="margin-right: 15px"></i>
                <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item>查看</el-dropdown-item>
                    <el-dropdown-item>新增</el-dropdown-item>
                    <el-dropdown-item>删除</el-dropdown-item>
                </el-dropdown-menu>
            </el-dropdown>
            <span>王小虎</span>
        </el-header>
        <el-container>
            <el-aside width="200px" style="background-color: rgb(238, 241, 246)">
                <el-menu :default-openeds="['1', '3']">
                    <el-submenu :index="m.order" v-for="m in menus" v-bind:key="m.order">
                        <template slot="title"><i class="el-icon-message"></i>{{m.name}}</template>
                        <el-menu-item :index="c.order" v-for="c in m.childs" v-bind:key="c.order">
                            {{c.name}}
                        </el-menu-item>
                    </el-submenu>
                </el-menu>
            </el-aside>

            <el-main width="400px">
                <el-table :data="tableData">
                    <el-table-column prop="date" label="日期" width="140">
                    </el-table-column>
                    <el-table-column prop="name" label="姓名" width="120">
                    </el-table-column>
                    <el-table-column prop="address" label="地址">
                    </el-table-column>
                </el-table>
            </el-main>
        </el-container>
    </el-container>
</template>
<style>
    .el-header {
        background-color: #B3C0D1;
        color: #333;
        line-height: 60px;
    }

    .el-aside {
        color: #333;
    }
</style>

<script>
    import menu from '../menu/menu'

    export default {
        data() {
            const item = {
                date: '2016-05-02',
                name: '王小虎',
                address: '上海市普陀区金沙江路 1518 弄'

            };
            return {
                tableData: Array(10).fill(item),
                menus: menu.menus
            };
        }
    };
</script>