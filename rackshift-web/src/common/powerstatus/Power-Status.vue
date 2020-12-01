<template>
  <div>
    <span :class="{ green : status == 'on', red : status == 'off', grey : status == 'unknown'}">
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
</style>