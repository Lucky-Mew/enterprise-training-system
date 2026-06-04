<template>
  <div class="card" style="margin-bottom: 5rpx">
    <!-- 创建题库 -->
    <el-card class="mb-3">
      <el-button type="primary" @click="showCreateDialog">新建题库</el-button>
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

    <!-- 题库列表 -->
    <el-table :data="banks" style="width: 100%">
      <el-table-column prop="title" label="题库名称"></el-table-column>
      <el-table-column prop="description" label="描述"></el-table-column>
      <el-table-column label="操作" width="200">
        <template #default="scope">
          <el-button @click="manageQuestions(scope.row)">管理题目</el-button>
          <el-button type="danger" @click="deleteBank(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 创建题库对话框 -->
    <el-dialog v-model="createDialogVisible" title="新建题库">
      <el-form :model="newBank">
        <el-form-item label="题库名称" required>
          <el-input v-model="newBank.title"></el-input>
        </el-form-item>
        <el-form-item label="题库描述">
          <el-input v-model="newBank.description" type="textarea"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="createDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="createBank">创建</el-button>
      </template>
    </el-dialog>

    <!-- 题目管理对话框 -->
    <QuestionSelector
        v-model:visible="questionManageVisible"
        :bank-id="currentBankId"
        @update:questions="handleQuestionsUpdate"
    />
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
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'
import QuestionSelector from './QuestionSelector.vue'

const banks = ref([])
const createDialogVisible = ref(false)
const questionManageVisible = ref(false)
const currentBankId = ref(null)

const newBank = reactive({
  title: '',
  description: ''
})
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

// 显示弹窗
const showCreateDialog = () => {
  newBank.title = ''
  newBank.description = ''
  createDialogVisible.value = true
}

// 加载题库列表
const loadBanks = async () => {
  const res = await request.get('/questionBank/list', {
    params: {
      page: pagination.page,
      pageSize: pagination.pageSize,
      keyword: searchKeyword.value || undefined // 空值时传undefined
    }
  })
  if (res.code === '200') {
    banks.value = res.data.records
    pagination.total = res.data.total
  }
}

// 创建题库
const createBank = async () => {
  const res = await request.post('/questionBank/create', newBank)
  if (res.code === '200') {
    ElMessage.success('创建成功')
    createDialogVisible.value = false
    searchKeyword.value = ''
    pagination.page = 1
    loadBanks()
  }
}

// 删除题库方法
const deleteBank = async (bankId) => {
  try {
    await ElMessageBox.confirm('确定删除该题库吗？', '警告', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    const res = await request.delete(`/questionBank/delete/${bankId}`)
    if (res.code === '200') {
      ElMessage
          .success('删除成功')
      // 立即更新本地数据（避免重新请求）
      banks
          .value = banks.value.filter(b => b.id !== bankId)
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage
          .error('删除失败: ' + (error.response?.data?.msg || error.message))
    }
  }
  loadBanks() // 删除后重新加载当前页
}

// 管理题目
const manageQuestions = (bank) => {
  currentBankId.value = bank.id
  questionManageVisible.value = true
}

// 处理题目更新
const handleQuestionsUpdate = async (questionIds) => {
  await request.post(`/questionBank/${currentBankId.value}/updateQuestions`, questionIds)
  ElMessage.success('题目添加成功')
}

onMounted(() => {
  loadBanks()
})
</script>