<template>
  <div class="home-container">
    <!-- 欢迎横幅 -->
    <div class="welcome-banner">
      <h1>您好，{{ data.user.name }}！欢迎使用企业培训系统 <span class="emoji">🎉</span></h1>
    </div>

    <!-- 双列布局 -->
    <el-row :gutter="20">
      <!-- 左侧功能区 -->
      <el-col :xs="24" :sm="16">
        <template v-if="data.user.role === '1'">
          <admin-dashboard/>
        </template>

        <template v-else-if="data.user.role === '2'">
          <teacher-view/>
        </template>

        <template v-else>
          <student-home/>
        </template>

<!--        课程轮播图-->
        <div class="card chart-section">
          <h3 class="section-title"><i class="el-icon-picture"></i> 最新课程封面</h3>
          <el-carousel
              height="calc(100vw * 0.3)"
              :autoplay="5000"
              trigger="hover">
            <el-carousel-item
                v-for="(course, index) in data.courseData"
                :key="index"
                style="padding: 10px;">
              <div class="carousel-container">
                <el-image
                    :src="course.coverUrl"
                    fit="cover"
                    :preview-src-list="[course.coverUrl]"
                    class="course-image">
                  <template #error>
                    <div class="image-error">
                      <i class="el-icon-picture-outline"></i>
                      <span>封面加载失败</span>
                    </div>
                  </template>
                </el-image>
                <div class="course-info">
                  <h4>{{ course.name }}</h4>
                  <p>{{ course.description }}</p>
                </div>
              </div>
            </el-carousel-item>
          </el-carousel>
        </div>
      </el-col>

      <!-- 右侧公告区 -->
      <el-col :xs="24" :sm="8">
        <div class="card notice-card">
          <div class="section-header">
            <i class="el-icon-megaphone"></i>
            <h3>系统公告</h3>
            <el-link type="primary" @click="goNoticePage" class="more-link">更多»</el-link>
          </div>

          <el-timeline>
            <el-timeline-item
                v-for="(item, index) in data.noticeData"
                :key="index"
                :timestamp="item.time"
                placement="top"
                :color="index === 0 ? '#0bbd87' : '#909399'"
                :hide-timestamp="windowWidth < 768">
              <h4 class="notice-title">{{ item.title }}</h4>
              <p class="notice-content">{{ item.content }}</p>
              <el-tag
                  v-if="item.tag"
                  :type="item.tagType || 'info'"
                  size="mini">
                {{ item.tag }}
              </el-tag>
            </el-timeline-item>
          </el-timeline>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, defineAsyncComponent,onMounted } from 'vue'
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
import {useRouter} from "vue-router";


const router = useRouter() // 获取路由实例

// 动态组件导入（按需加载）
const AdminDashboard = defineAsyncComponent(() => import('../components/AdminDashboard.vue'))
const TeacherView = defineAsyncComponent(() => import('../components/TeacherView.vue'))
const StudentHome = defineAsyncComponent(() => import('../components/StudentHome.vue'))

const data = reactive({
  user:JSON.parse(localStorage.getItem('code_user') || "{}"),
  noticeData: [],
  courseData: []
})

const loadNotice = () => {
  request.get('notice/selectAll').then(res => {
    if (res.code === '200'){
      data.noticeData = res.data
      if (data.noticeData.length > 6) {
        data.noticeData = data.noticeData.slice(0,6)
      }
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const windowWidth = ref(window.innerWidth)
onMounted(() => {
  window.addEventListener('resize', () => {
        windowWidth.value = window.innerWidth
      })
  loadNotice()
  loadCourse()
})

// 获取课程数据
const loadCourse = () => {
  request.get('/course/selectPage', {
    params: {
      pageNum: 1,
      pageSize: 3,
    }
  }).then(res => {
    if (res.code === '200') {
      data.courseData = res.data?.list
    } else {
      ElMessage.error(res.msg)
    }
  })
}


const goNoticePage = () => {
  router.push('/manager/notice')
}

loadNotice()
</script>

<style scoped>
.welcome-banner {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  color: white;
  padding: 2rem;
  border-radius: 8px;
  margin-bottom: 20px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);

  h1 {
    margin: 0;
    font-size: 1.8rem;
    .emoji {
      vertical-align: middle;
    }
  }

  .sub-text {
    opacity: 0.9;
    margin: 8px 0 0;
  }
}

.notice-card {
  .section-header {
    display: flex;
    align-items: center;
    gap: 8px;
    margin-bottom: 1.5rem;

    i {
      font-size: 1.4rem;
      color: #0bbd87;
    }

    .more-link {
      margin-left: auto;
    }
  }

  .notice-title {
    margin: 0 0 6px;
    color: #2c3e50;
  }

  .notice-content {
    font-size: 0.9em;
    color: #666;
    line-height: 1.5;
  }
}

.chart-section {
  .section-title {
    color: var(--el-color-primary);
    i {
      margin-right: 8px;
    }
  }
}

/* 轮播图样式 */
.carousel-container {
  position: relative;
  height: 100%;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);

  .course-image {
    width: 100%;
    height: 100%;
    transition: transform 0.3s;

    &:hover {
      transform: scale(1.05);
    }
  }

  .course-info {
    position: absolute;
    bottom: 0;
    left: 0;
    right: 0;
    padding: 15px;
    background: linear-gradient(transparent, rgba(0,0,0,0.7));
    color: white;

    h4 {
      margin: 0 0 5px;
      font-size: 16px;
    }

    p {
      margin: 0;
      font-size: 12px;
      opacity: 0.9;
      display: -webkit-box;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
      overflow: hidden;
    }
  }
}

/* 错误状态 */
.image-error {
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: #f5f7fa;
  color: #909399;

  i {
    font-size: 24px;
    margin-bottom: 5px;
  }
}
</style>
