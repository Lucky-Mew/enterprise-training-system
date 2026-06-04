<template>
  <div class="card" style="margin-bottom: 5px">
    <!-- 新增搜索表单 -->
    <div class="search-form" style="margin-bottom: 20px">
      <el-input
          v-model="data.trainingPlanTitle"
          placeholder="请输入培训计划标题"
          style="width: 200px; margin-right: 10px"
      />
      <el-button type="primary" @click="loadTasks">搜索</el-button>
    </div>
    <el-table :data="data.tableData" style="width: 100%"
              :header-cell-style="{color: '#333',backgroundColor: '#eaf4ff'}">
      <el-table-column type="selection" width="55"/>
      <el-table-column prop="trainingPlanId" label="培训计划ID"/>
      <el-table-column prop="trainingPlanTitle" label="培训计划标题" />
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
          <el-button @click="handleViewDetails(scope.row)">查看详情</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="data.dialogVisible" title="任务详情" class="task-detail-dialog">
      <div class="task-detail-form">
        <div class="form-row">
          <div class="form-label">培训计划ID</div>
          <div class="form-value">{{ data.currentTask.trainingPlanId }}</div>
        </div>
        <div class="form-row">
          <div class="form-label">培训计划标题</div>
          <div class="form-value">{{ data.currentTask.trainingPlanTitle }}</div>
        </div>
        <div class="form-row">
          <div class="form-label">开始时间</div>
          <div class="form-value">{{ formatTime(data.currentTask, null, data.currentTask.startTime) }}</div>
        </div>
        <div class="form-row">
          <div class="form-label">结束时间</div>
          <div class="form-value">{{ formatTime(data.currentTask, null, data.currentTask.endTime) }}</div>
        </div>
        <div class="form-row">
          <div class="form-label">状态</div>
          <div class="form-value">
            <span v-if="data.currentTask.status === 0">未完成</span>
            <span v-else-if="data.currentTask.status === 1">待审核</span>
            <span v-else-if="data.currentTask.status === 2">已完成</span>
            <span v-else>{{ data.currentTask.status }}</span>
          </div>
        </div>
        <div class="form-row">
          <div class="form-label">导师名字</div>
          <div class="form-value">{{ data.currentTask.mentorName }}</div>
        </div>
        <div class="form-row">
          <div class="form-label">上传图片</div>
          <div class="form-value">
            <el-upload
                action="http://localhost:9999/trainingTask/uploadImage"
                :headers="{ token: data.user.token }"
                :on-success="handleUploadSuccess"
                list-type="picture"
            >
              <el-button type="primary">上传图片</el-button>
            </el-upload>
          </div>
        </div>
      </div>
      <template #footer>
        <el-button @click="data.dialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="handleSubmitTask" :disabled="!data.currentTask.imageUrl">提交审核</el-button>
      </template>
    </el-dialog>
  </div>
  <div class="card">
    <el-pagination
        background
        @current-change="handlePageChange"
        @size-change="handleSizeChange"
        :current-page="data.pagination.pageNum"
        :page-sizes="[5, 10, 20]"
        :page-size="data.pagination.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="data.pagination.total"
        style="margin-top: 20px"
    />
  </div>
</template>

<script setup>
import {reactive, onMounted} from "vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";

const emit = defineEmits(['updateUser']);

const data = reactive({
  user: JSON.parse(localStorage.getItem("code_user") || "{}"),
  tasks: [],
  dialogVisible: false,
  currentTask: {},
  tableData: [],
  pagination: {
    pageNum: 1,
    pageSize: 10,
    total: 0
  },
  trainingPlanTitle: "",
});

// 格式化时间的方法
const formatTime = (row, column, cellValue) => {
  if (cellValue) {
    return cellValue.toString().replace('T', ' ');
  }
  return '';
};



const handleViewDetails = (task) => {
  data.currentTask = task;
  data.currentTask.imageUrl = ''; // 清空图片URL
  data.dialogVisible = true;
};

const handleUploadSuccess = (res) => {
  if (res.code === '200') {
    ElMessage.success('图片上传成功');
    data.currentTask.imageUrl = res.data;
  } else {
    ElMessage.error(res.msg);
  }
};

const handleSubmitTask = () => {
  request.post('/trainingTask/submitTask', {
    id: data.currentTask.id, // 修改为 id
    imageUrl: data.currentTask.imageUrl,
    status: 1
  }).then(res => {
    if (res.code === '200') {
      ElMessage.success('任务已提交审核');
      data.dialogVisible = false;
      // 重新加载任务列表
      const staffUserId = data.user.id;
      request.get(`/trainingTask/getTasksByStaffUserId/${staffUserId}`).then(res => {
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

// 加载任务数据
const loadTasks = () => {
  request.get("/trainingTask/getTasksPage", {
    params: {
      pageNum: data.pagination.pageNum,
      pageSize: data.pagination.pageSize,
      trainingPlanTitle: data.trainingPlanTitle,
      staffUserName: null,
    }
  }).then(res => {
    if (res.code === "200") {
      data.tableData = res.data.list; // PageInfo数据结构
      data.pagination.total = res.data.total;
    }
  });
};

// 分页事件
const handlePageChange = (page) => {
  data.pagination.pageNum = page;
  loadTasks();
};

const handleSizeChange = (size) => {
  data.pagination.pageSize = size;
  loadTasks();
};

// 初始化加载
onMounted(() => {
  loadTasks();
});
</script>

<style scoped>
.task-detail-dialog .el-dialog__body {
  padding: 20px;
}

.task-detail-form {
  display: flex;
  flex-direction: column;
}

.form-row {
  display: flex;
  margin-bottom: 15px;
}

.form-label {
  width: 120px;
  font-weight: bold;
  color: #333;
}

.form-value {
  flex: 1;
  color: #666;
}
</style>