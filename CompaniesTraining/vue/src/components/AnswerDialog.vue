<template>
  <el-dialog v-model="visible" :title="`答题 - ${bankTitle}`" width="800px">
    <!-- 题目列表 -->
    <div v-for="(q, index) in questions" :key="q.id" class="question-item">
      <h4>第 {{ index + 1 }} 题：{{ q.content }}</h4>
      <el-tag :type="getTagType(q.type)">{{ formatType(q.type) }}</el-tag>
      <!-- 单选题/多选题 -->
      <div v-if="q.type !== 'JUDGMENT'">
        <el-checkbox-group v-model="userAnswers[q.id]" v-if="q.type === 'MULTIPLE'">
          <el-checkbox
              v-for="(opt, idx) in JSON.parse(q.options)"
              :key="idx"
              :label="String.fromCharCode(65 + idx)"
          >
            {{ String.fromCharCode(65 + idx) }}. {{ opt }}
          </el-checkbox>
        </el-checkbox-group>

        <el-radio-group v-model="userAnswers[q.id]" v-else>
          <el-radio
              v-for="(opt, idx) in JSON.parse(q.options)"
              :key="idx"
              :label="String.fromCharCode(65 + idx)"
          >
            {{ String.fromCharCode(65 + idx) }}. {{ opt }}
          </el-radio>
        </el-radio-group>
      </div>

      <!-- 判断题 -->
      <el-radio-group v-else v-model="userAnswers[q.id]">
        <el-radio label="T">正确</el-radio>
        <el-radio label="F">错误</el-radio>
      </el-radio-group>
    </div>

    <!-- 操作按钮 -->
    <template #footer>
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="submitAnswers">提交答案</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import {reactive, ref, watch} from 'vue'
import request from '@/utils/request.js'

const data = reactive({
  user:JSON.parse(localStorage.getItem('code_user') || '{}'),})

const props = defineProps({
  bankId: {
    type: Number,
    required: true
  }
})

const visible = defineModel('visible')
const questions = ref([])
const userAnswers = ref({})
const bankTitle = ref('')

// 加载题库标题和题目
const loadData = async () => {
  // 加载题库标题
  const bankRes = await request.get(`/questionBank/${props.bankId}`)
  if (bankRes.code === '200') {
    bankTitle.value = bankRes.data.title
  }

  // 加载题目
  const res = await request.get(`/questionBank/${props.bankId}/questions`)
  if (res.code === '200') {
    questions.value = res.data
  }
}

// 提交答案
const emit = defineEmits(['submit-success'])

const submitAnswers = async () => {
  try {
    // 获取用户信息
    const user = JSON.parse(localStorage.getItem('code_user') || '{}');
    if (!user.id) {
      ElMessage
          .error('用户未登录');
      return;
    }

    // 转换答案格式：确保所有值为字符串
    const formattedAnswers = {};
    for (const [questionId, answer] of Object.entries(userAnswers.value)) {
      if (Array.isArray(answer)) {
        // 多选题：数组转逗号分隔字符串
        formattedAnswers
            [questionId] = answer.join(',');
      } else {
        // 单选题/判断题：直接保留字符串
        formattedAnswers
            [questionId] = answer;
      }
    }

    // 提交请求
    const res = await request.post('/answer/submit', {
      userId: user.id,
      bankId: props.bankId,
      answers: formattedAnswers // 使用格式化后的答案
    });

    if (res.code === '200') {
      ElMessage
          .success(`得分：${res.data.score}/${res.data.total}`);
      emit('submit-success');
      visible
          .value = false;
    } else {
      ElMessage
          .error(res.msg || '提交失败');
    }
  } catch (error) {
    console
        .error('提交失败:', error);
    ElMessage
        .error('提交失败: ' + (error.response?.data?.msg || error.message));
  }
};

// 当弹窗打开时加载数据
watch(visible, (val) => {
  if (val) {
    userAnswers.value = {} // 清空历史答案
    loadData()
  }
})

// 添加题型转换函数
const formatType = (type) => {
  const typeMap = {
    SINGLE: '单选题',
    MULTIPLE: '多选题',
    JUDGMENT: '判断题'
  };
  return typeMap[type] || '未知题型';
};

// 标签样式类型
const getTagType = (type) => {
  const typeStyle = {
    SINGLE: 'primary',
    MULTIPLE: 'success',
    JUDGMENT: 'warning'
  };
  return typeStyle[type] || 'info';
};
</script>

<style scoped>
.question-item {
  margin-bottom: 20px;
  padding: 10px;
  border-bottom: 1px solid #eee;
}
</style>

<style scoped>
.question-header {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 10px;
}
</style>