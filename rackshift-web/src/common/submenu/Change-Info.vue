<template>
  <el-menu
      id="main-menu"
      :unique-opened=true
      background-color="#111111"
      text-color="#fff"
      active-text-color="#fff"
      class="main-header-menu"
      mode="horizontal"
      style="cursor:pointer;"
  >
    <el-submenu index="1">
      <template slot="title">{{ user.name }}</template>
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
          name: 'info',
          value: 'info',
          index: '1'
        },
        {
          name: 'logout',
          index: '2',
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
      } else {
        this.$router.push("info");
      }
    }
  }
}
</script>