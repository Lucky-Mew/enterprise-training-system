<template>
  <div>
    <!-- 搜索栏 -->
    <div class="card" style="margin-bottom: 5px">
      <el-input v-model="state.searchContent" placeholder="题目内容" style="width: 200px; margin-right: 10px"></el-input>
      <el-select v-model="state.searchType" placeholder="题型" clearable style="width: 120px; margin-right: 10px">
        <el-option label="单选题" value="SINGLE"></el-option>
        <el-option label="多选题" value="MULTIPLE"></el-option>
        <el-option label="判断题" value="JUDGMENT"></el-option>
      </el-select>
      <el-button type="primary" @click="load">搜索</el-button>
      <el-button @click="reset">重置</el-button>
    </div>

    <!-- 操作按钮 -->
    <div class="card" style="margin-bottom: 5px">
      <el-button type="primary" @click="handleAdd">新增题目</el-button>
      <el-button type="danger" @click="deleteBatch">批量删除</el-button>
    </div>

    <!-- 数据表格 -->
    <div class="card">
      <el-table
          :data="state.tableData"
          @selection-change="handleSelectionChange"
          style="width: 100%"
      >
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="content" label="题目内容" min-width="300"></el-table-column>
        <el-table-column prop="type" label="题型" width="100">
          <template #default="scope">
            {{ formatType(scope.row.type) }}
          </template>
        </el-table-column>
        <el-table-column prop="difficulty" label="难度" width="100">
          <template #default="scope">
            <el-rate :model-value="scope.row.difficulty" disabled/>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" :formatter="formatTime"></el-table-column>
        <el-table-column label="操作" width="150">
          <template #default="scope">
            <el-button type="primary" icon="Edit" @click="handleEdit(scope.row)"></el-button>
            <el-button type="danger" icon="Delete" @click="handleDelete(scope.row.id)"></el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 分页 -->
    <div class="card">
      <el-pagination
          v-model:current-page="state.pageNum"
          v-model:page-size="state.pageSize"
          :total="state.total"
          @current-change="load"
          @size-change="load"
          layout="total, prev, pager, next"
      />
    </div>

    <!-- 新增/编辑弹窗 -->
    <el-dialog v-model="state.dialogVisible" :title="state.form.id ? '编辑题目' : '新增题目'" width="600px">
      <el-form :model="state.form" label-width="80px">
        <el-form-item label="题型" required>
          <el-select
              v-model="state.form.type"
              @change="handleTypeChange"
              placeholder="请选择题型"
          >
            <el-option label="单选题" value="SINGLE"></el-option>
            <el-option label="多选题" value="MULTIPLE"></el-option>
            <el-option label="判断题" value="JUDGMENT"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="题目内容" required>
          <el-input
              v-model="state.form.content"
              type="textarea"
              :rows="3"
              placeholder="请输入题目内容"
          ></el-input>
        </el-form-item>

        <el-form-item v-if="showOptions" label="选项配置">
          <div v-for="(option, index) in state.optionsList" :key="index" class="option-item">
            <el-input
                v-model="option.value"
                style="width: 85%; margin-right: 10px"
                :placeholder="`选项 ${String.fromCharCode(65 + index)}`"
            ></el-input>
            <el-button type="danger" icon="Delete" @click="removeOption(index)"></el-button>
          </div>
          <el-button type="primary" @click="addOption">添加选项</el-button>
        </el-form-item>

        <el-form-item label="正确答案" required>
          <template v-if="state.form.type === 'JUDGMENT'">
            <el-radio-group v-model="state.form.answer">
              <el-radio label="T">正确</el-radio>
              <el-radio label="F">错误</el-radio>
            </el-radio-group>
          </template>
          <template v-else>
            <el-checkbox-group v-model="state.form.answerList">
              <el-checkbox
                  v-for="(option, index) in state.optionsList"
                  :key="index"
                  :label="String.fromCharCode(65 + index)"
              >
                {{ String.fromCharCode(65 + index) }}
              </el-checkbox>
            </el-checkbox-group>
          </template>
        </el-form-item>

        <el-form-item label="难度等级">
          <el-rate v-model="state.form.difficulty" :max="5"/>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="state.dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'

