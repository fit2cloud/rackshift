<template>
  <div>
    <span :class="{ green : status == 'on', red : status == 'off'}">
    {{ $t(status) }}
    </span>
    <el-tooltip class="dark" :content="content">
      <i class="el-icon-question" v-if="'unknown' == status"></i>
    </el-tooltip>
  </div>
</template>
<script>
export default {
  props: ['content', 'status']
}
</script>
<style>
.red {
  color: red;
}

.green {
  color: greenyellow;
}
</style>