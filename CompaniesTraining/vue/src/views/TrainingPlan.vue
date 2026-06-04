<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input clearable style="width: 260px; margin-right: 5px" v-model="data.username" placeholder="请输入标题查询" :prefix-icon="Search"></el-input>
      <el-button type="primary" @click="load">查询</el-button>
      <el-button @click="reset">重置</el-button>
    </div>
    <div class="card" style="margin-bottom: 5px">
      <el-button type="primary" @click="handleAdd">新 增</el-button>
      <el-button type="danger" @click="deleteBatch">批量删除</el-button>
    </div>
    <div class="card" style="margin-bottom: 5px">
      <el-table :data="data.tableData" style="width: 100%" @selection-change="handleSelectionChange"
                :header-cell-style="{color: '#333',backgroundColor: '#eaf4ff'}">
        <el-table-column prop="title" label="标题"/>
        <el-table-column prop="content" label="内容"  />
        <el-table-column prop="courseTitle" label="关联课程" />
        <el-table-column :formatter="formatTime" prop="startTime" label="开始时间" />
        <el-table-column :formatter="formatTime" prop="endTime" label="结束时间" />
        <el-table-column label="操作" width="200" >
          <template #default="scope">
            <el-button type="primary" icon="Edit" circle @click="handleEdit(scope.row)"></el-button>
            <el-button type="danger" icon="Delete" circle @click="handleDelete(scope.row.id)"></el-button>
            <el-button type="success" @click="openAssignDialog(scope.row)">下发任务</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="card" style="margin-bottom: 5px">
      <el-pagination
          v-model:current-page="data.pageNum"
          v-model:page-size="data.pageSize"
          :page-sizes="[5, 10]"
          :page-size="data.pageSize"
          layout="total, prev, pager, next ,sizes"
          :total="data.total"
          @size-change="load"
          @current-change="load"
      />
    </div>

    <el-dialog v-model="data.formVisible" title="培训计划" width="500" destroy-on-close="">
      <el-form status-icon ref="formRef" :model="data.form" :rules="data.rules" label-width="80px" style="padding: 20px 30px 10px 0">
        <el-form-item prop="title" label="标题">
          <el-input v-model="data.form.title" autocomplete="off" placeholder="请输入标题"/>
        </el-form-item>
        <el-form-item prop="content" label="内容">
          <el-input v-model="data.form.content" autocomplete="off" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item prop="courseId" label="关联课程">
          <el-select
              v-model="data.form.courseId"
              placeholder="请选择课程"
              clearable
              style="width: 100%"
          >
            <el-option
                v-for="course in data.courseList"
                :key="course.id"
                :label="course.title"
                :value="course.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="startTime" label="开始时间">
          <el-col :span="11">
            <el-date-picker
                v-model="data.form.startTime"
                type="datetime"
                placeholder="Pick a date"
                style="width: 100%"
            />
          </el-col>
        </el-form-item>
        <el-form-item prop="endTime" label="结束时间">
          <el-col :span="11">
            <el-date-picker
                v-model="data.form.endTime"
                type="datetime"
                placeholder="Pick a date"
                style="width: 100%"
            />
          </el-col>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="data.formVisible = false">取消</el-button>
          <el-button type="primary" @click="save">保存</el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog v-model="data.assignDialogVisible" title="选择学员下发任务" width="500">
      <el-form status-icon ref="assignFormRef" :model="data.assignForm" label-width="80px" style="padding: 20px 30px 10px 0">
        <el-form-item label="选择学员">
          <el-select
              v-model="data.assignForm.staffUserId"
              placeholder="搜索或选择学员"
              clearable
              filterable
              style="width: 100%"
          >
            <el-option
                v-for="user in data.staffUserList"
                :key="user.id"
                :label="user.name"
                :value="user.id"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="data.assignDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="assignTask">下发</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import {Search} from "@element-plus/icons-vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";

