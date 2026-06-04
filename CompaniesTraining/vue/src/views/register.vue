<template>
  <div class="bg">
    <div style="width: 350px; background-color: #fff; border-radius: 5px; padding: 20px; box-shadow: 40px 20px rgba(0,0,0,0.1)">
      <el-form ref="formRef" :model="data.form" :rules="data.rules">
        <div style="margin: 20px 0; text-align: center; font-weight: bold; font-size: 30px;">企业培训系统</div>
        <div style="margin: 20px 0; text-align: center;  font-size: 24px;">注 册</div>
        <el-form-item prop="username">
          <el-input size="large" v-model="data.form.username" autocomplete="off"  prefix-icon="User" placeholder="请输入账号"/>
        </el-form-item>
        <el-form-item prop="password">
          <el-input size="large" show-password="true" v-model="data.form.password" autocomplete="off"  prefix-icon="Lock" placeholder="请输入密码"/>
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input size="large" show-password="true" v-model="data.form.confirmPassword" autocomplete="off"  prefix-icon="Lock" placeholder="请输入密码"/>
        </el-form-item>
        <div>
          <el-button style="width: 100%" size="large" type="primary" @click="register">注 册</el-button>
        </div>
        <div style="text-align: right; padding-top: 10px">
          已有账号? 请返回<a style="color: #5e2bf6" href="/login">登录</a>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { reactive,ref } from "vue";
import {ElMessage} from "element-plus";
import request from "@/utils/request.js";
import router from "@/router/index.js";

const formRef = ref()

const validatePass = (rule, value, callback) => {
  // value 表示用户输入的确认密码
  if (value !== data.form.password) {
    callback(new Error("两次输入的密码不匹配！"))
  } else {
    callback()
  }
}

const data = reactive({
  form: {},
  rules: {
    username: [
      { required: true,message: '请输入账号', trigger: 'blur' }
    ],
    password: [
      { required: true,message: '请输入密码', trigger: 'blur' }
    ],
    confirmPassword: [
      { required: true,message: '请再次输入密码', trigger: 'blur' },
      {validator: validatePass}
    ]
  }
})

const register = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      request.post('/register',data.form).then(res => {
        if (res.code === '200') {
          // 存储用户信息
          ElMessage.success("注册成功")
          router.push('/login')
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })

}
</script>

<style scoped>
.bg {
  width: 100%;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: hidden;
  background-image: url("@/assets/imgs/bg1.jpg");
  background-size: cover;
}

</style>