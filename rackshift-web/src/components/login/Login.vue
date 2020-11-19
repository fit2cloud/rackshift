<template>
  <div class="login-container">
    <el-row type="flex">

      <el-col :span="12">

        <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm">
          <div class="logo">
            <img src="../../assets/rackshift-02.png" style="width: 224px" alt="">
          </div>
          <div class="title">
            <span id="s1">{{ $t('Login') }}</span>
            <span id="s2">
              RackShift
            </span>
          </div>
          <div class="border"></div>
          <div class="welcome">
            {{ $t('welcome') }}
          </div>
          <div class="form">
            <el-form-item v-slot:default>
              <el-radio-group v-model="ldap">
                <el-radio label="LDAP" size="mini" v-if="ldap">LDAP</el-radio>
                <el-radio label="LOCAL" size="mini" v-if="ldap">普通登录</el-radio>
              </el-radio-group>
            </el-form-item>

            <el-form-item prop="userName">
              <el-input type="text" v-model="ruleForm.userName" autocomplete="off"
                        :placeholder="$t('username_or_id')"></el-input>
            </el-form-item>
            <el-form-item prop="password">
              <el-input type="password" v-model="ruleForm.password" autocomplete="off" show-password
                        :placeholder="$t('password')"></el-input>
            </el-form-item>
          </div>
          <div class="btn">
            <el-button type="primary" class="submit" @click="submitForm('ruleForm')">{{ $t('Login') }}</el-button>
          </div>
          <div class="msg">
            {{ msg }}
          </div>

        </el-form>


      </el-col>

      <el-col :span="12" class="image">
        <div class="grid-content bg-purple-light"></div>
      </el-col>
    </el-row>
  </div>
</template>

<style>
.msg {
  margin-top: 10px;
  padding: 0 40px;
  color: red;
  text-align: center;
}

.title {
  margin-top: 50px;
  font-size: 32px;
  letter-spacing: 0;
  text-align: center;
}

.title > #s1 {
  color: #999999;
}

.title > #s2 {
  color: #151515;
  font-size: 32px !important;
}

.login-container {
  min-width: 800px;
  max-width: 1440px;
  height: 560px;
  margin: calc((100vh - 560px) / 2) auto 0;
  background-color: #FFFFFF;
}

.image {
  background: url(../../assets/info.jpeg);
  height: 560px;
}

.btn > .submit {
  width: 100%;
  border-radius: 0;
}

.border {
  height: 2px;
  margin: 20px auto 20px;
  position: relative;
  width: 80px;
  background: #E95420;
}

.welcome {
  margin-top: 50px;
  font-size: 14px;
  color: #999999;
  letter-spacing: 0;
  line-height: 18px;
  text-align: center;
}

.form {
  margin-top: 30px;
  padding: 0 40px;
}


.btn {
  margin-top: 40px;
  padding: 0 40px;
}

.btn > .submit {
  width: 100%;
  border-radius: 0;
  border-color: #E95420;
  background-color: #E95420;
}

</style>

<script>
import HttpUtil from '../../common/utils/HttpUtil'

export default {
  name: "Login",
  data() {
    let validateUser = (rule, value, callback) => {
      if (value === '') {
        if (rule.field == "userName") {
          callback(new Error(this.$t('pls_input_user_name')));
        }
        if (rule.field == "password") {
          callback(new Error(this.$t('pls_input_pwd')));
        }
      }
      callback();
    };
    return {
      ldap: null,
      ruleForm: {
        userName: '',
        password: '',
      },
      rules: {
        userName: [
          {validator: validateUser, trigger: 'blur'}
        ],
        password: [
          {validator: validateUser, trigger: 'blur'}
        ]
      },
      msg: null
    };
  },
  beforeCreate() {
    if (localStorage.getItem("login") == "true") {
      this.$router.push("index");
    }
  },
  created: function () {
    // 主页添加键盘事件,注意,不能直接在焦点事件上添加回车
    document.addEventListener("keydown", this.watchEnter);
  },
  destroyed() {
    //移除监听回车按键
    document.removeEventListener("keydown", this.watchEnter);
  },
  methods: {
    watchEnter(e) {
      if (e.which == 13) {
        this.submitForm('ruleForm');
      }
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          HttpUtil.post("/signin", {
            userName: this.ruleForm.userName,
            password: this.ruleForm.password
          }, (res) => {
            localStorage.setItem('login', true);
            localStorage.setItem('user', JSON.stringify(res.data));
            localStorage.setItem('first', true);
            window.location.href = "/";
          }, (msg) => {
            if (msg && msg.indexOf('Authentication failed for token submission') != -1) {
              this.$message.error(this.$t('username_pwd_error_login_fail'));
            } else {
              this.$message.error(this.$t('login_fail'));
            }
          })
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    }
  }
}
</script>