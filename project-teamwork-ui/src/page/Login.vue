<template>
  <div>
    <el-form ref="loginForm" :model="loginForm" :rules="this.loginRules" label width=" 80px" class="login-box">
      <h3 class="login-title">欢迎登录</h3>
      <el-form-item label="账号" prop="username">
        <el-input type="text" placeholder=" 请输入账号" v-model="loginForm.username"/>
      </el-form-item>
      <el-form-item label="密码" prop="passwd">
        <el-input type="password" placeholder=" 请输入密码" v-model=" loginForm.passwd"/>
      </el-form-item>
      <el-form-item>
        <el-button id="login-bt" type="primary" @click="onSubmit">登录</el-button>
      </el-form-item>
    </el-form>
    <el-dialog
        title="温馨提示"
        :visible.sync="dialogVisible"
        width="30%"
        :before-close="handleClose">
      <span>请输入账号和密码</span>
      <span slot="footer" class="dialog-footer">
      <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
import request from "../util/request";
import {LOGIN_SUCCESS} from "../constants/Common.constants.js";

export default {
  name: "Login",
  data() {
    return {
      loginForm: {
        username: '',
        passwd: ''
      },
// 表单验证规则
      loginRules: {
        username: [{required: true, message: '账号不可为空', trigger: 'blur'}],
        passwd: [{required: true, message: '密码不可为空', trigger: 'blur'}]
      },
//对话框显示和隐藏
      dialogVisible: false
    }
  },
  methods: {
    handleClose: function () {
      console.log("Handle Close，空函数");
    },
    onSubmit() {
      const _this = this
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          request.post('/user/login', this.loginForm).then(async response => {
            console.log(response)
            if (response.data.code === LOGIN_SUCCESS) {
              //后端使用jwt生成token返回到前端，前端存储token
              localStorage.setItem("sysUser", this.loginForm.username)
              localStorage.setItem("token", response.data.data)
              await _this.$router.push({
                path: "/main",
                query: {username: this.loginForm.username}
              });
              this.$message.success(response.data.msg)
            } else {
              console.log(response.data);
              this.$message.error(this.loginForm.username + response.data.msg)
            }
          })
        } else {
          this.dialogVisible = true;
          return false;
        }
      });
    },

  }
}
</script>
<style scoped>
.login-box {
  border: 1px solid #DCDFE6;
  width: 350px;
  margin: 50px auto;
  padding: 35px 35px 15px 35px;
  border-radius: 5px;
  -webkit-border-radius: 5px;
  -moz-border-radius: 5px;
  box-shadow: 0 0 25px #909399;
}

.login-title {
  text-align: center;
  margin: 0 auto 40px auto;
  color: #303133;
}

#login-bt {
  margin: 0 auto;
}
</style>
