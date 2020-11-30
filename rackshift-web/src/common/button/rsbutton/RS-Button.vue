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
    typeClass: function () {
      if (this.type == "edit") {
        return "el-button";
      } else if (this.type == "del") {
        return "el-button--danger";
      } else if (this.type == "sync") {
        return "el-button--success";
      } else {
        return "el-button";
      }
    },
    icon: function () {
      if (this.type == "edit") {
        return "el-icon-edit";
      } else if (this.type == "del") {
        return "el-icon-delete";
      } else if (this.type == "add") {
        return "el-icon-plus";
      } else if (this.type == "sync") {
        return "el-icon-refresh";
      } else if (this.type == "view") {
        return "el-icon-view";
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