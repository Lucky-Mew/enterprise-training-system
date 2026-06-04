<template>
  <el-dialog v-model="visible" title="管理题目" width="800px">
    <!-- 搜索栏 -->
    <div style="margin-bottom: 15px; display: flex; gap: 10px;">
      <el-input
          v-model="searchKeyword"
          placeholder="搜索题目内容"
          clearable
          style="width: 250px"
      />
      <el-select v-model="searchType" placeholder="筛选题型" clearable style="width: 130px">
        <el-option label="单选题" value="SINGLE" />
        <el-option label="多选题" value="MULTIPLE" />
        <el-option label="判断题" value="JUDGMENT" />
      </el-select>
    </div>
    <div class="flex gap-4">
      <!-- 题目列表 -->
      <div class="w-1/2">
        <el-table
            :data="filteredQuestions"
            @selection-change="handleSelectionChange"
            height="400"
        >
          <el-table-column type="selection" width="55"></el-table-column>
          <el-table-column prop="content" label="题目内容" show-overflow-tooltip></el-table-column>
          <el-table-column prop="type" label="题型" width="90">
            <template #default="{row}">
              {{ typeMap[row.type] }}
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 已选题目 -->
      <div class="w-1/2">
        <h3 class="mb-2">已选题目（{{ selectedQuestions.length }}）</h3>
        <ul>
          <li v-for="(q, index) in selectedQuestions" :key="q.id" class="selected-question-item">
            {{ q.content }}
            <el-button
                type="danger"
                icon="Delete"
                size="small"
                @click="removeSelected(index)"
            ></el-button>
          </li>
        </ul>
      </div>
    </div>

    <template #footer>
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="submit">保存</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import request from '@/utils/request'

const props = defineProps(['bankId'])
const emit = defineEmits(['update:questions'])

const visible = defineModel('visible')
const allQuestions = ref([])
const selectedQuestions = ref([])
const searchKeyword = ref('')
const searchType = ref('')

// 根据搜索条件过滤题目
const filteredQuestions = computed(() => {
  return allQuestions.value.filter(q => {
    const matchKeyword = !searchKeyword.value || q.content.includes(searchKeyword.value)
    const matchType = !searchType.value || q.type === searchType.value
    return matchKeyword && matchType
  })
})

const typeMap = {
  SINGLE: '单选题',
  MULTIPLE: '多选题',
  JUDGMENT: '判断题'
}

// 加载所有题目
const loadQuestions = async () => {
  const res = await request.get('/question/selectAll')
  if (res.code === '200') {
    allQuestions.value = res.data
  }
}

const loadSelected = async () => {
  const res = await request.get(`/questionBank/${props.bankId}/questions`)
  if (res.code === '200') {
    selectedQuestions
        .value = res.data // 根据接口返回的数据结构调整
  }
}

watch(visible, (val) => {
  if (val) {
    loadQuestions()
    loadSelected() // 初始化已选题目
  }
})

// 修正选中逻辑
const handleSelectionChange = (selection) => {
  selectedQuestions
      .value =
      selection
}

// 添加删除已选题目方法
const removeSelected = (index) => {
  selectedQuestions
      .value.splice(index, 1)
}

// 提交选择
const submit = () => {
  emit('update:questions', selectedQuestions.value.map(q => q.id))
  visible.value = false
}

watch(visible, (val) => {
  if (val) loadQuestions()
})
</script>

<style scoped>
.selected-question-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 0;
  border-bottom: 1px solid #eee;
}
</style>