const state = reactive({
  // 搜索条件
  searchContent: '',
  searchType: '',

  // 分页参数
  pageNum: 1,
  pageSize: 10,
  total: 0,

  // 表格数据
  tableData: [],

  // 对话框控制
  dialogVisible: false,

  // 表单数据
  form: {
    id: undefined,
    type: 'SINGLE',
    content: '',
    options: '',
    answer: '',
    difficulty: 3,
    answerList: []
  },

  // 选项列表
  optionsList: [],

  // 选中项
  selectedIds: []
})


// 处理表格多选
const handleSelectionChange = (selection) => {
  state
      .selectedIds = selection.map(item => item.id)
}

// 题型显示转换
const formatType = (type) => {
  const map = {
    'SINGLE': '单选题',
    'MULTIPLE': '多选题',
    'JUDGMENT': '判断题'
  }
  return map[type] || '未知题型'
}

// 加载数据
const load = async () => {
  const res = await request.get('/question/selectPage', {
    params: {
      pageNum: state.pageNum,
      pageSize: state.pageSize,
      content: state.searchContent,
      type: state.searchType
    }
  })
  if (res.code === '200') {
    state.tableData = res.data.list
    state.total = res.data.total
  }
}

// 重置搜索
const reset = () => {
  state.searchContent = ''
  state.searchType = ''
  load()
}

// 处理题型变化
const handleTypeChange = () => {
  state.optionsList = []
  state.form.answerList = []
}

// 动态显示选项配置
const showOptions = computed(() => {
  return state.form.type !== 'JUDGMENT'
})

// 添加选项
const addOption = () => {
  state.optionsList.push({ value: '' })
}

// 删除选项
const removeOption = (index) => {
  state.optionsList.splice(index, 1)
}

// 打开新增/编辑弹窗
const handleAdd = () => {
  state.form = { type: 'SINGLE', difficulty: 3 }
  state.optionsList = []
  state.dialogVisible = true
}

const handleEdit = async (row) => {
  const res = await request.get(`/question/${row.id}`)
  if (res.code === '200') {
    state.form = res.data
    // 处理选项反序列化
    if (res.data.options) {
      state.optionsList = JSON.parse(res.data.options).map(value => ({ value }))
    }
    // 处理答案
    if (res.data.type !== 'JUDGMENT') {
      state.form.answerList = res.data.answer.split(',')
    }
    state.dialogVisible = true
  }
}

// 保存数据
const save = async () => {
  // 处理选项序列化
  if (state.form.type !== 'JUDGMENT') {
    state.form.options = JSON.stringify(state.optionsList.map(opt => opt.value))
    state.form.answer = state.form.answerList.join(',')
  }

  const url = state.form.id ? '/question/update' : '/question/add'
  const method = state.form.id ? 'put' : 'post'

  const res = await request[method](url, state.form)
  if (res.code === '200') {
    ElMessage.success('操作成功')
    state.dialogVisible = false
    load()
  }
}
// 删除单个
const handleDelete = (id) => {
  ElMessageBox
      .confirm('确定删除？', '警告').then(() => {
    request
        .delete(`/question/delete/${id}`).then(() => {
      ElMessage
          .success('删除成功')
      load()
    })
  })
}

// 批量删除
const deleteBatch = () => {
  if (state.selectedIds.length === 0) {
    ElMessage
        .warning('请选择记录')
    return
  }
  ElMessageBox
      .confirm('确定批量删除？', '警告').then(() => {
    request
        .delete('/question/deleteBatch', { data: state.selectedIds }).then(() => {
      ElMessage
          .success('删除成功')
      load()
    })
  })
}

// 格式化时间
const formatTime = (row, column, cellValue) => {
  return cellValue ? new Date(cellValue).toLocaleString() : ''
}

// 初始化加载
load()
</script>

<style scoped>
.card {
  padding: 15px;
  background: white;
  border-radius: 4px;
  margin-bottom: 10px;
}
.option-item {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}
</style>