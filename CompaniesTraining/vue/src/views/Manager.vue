<template>
  <div>
    <!-- 头部区域开始-->
    <div style="height: 60px;  display: flex;">
      <div style="width: 240px; display: flex; padding-left: 20px; background-color: #3a456b; align-items: center">
        <img style="width: 40px; height: 40px; border-radius: 50%" src="@/assets/imgs/logo.png">
        <span  style="font-size: 20px; font-weight: bold;color: #fafafa;margin-left: 5px">企业培训系统</span>
      </div>
      <div style="flex: 1; display: flex; align-items: center; padding-left: 20px; border-bottom: 1px solid #ddd;">
        <span style="margin-right: 5px; cursor: pointer" @click="router.push('/manager/home')">首页</span>/
        <span style="margin-right: 5px">{{router.currentRoute.value.meta.name}}</span>
      </div>
        <div style="width: fit-content;padding-right: 20px;display: flex;align-items: center;border-bottom: 1px solid #ddd;">
          <el-dropdown>
            <div style="display: flex;align-items: center;">
              <img v-if="data.user?.avatar" style="width: 40px;height: 40px; border-radius: 50%" :src="data.user?.avatar">
              <img v-else style="width: 40px;height: 40px; border-radius: 50%" src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png">
              <span style="margin-left: 5px">{{ data.user?.name }}</span>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="router.push('/manager/person')">个人信息</el-dropdown-item>
                <el-dropdown-item @click="router.push('/manager/updatePassword')">修改密码</el-dropdown-item>
                <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>

        </div>

    </div>
    <!-- 头部区域结束-->

    <!-- 下方区域开始-->
    <div style="display: flex">
    <!--菜单区域开始-->
      <div style="width: 240px;">
        <el-menu router :default-openeds="['1','2','3','4','5']" :default-active="router.currentRoute.value.path" style="min-height: calc(100vh - 60px)">
          <el-menu-item index="/manager/home">
            <el-icon><House /></el-icon>
            <span>首页</span>
          </el-menu-item>
          <el-sub-menu index="1">
            <template #title>
              <el-icon><img style="width: 22px;height: 22px" src="@/assets/imgs/notice.png"></img></el-icon>
              <span v-if="data.user.role === '1' ">公告管理</span>
              <span v-else>公告信息</span>
            </template>
            <el-menu-item style="font-size: 12px" index="/manager/notice">系统公告</el-menu-item>
          </el-sub-menu>

          <el-sub-menu index="2">
            <template #title>
              <el-icon><img style="width: 22px;height: 22px" src="@/assets/imgs/course.png"></img></el-icon>
              <span v-if="data.user.role === '1' ">课程管理</span>
              <span v-else>课程</span>
            </template>
            <el-menu-item style="font-size: 12px" index="/manager/course">培训课程</el-menu-item>
          </el-sub-menu>

          <el-sub-menu v-if="data.user.role === '1' " index="3">
            <template #title>
              <el-icon><img style="width: 22px;height: 22px" src="@/assets/imgs/pxjh.png"></img></el-icon>
              <span>培训管理</span>
            </template>
            <el-menu-item style="font-size: 12px" index="/manager/trainingPlan">培训计划</el-menu-item>
          </el-sub-menu>

          <el-sub-menu index="4">
            <template #title>
              <el-icon><img style="width: 22px;height: 22px" src="@/assets/imgs/tk.png"></img></el-icon>
              <span v-if="data.user.role !== '3'">测试题库管理</span>
              <span v-else >测试题</span>
            </template>
            <el-menu-item v-if="data.user.role !== '3'" style="font-size: 12px" index="/manager/question">测试题</el-menu-item>
            <el-menu-item v-if="data.user.role !== '3'" style="font-size: 12px" index="/manager/banks">测试题库</el-menu-item>
            <el-menu-item v-if="data.user.role === '3' " style="font-size: 12px" index="/manager/studentBankList">测试题</el-menu-item>
          </el-sub-menu>

          <el-sub-menu index="5" v-if="data.user.role === '1'">
            <template #title>
              <el-icon><img style="width: 22px;height: 22px" src="@/assets/imgs/user.png"></img></el-icon>
              <span>用户管理</span>
            </template>
            <el-menu-item style="font-size: 12px" index="/manager/admin">管理员信息</el-menu-item>
            <el-menu-item style="font-size: 12px" index="/manager/staffUser">用户信息</el-menu-item>
          </el-sub-menu>

<!--          导师界面-->
          <el-sub-menu index="5" v-if="data.user.role === '2'">
            <template #title>
              <el-icon><img style="width: 22px;height: 22px" src="@/assets/imgs/user.png"></img></el-icon>
              <span>学员管理</span>
            </template>
            <el-menu-item style="font-size: 12px" index="/manager/traineesMsg">学员信息</el-menu-item>
            <el-menu-item style="font-size: 12px" index="/manager/trainingPlan">培训计划</el-menu-item>
            <el-menu-item style="font-size: 12px" index="/manager/mentorTaskReview">培训完成情况</el-menu-item>
          </el-sub-menu>

<!--          学员界面-->
          <el-sub-menu index="5" v-if="data.user.role === '3'">
            <template #title>
              <el-icon><img style="width: 22px;height: 22px" src="@/assets/imgs/user.png"></img></el-icon>
              <span>我的导师</span>
            </template>
            <el-menu-item style="font-size: 12px" index="/manager/MentorMsg">导师信息</el-menu-item>
            <el-menu-item style="font-size: 12px" index="/manager/staffUserTasks">培训任务</el-menu-item>
          </el-sub-menu>
        </el-menu>
      </div>
      <!--菜单区域结束-->

      <!--数据渲染区域开始-->
      <div style="flex:1; width:0; margin: 10px; background-color: #f2f4ff">
        <RouterView @updateUser="updateUser"/>
      </div>
      <!--数据渲染区域结束-->
    </div>
    <!-- 下方区域结束-->
  </div>
</template>

<script setup>

import router from "@/router/index.js";
import {reactive} from "vue";

const data = reactive({
  user:JSON.parse(localStorage.getItem('code_user')),
})

const logout = () => {
  localStorage.removeItem('code_user')
  location.href = '/login'
}

const updateUser = () => {
  data.user = JSON.parse(localStorage.getItem("code_user") || '{}')
}

if (!data.user?.id){
  location.href = '/login'
}
</script>

<style>
.el-menu {
  background-color: #3a456b;
  border:none;
}
.el-sub-menu__title{
  color: #ddd;
  background-color: #3a456b;
}
.el-menu-item{
  height: 50px;
  color: #ddd;
}
.el-menu .is-active {
  background-color: #537bee;
  color: #fff;
}
.el-sub-menu__title:hover {
  background-color: #3a456b;
}
.el-menu-item:not(.is-active):hover {
  background-color: #7a9fff;
  color: #333;
}
.el-dropdown {
  cursor: pointer;
}
.el-tooltip__trigger {
  outline: none;
}
.el-menu--inline .el-menu-item {
  padding-left: 48px !important;
}
</style>