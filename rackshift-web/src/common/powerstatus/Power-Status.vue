<template>
  <div>
    <span :class="{ green : status == 'on', red : status == 'off', grey : status == 'unknown'}">
      <el-tooltip class="dark" :content="$t('refresh')">
        <el-link @click="refreshPower">{{ $t(status) }}</el-link>
      </el-tooltip>
    </span>
    <el-tooltip class="dark" :content="content">
      <i class="el-icon-question" v-if="'unknown' == status"></i>
    </el-tooltip>
  </div>
</template>
<script>
import HttpUtil from "../../common/utils/HttpUtil";

export default {
  props: ['content', 'status', 'bareMetalId', 'callback'],
  methods: {
    refreshPower() {
      HttpUtil.get("/bare-metal/refreshPower/" + this.bareMetalId + "/", {}, res => {
        if (this.callback) {
          this.callback();
        }
      }, res => {
        this.$message.error(res.message);
      });
    }
  }
}
</script>
<style>

</style>