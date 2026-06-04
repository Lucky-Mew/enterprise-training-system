<template>
  <div class="container">
    <div class="card">
      <h2 class="title">个人中心</h2>
      <el-form ref="formRef" :model="data.user" label-width="80px">
        <!-- 头像上传 -->
        <el-form-item label="头像" class="avatar-item">
          <el-upload
              action="http://localhost:9999/files/upload"
              :headers="{ token: data.user.token }"
              :on-success="handleFileSuccess"
              :show-file-list="false"
          >
            <div class="avatar-wrapper">
              <img v-if="data.user.avatar" :src="data.user.avatar" class="avatar" />
              <div v-else class="avatar-upload">
                <el-icon :size="30"><Plus /></el-icon>
                <span class="upload-text">点击上传</span>
              </div>
            </div>
          </el-upload>
        </el-form-item>

        <!-- 基本信息 -->
        <el-form-item label="账号">
          <el-input v-model="data.user.username" placeholder="请输入账号" />
        </el-form-item>

        <el-form-item label="名称">
          <el-input v-model="data.user.name" placeholder="请输入名称" />
        </el-form-item>

        <el-form-item label="电话">
          <el-input v-model="data.user.phone" placeholder="请输入电话" />
        </el-form-item>

        <el-form-item label="邮箱">
          <el-input v-model="data.user.email" placeholder="请输入邮箱" />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="update" class="submit-btn">保存修改</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>


<script setup>

import {reactive} from "vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";

const data = reactive({
  user:JSON.parse(localStorage.getItem('code_user') || '{}'),

})

const handleFileSuccess = (res) => {
  data.user.avatar = res.data
}

const emit = defineEmits(['updateUser'])

const update = () => {
  let url
  if (data.user.role === '1') {
    url = '/admin/update'
  } if  (data.user.role === '2' || data.user.role === '3') {
    url = '/staffUser/update'
  }
  request.put(url, data.user).then(res => {
    if (res.code === '200') {
      ElMessage.success("更新成功")
      localStorage.setItem("code_user", JSON.stringify(data.user))
      emit('updateUser')
    }
  })
}

</script>

<style scoped>
.container {
  padding: 20px;
  background: #f8f9fa;
  min-height: 100%;
}

.card {
  background: white;
  padding: 24px;
  border-radius: 8px;
  max-width: 600px;
  margin: 0 auto;
  box-shadow: 0 2px 12px rgba(0,0,0,0.05);
}

.title {
  text-align: center;
  margin-bottom: 24px;
  color: #303133;
}

/* 头像样式 */
.avatar-item {
  text-align: center;
}

.avatar-wrapper {
  width: 120px;
  height: 120px;
  border: 1px dashed #dcdfe6;
  border-radius: 50%;
  overflow: hidden;
  cursor: pointer;
  margin: 0 auto;
}

.avatar {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.avatar-upload {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  color: #909399;
}

.upload-text {
  font-size: 12px;
  margin-top: 8px;
}

/* 表单样式 */
.el-form-item {
  margin-bottom: 18px;
}

.el-input, .el-select {
  width: 100%;
}

.tip {
  font-size: 12px;
  color: #f56c6c;
  margin-top: 4px;
}

.submit-btn {
  width: 100%;
  height: 40px;
}
</style>