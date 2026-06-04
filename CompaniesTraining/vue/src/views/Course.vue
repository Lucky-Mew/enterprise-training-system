<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input clearable style="width: 260px; margin-right: 5px" v-model="data.title" placeholder="请输入标题查询" :prefix-icon="Search"></el-input>
      <el-button type="primary" @click="load">查询</el-button>
      <el-button @click="reset">重置</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px" v-if="data.user.role === '1'" >
      <el-button type="primary" @click="handleAdd" >新 增</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-table :data="data.tableData" style="width: 100%" :header-cell-style="{color: '#333',backgroundColor: '#eaf4ff'}">
        <el-table-column label="封面" width="300">
          <template #default="scope">
            <el-image v-if="scope.row.coverUrl" :src="scope.row.coverUrl" :preview-src-list="[scope.row.coverUrl]" :preview-teleported="true"
                      style="width: 100%; height: 10vh; display: block"/>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="课程标题"/>
        <el-table-column prop="synopsis" label="课程简介"  />
        <el-table-column prop="time" label="发布时间" />
<!--        <el-table-column label="视频" >-->
<!--          <template #default="scope">-->
<!--            <video v-if="scope.row.videoUrl" width="100" controls>-->
<!--              <source :src="scope.row.videoUrl" type="video/mp4">-->
<!--              您的浏览器不支持视频播放。-->
<!--            </video>-->
<!--          </template>-->
<!--        </el-table-column>-->
        <el-table-column label="操作" width="150">
          <template #default="scope">
            <el-button type="info" icon="View" circle @click="handleDetail(scope.row)"></el-button>
            <el-button  v-if="data.user.role === '1' " type="primary" icon="Edit" circle @click="handleEdit(scope.row)"></el-button>
            <el-button  v-if="data.user.role === '1' " type="danger" icon="Delete" circle @click="handleDelete(scope.row.id)"></el-button>
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

    <el-dialog v-model="data.formVisible" title="课程信息" width="500" destroy-on-close="">
      <el-form status-icon ref="formRef" :model="data.form" :rules="data.rules" label-width="80px" style="padding: 20px 30px 10px 0">
        <el-form-item prop="coverUrl" label="封面">
          <el-upload
              action="http://localhost:9999/files/upload"
              :headers="{ token: data.user.token }"
              :on-success="handleFileSuccess"
              list-type="picture"
          >
            <el-button type="primary">上传封面</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item prop="title" label="课程标题">
          <el-input v-model="data.form.title" autocomplete="off" placeholder="请输入课程标题"/>
        </el-form-item>
        <el-form-item prop="synopsis" label="课程简介">
          <el-input type="textarea" :rows="3" v-model="data.form.synopsis" autocomplete="off" placeholder="请输入课程简介"/>
        </el-form-item>
        <el-form-item prop="videoUrl" label="视频">
          <el-upload
              action="http://localhost:9999/course/uploadVideo"
              :headers="{ token: data.user.token }"
              :on-success="handleVideoSuccess"
              :key="data.detailData.videoUrl"
              list-type="video"
          >
            <el-button type="primary">上传视频</el-button>
          </el-upload>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="data.formVisible = false">取消</el-button>
          <el-button type="primary" @click="save">保存</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 添加详情弹窗 -->
    <el-dialog v-model="data.detailVisible" title="课程详情" width="700">
      <div style="margin-bottom: 20px">
        <h2 style="margin-bottom: 10px">{{ data.detailData.title }}</h2>
        <div style="color: #666; margin-bottom: 20px">{{ data.detailData.time }}</div>
        <el-image
            v-if="data.detailData.coverUrl"
            :src="data.detailData.coverUrl"
            style="width: 100%; height: 300px; margin-bottom: 20px"
            fit="cover"
        />
        <div style="line-height: 1.6; margin-bottom: 20px">{{ data.detailData.synopsis }}</div>
        <!-- 确保 videoUrl 是响应式绑定的完整 URL -->
        <video
            controls
            style="width:100%"
            :src="data.detailData.videoUrl"
            @error="handleVideoError"
        >
          <source :src="data.detailData.videoUrl">
        </video>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import 'video.js/dist/video-js.min.css'
import {Search} from "@element-plus/icons-vue";
import {reactive,ref} from "vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";

const formRef = ref()

const data = reactive({
  user:JSON.parse(localStorage.getItem('code_user') || "{}"),
  title: null,
  pageNum: 1,
  pageSize: 5,
  total: 0,
  tableData: [],
  form: {},
  formVisible: false,
  detailVisible: false,
  detailData: {},
  rules: {
    title: [
      { required: true,message: '请填写课程标题', trigger: 'blur' }
    ],
    synopsis: [
      { required: true,message: '请填写课程简介', trigger: 'blur' }
    ]
  }
})

const load = () => {
  request.get('/course/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      title: data.title,
    }
  }).then(res => {
    if (res.code === '200'){
      data.tableData = res.data?.list
      data.total = res.data?.total
    }else {
      ElMessage.error(res.msg)
    }
  })
}
load()

const handleAdd =() => {
  data.form = {}
  data.formVisible = true
}

const add = () => {
  request.post('/course/add',data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('新增成功')
      data.formVisible = false
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const save = () => {
  formRef.value.validate(valid => {
    if (valid) {
      data.form.id ? update() : add()
    }
  })
}

const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
}

const update = () => {
  formRef.value.validate((valid) => {
    if (valid) { //验证通过的时候
      request.put('course/update',data.form).then(res => {
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

const handleDelete = (id) => {
  ElMessageBox.confirm(
      '确定要删除数据?',
      '提示',
      {
        type: 'warning',
      }).then(res => {
    request.delete('course/delete/' + id).then(res => {
      if (res.code === '200') {
        ElMessage.success('删除成功')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {})
}

const reset = () => {
  data.title = null
  load()
}

const handleFileSuccess = (res) => {
  data.form.coverUrl = res.data
}

const handleVideoSuccess = (res) => {
  data.form.videoUrl = res.data
}

const handleDetail = (row) => {
  request
      .get(`/course/${row.id}`).then(res => {
    if (res.code === '200') {
      data.detailData = res.data
      data.detailVisible = true
    }
  })
}

const handleVideoError = (e) => {
  const video = e.target;
  console.error("视频播放失败:", video.error);
  ElMessage.error(`视频加载失败: ${video.error.message}`);
};
</script>

<style>
.el-dialog__body {
  padding: 20px;
}
</style>