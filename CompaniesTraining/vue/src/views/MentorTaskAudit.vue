<template>
  <div class="card" style="margin-bottom: 5px">
    <el-table :data="data.tasks" style="width: 100%"
              :header-cell-style="{color: '#333',backgroundColor: '#eaf4ff'}">
      <el-table-column prop="trainingPlanId" label="培训计划ID"/>
      <el-table-column prop="trainingPlanTitle" label="培训计划标题" />
      <el-table-column prop="staffUserId" label="学员ID" />
      <el-table-column :formatter="formatTime" prop="startTime" label="开始时间"  />
      <el-table-column :formatter="formatTime" prop="endTime" label="结束时间" />
      <el-table-column prop="status" label="状态">
        <template #default="scope">
          <span v-if="scope.row.status === 0">未完成</span>
          <span v-else-if="scope.row.status === 0">未完成</span>
          <span v-else-if="scope.row.status === 1">待审核</span>
          <span v-else-if="scope.row.status === 2">已完成</span>
          <span v-else>{{ scope.row.status }}</span>
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
          <span class="form-value">{{ data.currentTask.studentName }}</span>
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
  reviewResult: null
});

// 格式化时间的方法
const formatTime = (row, column, cellValue) => {
  if (cellValue) {
    return cellValue.toString().replace('T', ' ');
  }
  return '';
};

onMounted(() => {
  const mentorId = data.user.id;
  request.get(`/trainingTask/getTasksForMentor/${mentorId}`).then(res => {
    if (res.code === "200") {
      data.tasks = res.data;
    } else {
      ElMessage.error(res.msg);
    }
  });
});

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
