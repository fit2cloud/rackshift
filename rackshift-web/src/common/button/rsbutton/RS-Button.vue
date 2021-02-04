<template>
  <el-tooltip class="item" effect="dark" :content="tip ? tip : type == 'del' ? $t('delete') : $t('edit')"
              placement="bottom-end">
    <el-button
        type="button"
        :icon="icon"
        :class="typeClass"
        @click="handleEdit"
        style="cursor: pointer;font-size: 12px;"
        circle
    >
    </el-button>
  </el-tooltip>
</template>

<script>
export default {
  name: "RSButton",
  props: {
    type: {
      type: String,
      default() {
        return "edit";
      }
    },
    tip: {
      type: String
    },
  },
  computed: {
    typeClass() {
      switch (this.type) {
        case "edit":
          return "el-button";
        case "del":
          return "el-button--danger";
        case "sync":
          return "el-button--success";
        default :
          return "el-button";
      }
    },
    icon() {
      switch (this.type) {
        case "edit":
          return "el-icon-edit";
        case "del":
          return "el-icon-delete";
        case "sync":
          return "el-icon-refresh";
        case "view":
          return "el-icon-view";
        case "run":
          return "el-icon-caret-right";
        default :
          return "";
      }
    }
  },
  methods: {
    handleEdit(row, type) {
      this.$emit('click', row, type);
    }
  }
}
</script>

<style scoped>

</style>