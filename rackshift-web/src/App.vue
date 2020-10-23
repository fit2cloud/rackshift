<template>
  <div id="app">
    <el-container v-if="login">
      <el-header id="main-header">
        <el-row type="flex" style="display: flex;">
          <el-col :span="20"><span id="main-title">RackShift</span></el-col>
          <el-col :span="4" class=" align-right          ">
            <ChangeLanguage></ChangeLanguage>
            <ChangeInfo></ChangeInfo>
          </el-col>
        </el-row>

      </el-header>
      <el-container>
        <el-aside style="background-color: #F7F7F7">
          <el-menu id="main-menu" :unique-opened=false style="border-right: none;">
            <el-submenu :index="m.order" v-for="m in menus">
              <template slot="title"><i :class="m.icon"></i>{{ $t(m.name) }}</template>
              <el-menu-item :class="$route.path == c.router ? 'is-active' : ''"
                            v-for="c in m.childs" :index="m.order + '-' + c.order"
                            v-on:click="$router.push(c.router)">
                <i :class="c.icon"></i>
                {{ $t(c.name) }}
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
import ChangeLanguage from './common/submenu/Change-Language'
import ChangeInfo from './common/submenu/Change-Info'

export default {
  name: 'App',
  data() {
    return {
      menus: menu.menus,
      login: localStorage.getItem("login") == "true",
      user: JSON.parse(localStorage.getItem("user")),
    };
  }
  ,
  components: {
    ChangeLanguage,
    ChangeInfo
  }
  ,
  mounted() {
    if (localStorage.getItem('first') == 'true') {
      this.$router.push("/bare-metal");
      localStorage.removeItem('first');
    }
    let l = localStorage.getItem("lang") ? localStorage.getItem("lang") : 'zh_CN';
    this.changeLaunguage(l);
  }
  ,
  methods: {
    logout() {
      HttpUtil.get("logout", null, () => {
        localStorage.removeItem("login");
        window.location.href = "/";
        window.event.returnValue = false;
      })
    }
    ,
    changeLaunguage(l) {
      this.$setLang(l);
    }
    ,
    action(command) {
      if ('logout' == command) {
        this.logout();
      } else {
        this.$router.push("info");
      }
    }
    ,
  }
}
;
</script>

<style>
* {
  font-size: 14px !important;
}

body {
  color: #2B415C;
  -webkit-font-smoothing: antialiased;
  margin: 0;
  padding: 0;
  font-family: Helvetica Neue, Helvetica, PingFang SC, Hiragino Sans GB, Arial, sans-serif;
}

.form .el-input > .el-input__inner {
  border-radius: 0;
}

.el-aside {
  color: #333;
  width: 220px !important;
  border-right: none;
  z-index: 1;
}

.container {
  background: #fff;
  width: 100%;
}

.user-name {
  display: inline-block;
  margin-right: 10px;
  color: #9E9E9E;
}

#main-header {
  font-size: 12px;
  height: 45px !important;
  background: linear-gradient(to right, #00447C 20%, #00447C 80%);
  color: #333;
  z-index: 2;
  line-height: 45px;
  box-shadow: rgba(0, 0, 0, 0.4) 0px 2px 4px;
}

#main-title {
  font-size: 1.3rem !important;
  font-weight: 400;
  font-family: Metropolis, Avenir Next, Helvetica Neue, Arial, sans-serif;
  letter-spacing: .01em;
  color: #fafafa;
  text-decoration: none;
  display: block;
  left: 1.6rem;
}

#main-menu {
  background-color: #F7F7F7;
}

.machine-title {
  padding: 0px 0 10px 0px;
}

.machine-title2 {
  padding: 0 0 10px 0;
}

.batch-button {
  margin-left: 10px;
}

.demo-drawer__content {
  padding: 20px;
}

.pagination {
  margin-top: 10px;
}

.el-tabs__nav {
  margin-left: 10px;
}

.el-drawer__body {
  overflow: scroll;
}

.demo-drawer__footer {
  display: flex;
}

.demo-drawer__footer button {
  flex: 1;
}

#dropdown {
  background-color: #2B415C;
  padding: 0 10px 0 10px;
}

#dropdown2 {
  background-color: #2B415C;
  padding-left: 5px;
}

.align-right {
  float: right;
}

.main-header-menu {
  padding: 0;
  margin: 0;
  display: inline-block;
}

.el-menu--horizontal > .el-submenu .el-submenu__title {
  height: 44px !important;
  line-height: 44px !important;
}

.el-menu.el-menu--horizontal {
  border: none !important;
}

button {
  cursor: pointer;
}

.drawer-header {
  padding-bottom: 30px;
}

:focus {
  outline: 0;
}

</style>
