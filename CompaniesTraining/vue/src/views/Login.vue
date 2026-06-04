<template>
  <div class="bg">
    <div style="width: 350px; background-color: #fff; border-radius: 5px; padding: 20px; box-shadow: 40px 20px rgba(0,0,0,0.1)">
      <el-form ref="formRef" :model="data.form" :rules="data.rules">
        <div style="margin: 20px 0; text-align: center; font-weight: bold; font-size: 30px;">企业培训系统</div>
        <div style="margin: 20px 0; text-align: center; font-size: 24px;">欢 迎 登 录</div>
        <el-form-item prop="username">
          <el-input size="large" v-model="data.form.username" autocomplete="off"  prefix-icon="User" placeholder="请输入账号"/>
        </el-form-item>
        <el-form-item prop="password">
          <el-input size="large" show-password="true" v-model="data.form.password" autocomplete="off"  prefix-icon="Lock" placeholder="请输入密码"/>
        </el-form-item>
        <el-form-item prop="role">
          <el-select size="large" style="width: 100%" v-model="data.form.role">
            <el-option label="管理员" value=1></el-option>
            <el-option label="企业导师" value=2></el-option>
            <el-option label="企业员工" value=3></el-option>
          </el-select>
        </el-form-item>
        <div>
          <el-button style="width: 100%" size="large" type="primary" @click="addLogin">登 录</el-button>
        </div>
        <div style="text-align: right; padding-top: 10px">
          还没有账号? 请<a style="color: #5e2bf6" href="/register">注册</a>
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
const data = reactive({
  form: {
    role: '1',
  },
  rules: {
    username: [
      { required: true,message: '请输入账号', trigger: 'blur' }
    ],
    password: [
      { required: true,message: '请输入密码', trigger: 'blur' }
    ],
  }
 })

const addLogin = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      request.post('/login',data.form).then(res => {
        if (res.code === '200') {
          // 存储用户信息
          localStorage.setItem("code_user",JSON.stringify(res.data || {}))
          ElMessage.success("登录成功")
          router.push('/')
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
  background-image: url("@/assets/imgs/bg.jpg");
  background-size: cover;
}

</style>