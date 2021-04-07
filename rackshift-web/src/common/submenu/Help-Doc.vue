<template>
  <ul role="menubar" class="main-header-menu el-menu--horizontal el-menu"
      style="background-color: rgb(0, 74, 113);" @mouseenter="setColor(true)" @mouseout="setColor(false)">
    <li role="menuitem" aria-haspopup="true" class="el-submenu" tabindex="0">
      <div class="el-submenu__title" @click="helpDoc" id="help-doc"
           style="border-bottom-color: transparent; color: rgb(255, 255, 255); background-color: rgb(0, 74, 113);">
        {{ $t("help_doc") }}
      </div>
    </li>
  </ul>

</template>
<script>
import AboutUs from "@/common/about/AboutUs";

let _ = require('lodash')
export default {
  components: {AboutUs},
  data() {
    return {}
  },
  mounted() {
  },
  methods: {
    setColor(e) {
      if (e) {
        document.querySelector("#help-doc").setAttribute("style", "background-color:rgb(0, 59, 90);color:white;");
      } else {
        document.querySelector("#help-doc").setAttribute("style", "background-color:rgb(0, 74, 113);color:white;");
      }
    },
    helpDoc() {
      window.open("https://docs.rackshift.io/");
    }
  }
}
</script>