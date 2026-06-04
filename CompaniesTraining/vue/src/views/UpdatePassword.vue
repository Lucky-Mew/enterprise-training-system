<template>
  <div class="password-container">
    <div class="password-card">
      <h2 class="password-title">修改密码</h2>
      <el-form
          ref="formRef"
          :rules="data.rules"
          :model="data.user"
          label-width="100px"
          class="password-form"
      >
        <el-form-item prop="password" label="原密码">
          <el-input
              v-model="data.user.password"
              show-password
              size="large"
              placeholder="请输入原密码"
          >
            <template #prefix>
              <el-icon class="el-input__icon"><lock /></el-icon>
            </template>
          </el-input>
        </el-form-item>

        <el-form-item prop="newPassword" label="新密码">
          <el-input
              v-model="data.user.newPassword"
              show-password
              size="large"
              placeholder="请输入新密码（8-20位字符）"
          >
            <template #prefix>
              <el-icon class="el-input__icon"><lock /></el-icon>
            </template>
          </el-input>
        </el-form-item>

        <el-form-item prop="newPassword2" label="确认密码">
          <el-input
              v-model="data.user.newPassword2"
              show-password
              size="large"
              placeholder="请再次输入新密码"
          >
            <template #prefix>
              <el-icon class="el-input__icon"><lock /></el-icon>
            </template>
          </el-input>
        </el-form-item>

        <div class="submit-btn">
          <el-button
              type="primary"
              size="large"
              @click="updatePassword"
              class="save-btn"
          >
            保存修改
          </el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import { Lock } from '@element-plus/icons-vue'
import request from "@/utils/request.js";
import { ElMessage } from "element-plus";

const data = reactive({
  user: JSON.parse(localStorage.getItem('code_user') || '{}'),
  rules: {
    password: [
      { required: true, message: '请输入原密码', trigger: 'blur' }
    ],
    newPassword: [
      { required: true, message: '请输入新密码', trigger: 'blur' },
      { min: 8, max: 20, message: '长度在8到20个字符', trigger: 'blur' },
      { pattern: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[^]{8,20}$/,
        message: '需包含大小写字母和数字' }
    ],
    newPassword2: [
      { required: true, message: '请确认新密码', trigger: 'blur' },
      { validator: (rule, value, callback) => {
          if (value !== data.user.newPassword) {
            callback(new Error('两次输入密码不一致'))
          } else {
            callback()
          }
        }, trigger: 'blur'
      }
    ]
  }
})

const formRef = ref()

const updatePassword = () => {
  formRef.value.validate(valid => {
    if (valid) {
      request.post('/updatePassword', data.user).then(res => {
        if (res.code === '200') {
          ElMessage.success({
            message: '修改成功，即将跳转登录页面',
            duration: 1500
          })
          setTimeout(() => {
            localStorage.removeItem('code_user')
            window.location.href = '/login'
          }, 1800)
        } else {
          ElMessage.error(res.msg || '修改失败')
        }
      }).catch(() => {
        ElMessage.error('请求失败，请检查网络')
      })
    }
  })
}
</script>

<style scoped>
.password-container {
  min-height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  background: #f5f7fa;
  padding: 20px;
}

.password-card {
  background: white;
  border-radius: 12px;
  padding: 40px 50px;
  width: 100%;
  max-width: 500px;
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.06);
}

.password-title {
  text-align: center;
  margin-bottom: 35px;
  color: #2c3e50;
  font-size: 22px;
  font-weight: 600;
}

.password-form {
  :deep(.el-form-item__label) {
    font-weight: 500;
    color: #5a6c84;
    padding-bottom: 8px;
  }

  :deep(.el-input__inner) {
    height: 44px;
    border-radius: 8px;
    padding-left: 40px;
  }

  :deep(.el-input__prefix) {
    display: flex;
    align-items: center;
    left: 12px;
  }
}

.submit-btn {
  margin-top: 30px;
  text-align: center;
}

.save-btn {
  width: 100%;
  height: 44px;
  font-size: 16px;
  border-radius: 8px;
  background: linear-gradient(45deg, #409eff, #79bbff);
  transition: all 0.3s;
}

.save-btn:hover {
  opacity: 0.9;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
}

@media (max-width: 768px) {
  .password-card {
    padding: 30px 20px;
    margin: 20px;
  }

  .password-title {
    font-size: 20px;
    margin-bottom: 25px;
  }
}
</style>