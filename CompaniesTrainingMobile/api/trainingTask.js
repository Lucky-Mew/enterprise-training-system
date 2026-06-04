import request from '@/utils/request'
import getRequest from '@/utils/getRequest.js'

// 假设后端接口为 /trainingTask/getTasksPage（根据实际接口调整）
export function getTrainingTasks(params) {
  return getRequest({
    url: '/trainingTask/getTasksPage',
    params: {
      pageNum: params.pageNum,
      pageSize: params.pageSize,
      title: params.title || '',
      token: params.token, // 携带token
    },
  })
}

// trainingTask.js 新增审核接口
export function auditTrainingTask(params) {
  return request({
    url: '/trainingTask/audit',
    method: 'post',
    data: {
      taskId: params.taskId,
      status: params.status,
      token: params.token
    }
  });
}