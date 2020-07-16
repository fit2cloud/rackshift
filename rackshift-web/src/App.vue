<template>
  <div id="app">
    <el-container style=" border: 1px solid #eee" v-if="login">
      <el-header id="main-header">
        <span id="main-title">RackShift</span>
        <span class="user-name">{{user.name}}</span>
        <el-dropdown id="dropdown">
          <i class="el-icon-setting" style="margin-right: 15px;cursor: pointer;"></i>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item>{{$t('info')}}</el-dropdown-item>
            <el-dropdown-item><span v-on:click="logout">{{$t('logout')}}</span></el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>

      </el-header>
      <el-container id="main-container">
        <el-aside style="background-color: #F5F5F5;; margin-top: 10px;">
          <!--          <el-menu :default-openeds="['2', '3']">-->
          <el-menu id="main-menu">
            <el-submenu :index="m.order" v-for="m in menus" v-bind:key="m.order">
              <template slot="title"><i class="el-icon-message"></i>{{m.name}}</template>
              <el-menu-item :index="m.order + '' + c.order" v-for="c in m.childs" v-bind:key="c.order"
                            v-on:click="$router.push(c.router)">
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
  import HttpUtil from "./common/utils/HttpUtil";

  export default {
    name: 'App',
    data() {
      return {
        menus: menu.menus,
        login: localStorage.getItem("login") == "true",
        user: JSON.parse(localStorage.getItem("user"))
      };
    },
    methods: {
      logout() {
        HttpUtil.get("logout", null, () => {
          localStorage.removeItem("login");
          window.location.href = "/";
          window.event.returnValue=false;
        })
      }
    }
  };
</script>

<style>
  body {
    font-size: 10px !important;
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
    width: 220px !important;
  }

  .container {
    padding: 10px;
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
    background: linear-gradient(to right, #000000 20%, #00447C 80%);
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
    cursor: pointer;
  }

  #main-title {
    font-size: 0.96667rem;
    font-weight: 400;
    font-family: Metropolis, Avenir Next, Helvetica Neue, Arial, sans-serif;
    letter-spacing: .01em;
    color: #fafafa;
    text-decoration: none;
    display: block;
    position: fixed;
    left: 1.6rem;
  }

  #main-menu {
    background-color: #F5F5F5;
  }
</style>
