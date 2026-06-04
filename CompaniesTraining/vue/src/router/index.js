import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', redirect: '/manager/home'},
    { path: '/manager', component: () => import('../views/Manager.vue'),
      children: [
        {path: 'home',meta: {name: '主页'},component: () => import('../views/Home.vue'),},
        {path: 'admin',meta: {name: '管理员信息'},component: () => import('../views/Admin.vue'),},
        {path: 'staffUser',meta: {name: '用户信息'},component: () =>import('../views/StaffUser.vue'),},
        {path: 'person',meta: {name: '个人信息'},component: () =>import('../views/Person.vue'),},
        {path: 'updatePassword',meta: {name: '修改密码'},component: () =>import('../views/UpdatePassword.vue'),},
        {path: 'notice',meta: {name: '系统公告'},component: () =>import('../views/Notice.vue'),},
        {path: 'mentorMsg',meta: {name: '导师信息'},component: () =>import('../views/MentorMsg.vue'),},
        {path: 'traineesMsg',meta: {name: '学员信息'},component: () =>import('../views/TraineesMsg.vue'),},
        {path: 'course',meta: {name: '课程信息'},component: () =>import('../views/Course.vue'),},
        {path: 'trainingPlan',meta: {name: '培训计划管理'},component: () =>import('../views/TrainingPlan.vue'),},
        {path: 'staffUserTasks',meta: {name: '我的培训任务'},component: () =>import('../views/StaffUserTasks.vue'),},
        {path: 'mentorTaskReview',meta: {name: '培训完成情况'},component: () =>import('../views/MentorTaskReview.vue'),},
        {path: 'question',meta: {name: '测试题管理'},component: () =>import('../views/Question.vue'),},
        {path: 'banks',meta: {name: '测试题库管理'},component: () =>import('../views/QuestionBankManage.vue'),},
        {path: 'studentBankList',meta: {name: '测试题'},component: () =>import('../views/StudentBankList.vue'),},
      ]
    },
    { path: '/login', component: import('../views/Login.vue'),},
    { path: '/register', component: import('../views/register.vue'),},
    { path: '/notFound', component: import('../views/404.vue'),},
    { path: '/:pathMatch(.*)', redirect: '/notFound'}
  ],
})

export default router
