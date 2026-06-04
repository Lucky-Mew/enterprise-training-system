<template>
  <div class="quick-access">
    <el-row :gutter="10" justify="center">
      <el-col :span="6" v-for="(btn, index) in buttons" :key="index">
        <el-button
            :type="btn.type"
            :icon="btn.icon"
            class="access-btn"
            @click="btn.action">
          {{ btn.text }}
        </el-button>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router' // 新增导入

const router = useRouter() // 获取路由实例

const buttons = [
  {
    text: '系统公告',
    type: 'primary',
    icon: 'ChatRound',
    action: () => router.push('/manager/notice')
  },
  {
    text: '培训课程',
    type: 'success',
    icon: 'VideoPlay',
    action: () => router.push('/manager/course')
  },
  {
    text: '培训计划',
    type: 'warning',
    icon: 'Document',
    action: () => router.push('/manager/trainingPlan')
  },
  {
    text: '测试题',
    type: 'info',
    icon: 'EditPen',
    action: () => router.push('/manager/question')
  },
  {
    text: '测试题库',
    type: 'success',
    icon: 'EditPen',
    action: () => router.push('/manager/banks')
  },
  {
    text: '管理员信息',
    type: 'warning',
    icon: 'Avatar',
    action: () => router.push('/manager/admin')
  },
  {
    text: '用户信息',
    type: 'info',
    icon: 'User',
    action: () => router.push('/manager/staffUser')
  }
]
</script>

<style scoped>

.access-btn {
  width: 100%;
  height: 4vh;
  margin-bottom: 10px;
  padding: 12px 0;

}
</style>