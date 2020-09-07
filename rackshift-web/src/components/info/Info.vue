<template>
  <div class="container">

    <div class="machine-title">
      <i class="el-icon-user-solid">{{ $t('Info') }}</i>
    </div>

    <el-row>
      <el-col :span="8">
      </el-col>
      <el-col :span="8">
        <div><h1>{{ $t('change_pwd') }}</h1></div>
        <el-form :model="editObj">
          <el-form-item label="origin-password">
            <el-input v-model="editObj.originPwd" show-password></el-input>
          </el-form-item>
          <el-form-item label="new-password">
            <el-input v-model="editObj.newPwd" show-password></el-input>
          </el-form-item>

          <el-form-item label="confirm-password">
            <el-input v-model="editObj.confirmPwd" show-password></el-input>
          </el-form-item>

          <el-row>
            <el-button type="primary" @click="reset">{{ $t('reset') }}</el-button>
            <el-button type="primary" @click="change">{{ $t('confirm') }}</el-button>
          </el-row>
        </el-form>

      </el-col>
      <el-col :span="8">
      </el-col>
    </el-row>
  </div>
</template>

<script>

import HttpUtil from "../../common/utils/HttpUtil"
import Vue from "vue"


let _ = require('lodash');
export default {
  data() {
    return {
      editObj: {},
    };
  },
  mounted() {
  },
  methods: {
    change() {
      HttpUtil.post("/user/change", this.editObj, (res) => {
        if (res.data) {
          this.$notify.success(this.$t('edit_success'));
          this.editObj = {};
        } else {
          this.$notify.success(this.$t('edit_fail'));
        }
      });
    },
    reset() {
      this.editObj = {};
    }
    ,
  }
}
</script>

<style scoped>
.handle-box {
  margin-bottom: 20px;
}

.table {
  width: 100%;
  font-size: 14px;
}

.mr10 {
  margin-right: 10px;
}

</style>