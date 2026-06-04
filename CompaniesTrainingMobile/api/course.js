// src/api/course.js
import request from '@/utils/getRequest.js'

/**
 * 分页查询课程列表
 * @param {Object} params 包含分页参数和搜索条件
 * @param {Number} params.pageNum - 当前页码
 * @param {Number} params.pageSize - 每页数量
 * @param {String} params.title - 搜索标题（可选）
 * @param {String} token - 认证token
 */
export function getCoursePage(params) {
	console.log("params.title",params.title)
  return request({
    url: '/course/selectPage',
    params: {
      pageNum: params.pageNum,
      pageSize: params.pageSize,
      title: params.title || '',
	  token: params.token,// 关键：编码 token
    },
    
  })
}

/**
 * 获取课程详情
 * @param {Number} id 课程ID
 * @param {String} token - 认证token
 */
export function getCourseDetail(id, token) {
  return request({
    method: 'get',
    url: `/course/${id}`,
    header: { token }
  })
}