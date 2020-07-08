<template>
  <div id="app">
    <el-container style=" border: 1px solid #eee" v-if="login">
      <el-header id="main-header">
        <span class="user-name">王小虎</span>
        <el-dropdown id="dropdown">
          <i class="el-icon-setting" style="margin-right: 15px"></i>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item>查看</el-dropdown-item>
            <el-dropdown-item>新增</el-dropdown-item>
            <el-dropdown-item>{{$t('del')}}</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>

      </el-header>
      <el-container id="main-container">
        <el-aside width="200px;" style="background-color: rgb(238, 241, 246); margin-top: 10px;">
          <el-menu :default-openeds="['1', '3']">
            <el-submenu :index="m.order" v-for="m in menus" v-bind:key="m.order">
              <template slot="title"><i class="el-icon-message"></i>{{m.name}}</template>
              <el-menu-item :index="c.order" v-for="c in m.childs" v-bind:key="c.order"
                            @click="$router.push(c.router)">
                {{c.name}}
              </el-menu-item>
            </el-submenu>
          </el-menu>
        </el-aside>

        <el-container>
          <router-view></router-view>
        </el-container>
      </el-container>
    </el-container>

    <router-view v-if="!login"></router-view>
  </div>
</template>

<script>
  import menu from './components/menu/menu'

  export default {
    name: 'App',
    data() {
      return {
        menus: menu.menus,
        login: localStorage.getItem("login") == "true"
      };
    }
  };
</script>

<style>
  body {
    /*font-family: -apple-system, BlinkMacSystemFont, "Neue Haas Grotesk Text Pro", "Arial Nova", "Segoe UI", "Helvetica Neue", ".PingFang SC", "PingFang SC", "Source Han Sans SC", "Noto Sans CJK SC", "Source Han Sans CN", "Noto Sans SC", "Source Han Sans TC", "Noto Sans CJK TC", "Hiragino Sans GB", sans-serif;*/
    font-size: 10px !important;
    background-color: #F5F5F5;
    /*line-height: 26px;*/
    color: #2B415C;
    -webkit-font-smoothing: antialiased;
    margin: 0;
    padding: 0;
    font-family: Roboto, "PingFang SC", "Helvetica Neue", sans-serif;
  }

  .form .el-input > .el-input__inner {
    border-radius: 0;
  }

  .el-aside {
    color: #333;
  }

  .container {
    padding: 30px;
    background: #fff;
    border: 1px solid #ddd;
    border-radius: 5px;
    margin: 10px 10px;
    width: 100%;
    height: calc(100vh - 135px);
  }

  .user-name {
    display: inline-block;
    margin-right: 10px;
    color: #FFFFFF;
  }

  #main-header {
    text-align: right;
    font-size: 12px;
    height: 45px !important;
    background-color: #409EFF;;
    color: #333;
    line-height: 45px;
    box-shadow: 0 1px 5px 0 rgba(0, 0, 0, .2), 0 2px 2px 0 rgba(0, 0, 0, .14), 0 3px 1px -2px rgba(0, 0, 0, .12);
  }

  #main-container {
    margin: 0;
    font-size: 14px !important;
  }

  #dropdown {
    color: #ffffff;
  }
</style>
