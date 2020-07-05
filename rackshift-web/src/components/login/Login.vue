<template>
    <el-row :gutter="10">
        <el-col :xs="8" :sm="6" :md="4" :lg="3" :xl="1">
            <div class="grid-content bg-purple"></div>
        </el-col>
        <el-col :xs="4" :sm="6" :md="8" :lg="9" :xl="11">
            <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="100px"
                     class="demo-ruleForm">
                <el-form-item label="用户名" prop="userName">
                    <el-input type="password" v-model="ruleForm.userName" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="密码" prop="password">
                    <el-input type="password" v-model="ruleForm.password" autocomplete="off"></el-input>
                </el-form-item>

                <el-form-item>
                    <el-button type="primary" @click="submitForm('ruleForm')">登录</el-button>
                    <el-button @click="resetForm('ruleForm')">重置</el-button>
                </el-form-item>
            </el-form>
        </el-col>
        <el-col :xs="4" :sm="6" :md="8" :lg="9" :xl="11">
            <div class="grid-content bg-purple"></div>
        </el-col>
        <el-col :xs="8" :sm="6" :md="4" :lg="3" :xl="1">
            <div class="grid-content bg-purple-light"></div>
        </el-col>
    </el-row>

</template>
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
                }
            };
        },
        methods: {
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        HttpUtil.post("/signin", {
                            userName: this.ruleForm.userName,
                            password: this.ruleForm.password
                        }, (res) => {
                            alert(res.id);
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