<!-- src/components/QuickAccessPanel.vue -->
<template>
  <div class="quick-access">
    <el-row :gutter="10">
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
const buttons = [
  {
    text: '开始学习',
    type: 'primary',
    icon: 'VideoPlay',
    action: () => router.push('/learning')
  },
  {
    text: '我的课程',
    type: 'success',
    icon: 'Notebook',
    action: () => router.push('/courses')
  },
  {
    text: '考试中心',
    type: 'warning',
    icon: 'EditPen',
    action: () => router.push('/exams')
  },
  {
    text: '学习报告',
    type: 'info',
    icon: 'DataAnalysis',
    action: () => router.push('/reports')
  }
]
</script>

<style scoped>
.access-btn {
  width: 100%;
  margin-bottom: 10px;
  padding: 12px 0;
}
</style>