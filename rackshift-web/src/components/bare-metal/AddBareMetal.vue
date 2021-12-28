<template>
  <el-dialog :title="$t('add')" :visible.sync="addBareMetal" width="35vw" :close-on-click-modal="false">
    <el-form :model="form">
      <el-link>
        <el-tooltip class="dark" :content="$t('pxe_desc')">
          <i class="el-icon-question">{{ $t('why') }}</i>
        </el-tooltip>
      </el-link>
      <el-form-item :label="$t('pxe_mac')" :label-width="formLabelWidth">
        <el-input v-model="curObm.pxeMac" autocomplete="off"></el-input>
      </el-form-item>
    </el-form>
    <template v-slot:footer>
      <div class="dialog-footer">
        <el-button @click="addBareMetal = false">{{ $t('cancel') }}</el-button>
        <el-button type="primary" @click="submitAdd" :loading="loading">{{ $t('confirm') }}</el-button>
      </div>
    </template>
  </el-dialog>

</template>
<script>
import HttpUtil from "@/common/utils/HttpUtil";

export default {
  data() {
    return {
      addBareMetal: false,
      loading: false,
      curObm: {},
      form: {},
      formLabelWidth: "100px"
    }
  },
  methods: {
    submitAdd() {
      HttpUtil.post("/bare-metal/add", this.curObm, res => {
        if (res.success) {
          this.$message.success(this.$t("opt_success"));
          this.curObm = {};
          this.close();
          this.$emit("confirm");
        } else {
          this.$message.error(res.message);
        }
      });
    },
    open() {
      this.addBareMetal = true;
    },
    close() {
      this.addBareMetal = false;
    },
  }

}
</script>
<style>

</style>