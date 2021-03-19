<template>
  <el-menu
      id="main-menu"
      :unique-opened=true
      background-color="#004a71"
      text-color="#fff"
      active-text-color="#fff"
      class="main-header-menu"
      mode="horizontal"
  >
    <el-dialog :title="$t('change_pwd')" :visible.sync="changePwd" :append-to-body="true" width="35vw"
               :before-close="reset"
               :close-on-click-modal="false">
      <el-form :model="editObj" :rules="rules" ref="editForm" label-position="right">
        <el-form-item :label="$t('origin-password')" prop="originPwd" label-width="6vw">
          <el-input v-model="editObj.originPwd" show-password></el-input>
        </el-form-item>
        <el-form-item :label="$t('new-password')" prop="newPwd" label-width="6vw">
          <el-input v-model="editObj.newPwd" show-password></el-input>
        </el-form-item>

        <el-form-item :label="$t('confirm-password')" prop="confirmPwd" label-width="6vw">
          <el-input v-model="editObj.confirmPwd" show-password></el-input>
        </el-form-item>
      </el-form>
      <template v-slot:footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="reset">{{
              $t('cancel')
            }}
          </el-button>
          <el-button type="primary" @click="change">{{ $t('confirm') }}</el-button>
        </div>
      </template>
    </el-dialog>

    <el-submenu index="1">
      <template slot="title" style="width: 150px;overflow: hidden;">{{ user.name }}</template>
      <el-menu-item v-for="c in languages" @click="clicked(c.value)" :index="1 + '-' + c.index">
        {{ $t(c.name) }}
      </el-menu-item>
    </el-submenu>
  </el-menu>

</template>
<script>
import HttpUtil from "@/common/utils/HttpUtil";
import AboutUs from "@/common/about/AboutUs";
import {requiredValidator} from "@/common/validator/CommonValidator";

let _ = require('lodash')
export default {
  components: {AboutUs},
  data() {
    return {
      changePwd: false,
      editObj: {},
      aboutUsVisible: false,
      language: null,
      user: JSON.parse(localStorage.getItem("user")),
      languages: [
        {
          name: 'change_pwd',
          value: 'changePwd',
          index: '1'
        },
        {
          name: 'about',
          value: 'about',
          index: '3'
        },
        {
          name: 'logout',
          index: '4',
          value: 'logout',
        },
      ],
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
    }
  },
  mounted() {
  },
  methods: {
    reset() {
      this.changePwd = false;
      if (this.$refs.editForm) {
        this.$refs.editForm.resetFields();
      }
    },
    logout() {
      HttpUtil.get("logout", null, () => {
        localStorage.removeItem("login");
        sessionStorage.removeItem("rsSocket");
        window.location.href = "/";
        window.event.returnValue = false;
      })
    },
    clicked: function (command) {
      if ('logout' == command) {
        this.logout();
      } else if ('changePwd' == command) {
        this.changePwd = true;
      } else if ('about' == command) {
        this.aboutUsVisible = true;
        this.$emit('about');
      } else {
        window.open("https://docs.rackshift.io/");
      }
    },
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
          this.changePwd = false;
          this.logout();
        } else {
          this.$message.error(this.$t('opt_fail'));
        }
      });
    },
  }
}
</script>