<template>
  <div class="card" style="margin-bottom: 5px">
    <!-- 搜索栏 -->
    <div style="margin-bottom: 20px">
      <el-input
          v-model="data.searchForm.trainingPlanTitle"
          placeholder="培训计划标题"
          style="width: 200px; margin-right: 10px"/>
      <el-input
          v-model="data.searchForm.staffUserName"
          placeholder="学员姓名"
          style="width: 200px; margin-right: 10px"/>
      <el-button type="primary" @click="loadData">搜索</el-button>
    </div>
    <el-table :data="data.tasks" style="width: 100%"
              :header-cell-style="{color: '#333',backgroundColor: '#eaf4ff'}">
      <el-table-column prop="trainingPlanId" label="培训计划ID"/>
      <el-table-column prop="trainingPlanTitle" label="培训计划标题" />
      <el-table-column prop="staffUserName" label="学员姓名" />
      <el-table-column :formatter="formatTime" prop="startTime" label="开始时间"  />
      <el-table-column :formatter="formatTime" prop="endTime" label="结束时间" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="scope">
          <el-tag v-if="scope.row.status === 0" type="danger" effect="dark">未完成</el-tag>
          <el-tag v-else-if="scope.row.status === 1" type="warning" effect="dark">待审核</el-tag>
          <el-tag v-else-if="scope.row.status === 2" type="success" effect="dark">已完成</el-tag>
          <el-tag v-else type="info">{{ scope.row.status }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template #default="scope">
          <el-button @click="handleReview(scope.row)">审核</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="data.reviewDialogVisible" title="任务审核" class="task-detail-dialog">
      <el-form :model="data.currentTask" class="task-detail-form">
        <el-form-item label="培训计划ID" class="form-label">
          <span>{{ data.currentTask.trainingPlanId }}</span>
        </el-form-item>
        <el-form-item label="培训计划标题" class="form-label">
          <span class="form-value">{{ data.currentTask.trainingPlanTitle }}</span>
        </el-form-item>
        <el-form-item label="学员ID" class="form-label">
          <span class="form-value">{{ data.currentTask.staffUserId }}</span>
        </el-form-item>
        <el-form-item label="开始时间" class="form-label">
          <span class="form-value">{{ formatTime(data.currentTask, null, data.currentTask.startTime) }}</span>
        </el-form-item>
        <el-form-item label="结束时间" class="form-label">
          <span class="form-value">{{ formatTime(data.currentTask, null, data.currentTask.endTime) }}</span>
        </el-form-item>
        <el-form-item label="状态" class="form-label">
          <span class="form-value" v-if="data.currentTask.status === 1">待审核</span>
        </el-form-item>
        <el-form-item label="上传图片">
          <el-image  :src="data.currentTask.imageUrl" :preview-src-list="[`uploads/${data.currentTask.imageUrl}`]" :preview-teleported="true" />
        </el-form-item>
        <el-form-item label="审核结果">
          <el-radio-group v-model="data.reviewResult">
            <el-radio :label="2">通过</el-radio>
            <el-radio :label="0">不通过</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="data.reviewDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitReview">提交审核</el-button>
      </template>
    </el-dialog>
  </div>
  <div class="card">
    <!-- 分页 -->
    <el-pagination
        background
        layout="total, sizes, prev, pager, next"
        :current-page="data.pageNum"
        :page-size="data.pageSize"
        :total="data.total"
        @current-change="handlePageChange"
        @size-change="handleSizeChange"/>
  </div>
</template>

<script setup>
import { reactive, onMounted } from "vue";
import request from "@/utils/request.js";
import { ElMessage } from "element-plus";

const data = reactive({
  user: JSON.parse(localStorage.getItem("code_user") || "{}"),
  tasks: [],
  reviewDialogVisible: false,
  currentTask: {},
  reviewResult: null,
  pageNum: 1,
  pageSize: 10,
  total: 0,
  searchForm: {
    trainingPlanTitle: '',
    staffUserName: ''
  }
});

// 格式化时间的方法
const formatTime = (row, column, cellValue) => {
  if (cellValue) {
    return cellValue.toString().replace('T', ' ');
  }
  return '';
};

const handleReview = (task) => {
  data.currentTask = task;
  data.reviewDialogVisible = true;
};

const submitReview = () => {
  const task = {
    id: data.currentTask.id,
    status: data.reviewResult
  };
  request.post('/trainingTask/reviewTask', task).then(res => {
    if (res.code === '200') {
      ElMessage.success('审核结果已提交');
      data.reviewDialogVisible = false;
      // 重新加载任务列表
      const mentorId = data.user.id;
      request.get(`/trainingTask/getTasksForMentor/${mentorId}`).then(res => {
        if (res.code === "200") {
          data.tasks = res.data;
        } else {
          ElMessage.error(res.msg);
        }
      });
    } else {
      ElMessage.error(res.msg);
    }
  });
};

const loadData = () => {
  request.get('/trainingTask/getTasksPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      trainingPlanTitle: data.searchForm.trainingPlanTitle,
      staffUserName: data.searchForm.staffUserName
    }
  }).then(res => {
    data.tasks = res.data.list;
    data.total = res.data.total;
  });
};

const handlePageChange = (pageNum) => {
  data.pageNum = pageNum;
  loadData();
};

const handleSizeChange = (pageSize) => {
  data.pageSize = pageSize;
  loadData();
};

// 初始化加载
onMounted(loadData);
</script>


<style scoped>
.task-detail-dialog .el-dialog__body {
  padding: 20px;
}

.task-detail-form {
  width: 400px;
  margin: 0 auto;
}

.el-form-item {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
}

.form-value {
  flex: 1;
  color: #666;
  margin-left: 20px;
  word-break: break-word;
}

.el-image__img {
  max-width: 200px;
  max-height: 150px;
  object-fit: contain;
}

.el-radio-group {
  display: flex;
  gap: 15px;
}
</style>
