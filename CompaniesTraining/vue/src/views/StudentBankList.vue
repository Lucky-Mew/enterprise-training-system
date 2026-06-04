<template>
  <div class="card" style="margin-bottom: 5rpx">
    <el-card class="mb-3">
      <el-input
          v-model="searchKeyword"
          placeholder="输入题库名称或描述"
          style="width: 300px; margin-left: 20px"
          clearable
          @input="handleSearch"
      >
      </el-input>
      <el-button type="primary" @click="loadBanks">搜索</el-button>

    </el-card>
    <el-table :data="banks" style="width: 100%">
      <el-table-column prop="title" label="题库名称"></el-table-column>
      <el-table-column prop="description" label="描述"></el-table-column>
      <el-table-column label="分数/操作" width="220">
        <template #default="scope">
          <span v-if="scope.row.score !== undefined" class="score-text">
            {{ scope.row.score }}/{{ scope.row.total }}
          </span>
          <el-button
              v-else
              type="primary"
              @click="openAnswerDialog(scope.row)"
          >
            开始答题
          </el-button>
          <el-button
              v-if="scope.row.answered"
              type="info"
              @click="openAnswerDetailDialog(scope.row.id)"
          >
            查看详情
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 修改：添加默认值 -->
    <AnswerDialog v-model:visible="answerVisible" :bank-id="currentBankId || 0" @submit-success="loadBanks" />

    <!-- 答题详情对话框 -->
    <el-dialog v-model="answerDetailVisible" title="答题详情" width="600px">
        <template v-if="answerDetails.length > 0">
          <div
              v-for="(detail, index) in answerDetails"
              :key="index"
              class="detail-item"
          >
            <!-- 调试：打印整个对象 -->
            <p>第 {{ index + 1 }} 题：{{ detail.question }}</p>
            <p>你的答案：{{ detail.userAnswer }}</p>
            <p>正确答案：{{ detail.correctAnswer }}</p>
            <p>结果：{{ detail.correct ? '✅ 正确' : '❌ 错误' }}</p>
          </div>
        </template>
        <div v-else class="empty-tip">
          <el-empty description="暂无答题记录" />
        </div>
        <el-button @click="answerDetailVisible = false">关闭</el-button>
    </el-dialog>
  </div>
  <div class="card">
    <el-pagination
        v-model:current-page="pagination.page"
        v-model:page-size="pagination.pageSize"
        :page-sizes="[5, 10, 20]"
        :total="pagination.total"
        layout="total, sizes, prev, pager, next"
        @size-change="loadBanks"
        @current-change="loadBanks"
    />
  </div>
</template>

<script setup>
import {onMounted, reactive, ref, nextTick} from 'vue'
import request from '@/utils/request'
import AnswerDialog from '../components/AnswerDialog.vue'
import {ElMessage} from 'element-plus'
const emit = defineEmits(['updateUser']); // 声明组件支持的事件

const data = reactive({
  user: JSON.parse(localStorage.getItem('code_user') || '{}'),
})
const banks = ref([])
const answerVisible = ref(false)
const currentBankId = ref(null)
const answerDetailVisible = ref(false)
const answerDetails = ref([])
const searchKeyword = ref('')

// 新增分页参数
const pagination = reactive({
  page: 1,
  pageSize: 10,
  total: 0
})

// 添加防抖处理
let searchTimer = null
const handleSearch = () => {
  clearTimeout(searchTimer)
  searchTimer = setTimeout(() => {
    pagination.page = 1 // 搜索时重置到第一页
    loadBanks()
  }, 500)
}

// 加载题库列表及用户分数
const loadBanks = async () => {
  // 加载题库列表
  const bankRes = await request.get('/questionBank/list', {
    params: {
      page: pagination.page,
      pageSize: pagination.pageSize,
      keyword: searchKeyword.value || undefined // 空值时传undefined
    }
  })
  if (bankRes.code === '200') {
    const bankList = bankRes.data.records;
    pagination.total = bankRes.data.total; // 同步总条数

    // 加载用户分数
    const userId = data.user.id
    const scoreRes = await request.get(`/score/user/${userId}`)
    const scoreMap = {}
    if (scoreRes.code === '200') {
      scoreRes.data.forEach(s => {
        scoreMap[s.bankId] = {score: s.score, total: s.total, answered: true}
      })
    }

    // 合并分数数据
    banks.value = bankList.map(bank => reactive({
      ...bank,
      ...(scoreMap[bank.id] || {})
    }));
  }
}

// 打开答题弹窗
const openAnswerDialog = (bank) => {
  currentBankId.value = bank.id
  answerVisible.value = true
}

// 打开答题详情对话框
const openAnswerDetailDialog = async (bankId) => {
  try {
    const userId = data.user.id;
    const res = await request.get(`/answer/details/${userId}/${bankId}`);
    if (res.code === '200') {
      // 使用 reactive 包裹每个对象
      answerDetails.value = res.data.map(item => reactive({ ...item }));
      await nextTick(); // 等待 DOM 更新
      answerDetailVisible.value = true;
    }
  } catch (error) {
    console.error('获取详情失败:', error);
    ElMessage.error('获取详情失败: ' + (error.response?.data?.msg || error.message));
  }
};

// 初始化加载
onMounted(() => {
  loadBanks()
})
</script>

<style scoped>
/* 弹窗内容区域滚动 */
.el-dialog__body {
  max-height: 60vh;
  overflow-y: auto;
}

/* 结果样式 */
.correct { color: #67C23A; }
.wrong { color: #F56C6C; }

/* 优化细节项样式 */
.detail-item {
  margin-bottom: 15px;
  padding: 15px;
  background: #f8f9fa;
  border-radius: 4px;
}

.detail-item p {
  margin: 8px 0;
}

/* 强制弹窗层级 */
.el-dialog {
  z-index: 2000 !important;
}
</style>