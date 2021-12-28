<template>
  <div>
    <el-tooltip :content="curObj.remark">
      <el-link v-if="curObj.remark" @click="openEdit">{{ formatRemark() }}</el-link>
    </el-tooltip>

    <el-link v-if="curObj.remark=='' || !curObj.remark" type="add" @click="openEdit">{{$t('add')}}</el-link>

    <el-dialog :title="$t('edit') + $t('remark')" :visible.sync="edit" width="35vw" :close-on-click-modal="false">
      <el-form :model="form">
        <el-form-item :label="$t('remark')" :label-width="formLabelWidth">
          <el-input v-model="remark" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <template v-slot:footer>
        <div class="dialog-footer">
          <el-button @click="edit = false">{{ $t('cancel') }}</el-button>
          <el-button type="primary" @click="submit" :loading="loading">{{ $t('confirm') }}</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>
<script>
import HttpUtil from "@/common/utils/HttpUtil";

export default {
  props: ['curObj'],
  data() {
    return {
      remark: null,
      initialRemark: null,
      edit: false,
      loading: false,
      formLabelWidth: "100px",
      form: {},
    }
  },
  methods: {
    openEdit() {
      this.remark = this.curObj.remark;
      this.edit = true;
    },
    submit() {
      this.curObj.remark = this.remark;
      HttpUtil.post("bare-metal/remark", this.curObj, res => {
        if (res.success) {
          this.$message.success(this.$t("opt_success"));
          this.edit = false;
        } else {
          this.$message.error(this.$t("opt_fail"));
        }
      });
    },
    formatRemark() {
      if (this.curObj.remark && this.curObj.remark.length > 15) {
        return this.curObj.remark.substr(0, 15) + "...";
      } else {
        return this.curObj.remark;
      }
    }
  }
}
</script>