<template>
  <div class="main">
    <div class="logo_title">
      <span class="cyb">CYB</span>
      <span class="TeamWork">-TeamWork</span>
    </div>
    <el-form ref="loginForm" :model="loginForm" :rules="this.loginRules" label width=" 80px" class="login-box">
      <h3 class="login-title">欢迎登录</h3>
      <el-form-item label="账号" prop="username">
        <el-input type="text" placeholder=" 请输入账号" v-model="loginForm.username"/>
      </el-form-item>
      <el-form-item label="密码" prop="passwd">
        <el-input type="password" placeholder=" 请输入密码" v-model=" loginForm.passwd"/>
      </el-form-item>
      <el-form-item>
        <el-button size="large" class="login-bt" type="primary" @click="onSubmit" @keyup.enter="keyDown($event)">登录</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
import {useUserStore} from "@/store/user.js";
import {mapState} from 'pinia'

const userStore = useUserStore();

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
      }
    }
  },
  methods: {
    onSubmit() {
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          userStore.login(this.loginForm)
        } else {
          return false;
        }
      });
    },
    //键盘事件
    keyDown(e) {
      //如果是回车则执行登录方法
      if (e.keyCode === 13) {
        this.onSubmit();
      }
    }
  },
  computed: {
  },
  mounted() {
    //绑定事件
    window.addEventListener('keydown', this.keyDown);
  },
  destroyed() {
    //销毁事件
    window.removeEventListener('keydown', this.keyDown, false);
  }
}
</script>
<style scoped>
.main{
  width: 100vw;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-image: url("src/assets/img/background.jpg");
  background-size: cover;
}
.login-box {
  border: 1px solid #DCDFE6;
  width: 25%;
  height: 32%;
  margin: 50px auto;
  padding: 35px 80px;
  border-radius: 8px;
  -webkit-border-radius: 5px;
  -moz-border-radius: 5px;
  box-shadow: 0 0 25px #909399;
  z-index: 100;
  background-color: rgba(234, 238, 241, 0.9);
}

.login-title {
  text-align: center;
  margin: 0 auto 30px auto;
  color: #303133;
  font-size: 25px;
  font-weight: bolder;
}

.login-bt {
  width: 80px;
  height: 40px;
  padding: 10px;
  margin: 5px auto;
  font-size: 20px;
  font-weight: bolder;
}

/deep/ .el-form-item__label {
  font-size: 19px;
  font-weight: bolder;
}

/deep/ .el-input__inner {
  font-size: 16px !important;
  color: #181818;
  font-weight: bold;
}

.el-form-item {
  margin-bottom: 30px;
}
.logo_title{
  font-size: 195px;
  color: rgb(255, 255, 255);
}
.cyb{
  position: absolute;
  top: 1%;
  left: 15%;
  font-family: 'cyb',sans-serif;
}
.TeamWork{
  position: absolute;
  top: 19%;
  left: 43%;
  font-size: 120px;
  font-family: 'TeamWork',sans-serif;
}
</style>