const data = reactive({
  user:JSON.parse(localStorage.getItem('code_user') || '{}'),
  username: null,
  name: null,
  role: null,
  pageNum: 1,
  pageSize: 5,
  total: 0,
  tableData:[],
  formVisible:false,
  form: {},
  rules: {
    title: [
      { required: true,message: '请填写标题', trigger: 'blur' }
    ],
    content: [
      { required: true,message: '请填写内容', trigger: 'blur' }
    ],
    startTime: [
      {
        type: 'date',
        required: true,
        message: '请选择时间',
        trigger: 'change',
      },
    ],
    endTime: [
      {
        type: 'date',
        required: true,
        message: '请选择时间',
        trigger: 'change',
      },
    ]
  },
  rows: [],
  ids: [],
  assignDialogVisible: false,
  assignForm: {
    trainingPlanId: null,
    staffUserId: null,
    startTime: null,
    endTime: null
  },
  staffUserList: [],
  courseList: []
})

const formRef = ref()
const assignFormRef = ref()

// 格式化时间的方法
const formatTime = (row, column, cellValue) => {
  if (cellValue) {
    return cellValue.toString().replace('T', ' ');
  }
  return '';
};

const load = () => {
  request.get('/trainingPlan/selectPage',{
    params:{
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      username: data.username,
      name: data.name,
      role: data.user.role
    }
  }).then(res => {
    if (res.code === '200') {
      data.tableData = res.data.list
      data.total = res.data.total
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const reset = () => {
  data.title = null
  load()
}

const handleAdd = () => {
  data.formVisible = true
  data.form = {}
}

const add = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      request.post('trainingPlan/add',data.form).then(res => {
        if (res.code === '200') {
          data.formVisible = false
          ElMessage.success('新增成功')
          load()
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}

const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
}

const update = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      request.put('trainingPlan/update',data.form).then(res => {
        if (res.code === '200') {
          data.formVisible = false
          ElMessage.success('修改成功')
          load()
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}

const save = () => {
  data.form.id ? update() : add()
}

const handleDelete = (id) => {
  ElMessageBox.confirm(
      '确定要删除数据?',
      '提示',
      {
        type: 'warning',
      }).then(res => {
    request.delete('trainingPlan/delete/' + id).then(res => {
      if (res.code === '200') {
        ElMessage.success('删除成功')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {})
}

const handleSelectionChange = (rows) => {
  data.rows = rows
  data.ids = data.rows.map(v => v.id)
}

const deleteBatch = () => {
  if (data.rows.length === 0){
    ElMessage.warning('请选择数据')
    return
  }
  ElMessageBox.confirm(
      '确定要删除所选数据?',
      '提示',
      {
        type: 'warning',
      }).then(res => {
    request.delete('trainingPlan/deleteBatch',{data: data.rows}).then(res => {
      if (res.code === '200') {
        ElMessage.success('删除成功')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {})
}

const loadCourses = () => {
  request.get('/course/selectAll').then(res => {
    if (res.code === '200') {
      data.courseList = res.data;
    }
  });
};

const loadStaffUsers = () => {
  request.get('/staffUser/selectAll').then(res => {
    if (res.code === '200') {
      // 只显示学员（role=3），过滤掉导师自己和其他导师
      data.staffUserList = res.data.filter(user => user.role === '3');
    }
  });
};

const openAssignDialog = (row) => {
  data.assignDialogVisible = true;
  data.assignForm.trainingPlanId = row.id;
  data.assignForm.startTime = row.startTime;
  data.assignForm.endTime = row.endTime;
};

const assignTask = () => {
  if (!data.assignForm.staffUserId) {
    ElMessage.warning('请选择学员');
    return;
  }
  const task = {
    trainingPlanId: data.assignForm.trainingPlanId,
    staffUserId: data.assignForm.staffUserId,
    staffUserMetId: data.user.id,
    startTime: data.assignForm.startTime,
    endTime: data.assignForm.endTime,
    status: 0
  };
  request.post('/trainingTask/assign', task).then(res => {
    if (res.code === '200') {
      data.assignDialogVisible = false;
      ElMessage.success('任务下发成功');
    } else {
      ElMessage.error(res.msg);
    }
  });
};

// 初始化时调用
loadCourses();
loadStaffUsers();
load()
</script>