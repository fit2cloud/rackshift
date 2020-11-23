<template>
  <div id="app">
    <el-container v-if="login">
      <el-header id="main-header">
        <div style="display: inline-block;">
          <img id="logo" src="./assets/rackshift-04.png" alt="">
        </div>
        <div style="display: flex; float: right">
          <div class="align-right">
            <ChangeLanguage></ChangeLanguage>
            <ChangeInfo @about="about"></ChangeInfo>
            <AboutUs ref="about"></AboutUs>
          </div>
        </div>

      </el-header>
      <el-container>
        <el-aside style="background-color: #F7F7F7">
          <el-menu id="main-menu" :unique-opened=false style="border-right: none;">
            <el-submenu :index="m.order" v-for="m in menus">
              <template slot="title"><i :class="m.icon"></i>{{ $t(m.name) }}</template>
              <el-menu-item :class="$route.path == c.router ? 'is-active' : ''"
                            v-for="c in m.childs" :index="m.order + '-' + c.order"
                            v-on:click="$router.push(c.router)">
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
import AboutUs from "@/common/about/AboutUs";

export default {
  name: 'App',
  data() {
    return {
      aboutUsVisible: false,
      menus: menu.menus,
      login: localStorage.getItem("login") == "true",
      user: JSON.parse(localStorage.getItem("user")),
      squareUrl: "https://cube.elemecdn.com/9/c2/f0ee8a3c7c9638a54940382568c9dpng.png",
    };
  }
  ,
  components: {
    ChangeLanguage,
    ChangeInfo,
    AboutUs
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
    about() {
      this.$refs.about.open();
    },
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
  /*font-size: 14px !important;*/
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

}

.user-name {
  display: inline-block;
  margin-right: 10px;
  color: #9E9E9E;
}

#main-header {
  min-width: 1520px;
  height: 40px !important;
  background: linear-gradient(to right, rgb(55, 71, 84) 20%, rgb(55, 71, 84) 80%);
  color: #333;
  z-index: 2;
  line-height: 40px;
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
  left: 0.6rem;
  position: fixed;
  width: 148px;
  height: 37px;
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
  margin: 20px 0;
}

.el-tabs__nav {
  margin-left: 10px;
}

.el-drawer__body {
  overflow-y: scroll;
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
  height: 40px !important;
  line-height: 40px !important;
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


.el-submenu__title {
  border-bottom-color: transparent !important;
}

.pagination {
  float: right;
  padding: 2px 15px !important;
}

.current-user.username {
  display: inline-block;
  font-size: 16px;
  font-weight: 500;
  margin: 0 5px;
  overflow-x: hidden;
  padding-bottom: 0;
  text-overflow: ellipsis;
  vertical-align: middle;
  white-space: nowrap;
  width: 180px;
}

.current-user .edit {
  opacity: 0;
}

.current-user:hover .edit {
  opacity: 1;
}

.pointer {
  cursor: pointer;
}

#logo {
  width: 148px;
  height: 37px;
}
</style>
