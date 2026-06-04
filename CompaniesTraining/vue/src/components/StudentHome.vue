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
    text: '培训任务',
    type: 'warning',
    icon: 'Notebook',
    action: () => router.push('/manager/staffUserTasks')
  },
  {
    text: '测试题库',
    type: 'info',
    icon: 'EditPen',
    action: () => router.push('/manager/banks')
  },
  {
    text: '导师信息',
    type: 'success',
    icon: 'User',
    action: () => router.push('/manager/mentorMsg')
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