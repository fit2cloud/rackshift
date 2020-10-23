<template>
  <div class="container">

    <div class="machine-title">
    </div>

    <el-row class="info-body">
      <el-col :span="8">
      </el-col>
      <el-col :span="8">
        <div><h1>{{ $t('change_pwd') }}</h1></div>
        <el-form :model="editObj" :rules="rules" ref="editForm">
          <el-form-item :label="$t('origin-password')" prop="originPwd">
            <el-input v-model="editObj.originPwd" show-password></el-input>
          </el-form-item>
          <el-form-item :label="$t('new-password')" prop="newPwd">
            <el-input v-model="editObj.newPwd" show-password></el-input>
          </el-form-item>

          <el-form-item :label="$t('confirm-password')" prop="confirmPwd">
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
import {requiredValidator} from "@/common/validator/CommonValidator";


let _ = require('lodash');
export default {
  data() {
    return {
      editObj: {},
      rules: {
        originPwd: [
          {validator: requiredValidator, trigger: 'blur', vue: this},
        ],
        newPwd: [
          {validator: requiredValidator, trigger: 'blur', vue: this},
        ],
        confirmPwd: [
          {validator: requiredValidator, trigger: 'blur', vue: this},
        ]
      },
    };
  },
  mounted() {
  },
  methods: {
    change() {
      this.validateResult = true;
      this.$refs.editForm.validate(f => {
        if (!f) {
          this.validateResult = false;
        }
      });
      if (!this.validateResult) return;
      HttpUtil.post("/user/change", this.editObj, (res) => {
        if (res.data) {
          this.$message.success(this.$t('opt_success'));
          this.editObj = {};
        } else {
          this.$message.error(this.$t('opt_fail'));
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

.info-body {
  margin-left: 10px;
}

</style>