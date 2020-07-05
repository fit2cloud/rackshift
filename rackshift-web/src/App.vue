<template>
  <div id="app">
    <router-view></router-view>
  </div>
</template>

<script>

  export default {
    name: 'App'
  }
</script>

<style>
  body {
    font-family: -apple-system, BlinkMacSystemFont, "Neue Haas Grotesk Text Pro", "Arial Nova", "Segoe UI", "Helvetica Neue", ".PingFang SC", "PingFang SC", "Source Han Sans SC", "Noto Sans CJK SC", "Source Han Sans CN", "Noto Sans SC", "Source Han Sans TC", "Noto Sans CJK TC", "Hiragino Sans GB", sans-serif;
    font-size: 14px;
    background-color: #F5F5F5;
    line-height: 26px;
    color: #2B415C;
    -webkit-font-smoothing: antialiased;
    margin: 0;
  }

  #app {
    width: 100%;
    height: 100%;
    position: absolute;
  }

  .form .el-input > .el-input__inner {
    border-radius: 0;
  }
</style>
