<template>
  <el-tooltip class="item" effect="dark" :content="getContent()"
              placement="bottom-end">
    <el-button
        type="button"
        :icon="icon"
        :class="typeClass"
        @click="handleEdit"
        style="cursor: pointer;font-size: 12px;"
        circle
    >
      <template v-slot:default>
      </template>
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
        case "add":
          return "el-button";
        case "edit":
          return "el-button";
        case "del":
          return "el-button--danger";
        case "sync":
          return "el-button--success";
        case "copy":
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
        case "add":
          return "el-icon-plus";
        case "copy":
          return "el-icon-plus";
        default :
          return "";
      }
    }
  },
  methods: {
    handleEdit(row, type) {
      this.$emit('click', row, type);
    },
    getContent() {
      if (this.tip) {
        return this.tip;
      }
      switch (this.type) {
        case "add":
          return this.$t("add");
        case "edit":
          return this.$t("edit");
        case "del":
          return this.$t("del");
        case "sync":
          return this.$t("sync");
        case "view":
          return this.$t("view");
        case "run":
          return this.$t("run");
        case "copy":
          return this.$t("copy");
        default :
          return "";
      }
    }
  }
}
</script>

<style scoped>

</style>