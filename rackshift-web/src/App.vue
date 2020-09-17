<template>
  <div id="app">
    <el-container style=" border: 1px solid #eee" v-if="login">
      <el-header id="main-header">
        <el-row>
          <el-col :span="21"><span id="main-title">RackShift</span></el-col>
          <el-col :span="3">
            <el-dropdown id="dropdown" @command="changeLaunguage">
          <span class="el-dropdown-link">
              {{ $t('Languages') }}<i class="el-icon-arrow-down el-icon--right"></i>
          </span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="zh_CN">{{ $t('chinese') }}</el-dropdown-item>
                <el-dropdown-item command="zh_TW">{{ $t('fanti') }}</el-dropdown-item>
                <el-dropdown-item command="en_US">{{ $t('english') }}</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
            <span class="user-name">{{ user.name }}</span>
            <el-dropdown id="dropdown" @command="action">
              <i class="el-icon-setting" style="margin-right: 15px;cursor: pointer;"></i>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="info">{{ $t('info') }}</el-dropdown-item>
                <el-dropdown-item command="logout">{{ $t('logout') }}</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </el-col>
        </el-row>

      </el-header>
      <el-container>
        <el-aside style="background-color: #F7F7F7">
          <el-menu id="main-menu" :unique-opened=false style="border-right: none;">
            <el-submenu :index="m.order" v-for="m in menus">
              <template slot="title"><i :class="m.icon"></i>{{ m.name }}</template>
              <el-menu-item :class="$route.path == c.router ? 'is-active' : ''"
                            v-for="c in m.childs" :index="m.order + '-' + c.order"
                            v-on:click="$router.push(c.router)">
                <i :class="c.icon"></i>
                {{ c.name }}
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
  mounted() {
    if (localStorage.getItem('first') == 'true') {
      this.$router.push("/bare-metal");
      localStorage.removeItem('first');
    }
  },
  methods: {
    logout() {
      HttpUtil.get("logout", null, () => {
        localStorage.removeItem("login");
        window.location.href = "/";
        window.event.returnValue = false;
      })
    },
    changeLaunguage(l) {
      this.$setLang(l);
      window.location.reload();
    },
    action(command) {
      if ('logout' == command) {
        this.logout();
      } else {
        this.$router.push("info");
      }
    },
  }
};
</script>

<style>
* {
  font-size: 13px !important;
}

body {
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
  border-right: none;
  z-index: 1;
}

.container {
  /*padding: 10px;*/
  background: #fff;
  /*border: 1px solid #ddd;*/
  /*border-left: none;*/
  /*border-radius: 5px;*/
  width: 100%;
  height: calc(100vh - 75px);
}

.user-name {
  display: inline-block;
  margin-right: 10px;
  color: #9E9E9E;
}

#main-header {
  font-size: 12px;
  height: 45px !important;
  background: linear-gradient(to right, #111, #111 80%, #111);
  /*background: linear-gradient(to right, #00447C ,#409EFF 80%, #00447C );*/
  /*background: linear-gradient(to right, #000000 20%, #00447C 80%);*/
  color: #333;
  z-index: 2;
  line-height: 45px;
  /*box-shadow: 0 1px 5px 0 rgba(0, 0, 0, .2), 0 2px 2px 0 rgba(0, 0, 0, .14), 0 3px 1px -2px rgba(0, 0, 0, .12);*/
  box-shadow: rgba(0, 0, 0, 0.4) 0px 2px 4px;
}

#dropdown {
  color: #9E9E9E;
  cursor: pointer;
  margin-right: 12px;
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
  padding: 10px;
}

.batch-button {
  margin-left: 15px;
}

.demo-drawer__content {
  padding: 20px;
}

.pagination {
  margin-top: 10px;
}

.el-drawer__body {
  overflow: scroll;
}

.el-button + .el-button {
  margin-left: 0px !important;
}

.demo-drawer__footer {
  display: flex;
}

.demo-drawer__footer button {
  flex: 1;
}
</style>
