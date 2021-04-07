<template>
  <div id="app">
    <el-container v-if="login">
      <el-header id="main-header">
        <div id="logo-div">
          <img id="logo" src="./assets/rackshift-04.png" alt="">
        </div>
        <div id="right-menu">
          <div class="align-right">
            <HelpDoc></HelpDoc>
            <ChangeLanguage></ChangeLanguage>
            <ChangeInfo @about="about"></ChangeInfo>
            <AboutUs ref="about"></AboutUs>
          </div>
        </div>

      </el-header>
      <el-container id="main-content">
        <el-aside id="main-menu-container">
          <el-menu id="main-menu" :unique-opened=false>
            <el-submenu :index="m.order" v-for="m in menus">
              <template slot="title"><i :class="m.icon"></i>{{ $t(m.name) }}</template>
              <el-menu-item :class="$route.path == c.router ? 'is-active' : ''"
                            v-for="c in m.childs" :index="m.order + '-' + c.order"
                            v-on:click="$router.push(c.router)">
                {{ $t(c.name) }}
              </el-menu-item>
            </el-submenu>
          </el-menu>

          <!--          <div class="rs-divider">-->
          <!--          </div>-->

          <!--          <a href="http://www.github.com/rackshift/rackshift" target="_blank" class="github">-->
          <!--            <div>-->
          <!--              <svg focusable="false" width="16" height="16">-->
          <!--                <path d="M13,14H3c-0.6,0-1-0.4-1-1V3c0-0.6,0.4-1,1-1h5v1H3v10h10V8h1v5C14,13.6,13.6,14,13,14z"></path>-->
          <!--                <path d="M10 1L10 2 13.3 2 9 6.3 9.7 7 14 2.7 14 6 15 6 15 1z"></path>-->
          <!--              </svg>-->
          <!--            </div>-->
          <!--            <span class="text">GitHub</span></a>-->
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
import HelpDoc from './common/submenu/Help-Doc'
import AboutUs from "@/common/about/AboutUs";

export default {
  name: 'App',
  data() {
    return {
      aboutUsVisible: false,
      menus: menu.menus,
      login: localStorage.getItem("login") === "true",
      user: JSON.parse(localStorage.getItem("user")),
      squareUrl: "https://cube.elemecdn.com/9/c2/f0ee8a3c7c9638a54940382568c9dpng.png",
    };
  }
  ,
  components: {
    ChangeLanguage,
    ChangeInfo,
    AboutUs,
    HelpDoc,
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
        sessionStorage.removeItem("rsSocket");
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
  background: white;
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
  border-right: 1px solid #e0e0e0;
  z-index: 1;
}

.container {
  background: white;
}

.user-name {
  display: inline-block;
  margin-right: 10px;
  color: #9E9E9E;
}

#main-header {
  min-width: 1520px;
  height: 40px !important;
  /*background: linear-gradient(to right, rgb(55, 71, 84) 20%, rgb(55, 71, 84) 80%);*/
  background: linear-gradient(to right, #004a71 20%, #004a71 80%);
  color: #333;
  z-index: 2;
  line-height: 40px;
  box-shadow: rgba(0, 0, 0, 0.4) 0px 2px 4px;
}

.main-title {
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
  border-right: none;
}

#main-menu-container {
  background-color: #fff;
  min-height: 780px;
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

#logo-div {
  display: inline-block;
}

#main-content {
  height: calc(100vh - 40px);
}

#right-menu {
  display: flex;
  float: right
}

.rs-divider {
  background-color: #DCDFE6;
  position: relative;
  display: block;
  height: 1px;
  margin: 10px 14px 10px 14px;
}

.github {
  color: #161616;
  display: flex;
  outline: 2px solid transparent;
  outline-offset: -2px;
  position: relative;
  display: flex;
  align-items: center;
  min-height: 2rem;
  padding: 0 1rem;
  text-decoration: none;
  flex-direction: row-reverse;
  justify-content: space-between;
}

.github > svg {
  fill: #525252;
}

a.github:hover {
  background: #e5e5e5;
}

.github .text {
  overflow: hidden;
  padding: 0 10px;
  white-space: nowrap;
  text-overflow: ellipsis;
  color: #525252;
  font-size: .875rem;
  line-height: 1.25rem;
  letter-spacing: .1px;
  -webkit-user-select: none;
  user-select: none;
  font-weight: 550;
}

.center {
  text-align: center;
}

.t100vw {
  width: 100vw;
}

.red {
  font-weight: 500;
  color: red;
}

.green {
  font-weight: 500;
  color: #55BA23;
}

.grey {
  font-weight: 500;
  color: #424342;
}

.input-element {
  width: 100% !important;
}

.drawer-full {
  padding: 20px;
}

.rs-nowrap {
  display: block;
  word-break: keep-all;
  white-space: nowrap;
  overflow: hidden;
}

.detail-info-rack {
  float: left;
  margin-top: 20px;
  border: 1px solid #EBEEF5;
  border-spacing: 0px !important;
  width: 100%;
}

.el-form-item .el-form-item {
  margin-bottom: 18px!important;
}
</style>
