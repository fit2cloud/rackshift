<template>
  <div>
    <el-input type="textarea" :rows="row" v-model="inputValue" @change="emitValue"
              :placeholder="placeholder"></el-input>
    <el-link href="https://docs.rackshift.io/post-install/" target="_blank">{{ $t('view_example') }}</el-link>
  </div>
</template>

<script>
export default {
  name: "RSCodeMirror",
  props: {
    value: Array
  },
  data() {
    return {
      inputValue: "",
      arrValue: [],
      row: 9,
      placeholder: "shell script"
    }
  },
  methods: {
    emitValue: function (val) {
      this.arrValue = val.split("\n");
      this.$emit("receiveValue", this.arrValue);
    }
  },
  watch: {
    value: {
      handler: function (newVal) {
        if (newVal)
          this.inputValue = newVal.join('\n')
      },
      immediate: true,
    }
  }
}

</script>

<style scoped>

</style>