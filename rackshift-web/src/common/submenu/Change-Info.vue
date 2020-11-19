<template>
  <el-menu
      id="main-menu"
      :unique-opened=true
      background-color="rgb(55,71,84)"
      text-color="#fff"
      active-text-color="#fff"
      class="main-header-menu"
      mode="horizontal"
  >
    <el-submenu index="1">
      <template slot="title" style="width: 150px;overflow: hidden;">{{ user.name }}</template>
      <el-menu-item v-for="c in languages" @click="clicked(c.value)" :index="1 + '-' + c.index">
        {{ $t(c.name) }}
      </el-menu-item>
    </el-submenu>
  </el-menu>
</template>
<script>
import HttpUtil from "@/common/utils/HttpUtil";

let _ = require('lodash')
export default {
  data() {
    return {
      language: null,
      user: JSON.parse(localStorage.getItem("user")),
      languages: [
        {
          name: 'change_pwd',
          value: 'change_pwd',
          index: '1'
        },
        {
          name: 'help_doc',
          value: 'help_doc',
          index: '2'
        },
        {
          name: 'logout',
          index: '3',
          value: 'logout',
        },
      ],
    }
  },
  mounted() {
  },
  methods: {
    logout() {
      HttpUtil.get("logout", null, () => {
        localStorage.removeItem("login");
        window.location.href = "/";
        window.event.returnValue = false;
      })
    },
    clicked: function (command) {
      if ('logout' == command) {
        this.logout();
      } else if ('change_pwd' == command) {
        this.$router.push("info");
      } else {
        window.open("https://rackshift.github.io/rackshift-docs-static/");
      }
    }
  }
}
</script>