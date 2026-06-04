<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input clearable style="width: 260px; margin-right: 5px" v-model="data.username" placeholder="请输入账号查询" :prefix-icon="Search"></el-input>
      <el-input clearable style="width: 260px; margin-right: 5px" v-model="data.name" placeholder="请输入名称查询" :prefix-icon="Search"></el-input>
      <el-button type="primary" @click="load">查询</el-button>
      <el-button @click="reset">重置</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-table :data="data.tableData" style="width: 100%"
                :header-cell-style="{color: '#333',backgroundColor: '#eaf4ff'}">
        <el-table-column type="selection" width="55"/>
        <el-table-column label="头像" width="100">
          <template #default="scope">
            <el-image v-if="scope.row.avatar" :src="scope.row.avatar" :preview-src-list="[scope.row.avatar]" :preview-teleported="true"
                      style="width: 40px; height: 40px; border-radius: 50%; display: block"/>
          </template>
        </el-table-column>
        <el-table-column prop="username" label="账号"/>
        <el-table-column prop="name" label="名称"  />
        <el-table-column prop="phone" label="电话" />
        <el-table-column prop="email" label="邮箱" />
        <el-table-column label="身份" width="100">
          <template #default="scope">
            <el-tag v-if="scope.row.role === '2'" type="warning" effect="dark">企业导师</el-tag>
            <el-tag v-else-if="scope.row.role === '3'" type="primary" effect="dark">企业员工</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100" >
          <template #default="scope">
            <el-button type="primary" icon="Edit" circle @click="handleEdit(scope.row)"></el-button>
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

    <el-dialog v-model="data.formVisible" title="学员信息" width="500" destroy-on-close="">
      <el-form status-icon ref="formRef" :model="data.form" label-width="80px" style="padding: 20px 30px 10px 0">
        <el-form-item prop="username" label="账号">
          <el-input v-model="data.form.username" autocomplete="off" disabled/>
        </el-form-item>
        <el-form-item prop="name" label="名称">
          <el-input v-model="data.form.name" autocomplete="off" disabled/>
        </el-form-item>
        <el-form-item prop="phone" label="电话">
          <el-input v-model="data.form.phone" autocomplete="off" disabled/>
        </el-form-item>
        <el-form-item prop="email" label="邮箱">
          <el-input v-model="data.form.email" autocomplete="off" disabled/>
        </el-form-item>
        <el-form-item prop="role" label="身份" >
          <el-tag v-if="data.form.role === '2'" type="warning" effect="dark" size="large">企业导师</el-tag>
          <el-tag v-else-if="data.form.role === '3'" type="primary" effect="dark" size="large">企业员工</el-tag>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="data.formVisible = false">关闭</el-button>
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
  rows: [],
  ids: []

})

const formRef = ref()

const load = () => {
  request.get('/staffUser/selectPage',{
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
  data.username = null
  data.name = null
  load()
}

const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
}

load()


</script>