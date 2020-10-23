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
      <template slot="title">
        <font-awesome-icon class="icon global" :icon="['fas', 'globe']"/>
        {{ $t(language) }}
      </template>
      <el-menu-item v-for="c in languages" @click="clicked(c)" :index="1 + '-' + c.index">
        {{ $t(c.name) }}
      </el-menu-item>
    </el-submenu>
  </el-menu>
</template>
<script>
let _ = require('lodash')
export default {
  data() {
    return {
      language: null,
      languages: [
        {
          name: 'zh_CN',
          value: 'zh_CN',
          index: '1'
        },
        {
          name: 'zh_TW',
          index: '2',
          value: 'zh_TW',
        },
        {
          name: 'en_US',
          index: '3',
          value: 'en_US',
        }
      ],
    }
  },
  mounted() {
    this.changeLanText();
  },
  methods: {
    changeLanText() {
      let l = localStorage.getItem("lang") ? localStorage.getItem("lang") : 'zh_CN';
      let index = _.findIndex(this.languages, function (o) {
        return o.value == l;
      });
      this.language = this.languages[index].name;
    },
    changeLaunguage(l) {
      this.$setLang(l);
      this.changeLanText();
    },
    clicked: function (c) {
      this.changeLaunguage(c.value);
    }
  }
}
</script>