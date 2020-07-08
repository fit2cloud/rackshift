<template>
    <div class="login-container">
        <el-row type="flex">

            <el-col :span="12">

                <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm"
                         class="form">
                    <div class="logo">
                        <!--                        <img src="../../assets/logo-dark-MeterSphere.svg" style="width: 224px" alt="">-->
                    </div>
                    <div class="title">
                        <span id="s2">Rackshift</span>
                    </div>
                    <div class="border"></div>
                    <div class="welcome">
                    </div>
                    <el-form-item label="用户名" prop="userName">
                        <el-input type="text" v-model="ruleForm.userName" autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="密码" prop="password">
                        <el-input type="password" v-model="ruleForm.password" autocomplete="off"></el-input>
                    </el-form-item>

                    <div class="btn">
                        <el-button type="primary" class="submit" @click="submitForm('ruleForm')">登录</el-button>
                        <!--                        <el-button @click="resetForm('ruleForm')">重置</el-button>-->
                    </div>
                    <div class="msg">
                        {{msg}}
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
    }

    .login-container {
        min-width: 800px;
        max-width: 1440px;
        height: 560px;
        margin: calc((100vh - 560px) / 2) auto 0;
        background-color: #FFFFFF;
    }

    .image {
        background: url(../../assets/info.png);
        height: 560px;
    }

    .btn > .submit {
        width: 100%;
        border-radius: 0;
        /*border-color: #8B479B;*/
        /*background-color: #8B479B;*/
    }

    .border {
        height: 2px;
        margin: 20px auto 20px;
        position: relative;
        width: 80px;
        background: #409EFF;
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
        margin-top: 60px;
        padding: 0 40px;
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
                        callback(new Error('请输入用户名'));
                    }
                    if (rule.field == "password") {
                        callback(new Error('请输入密码'));
                    }
                }
                callback();
            };
            return {
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
                        }, () => {
                            localStorage.setItem('login', true);
                            window.location.href = "/";
                        }, (msg) => {
                            this.msg = msg;
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