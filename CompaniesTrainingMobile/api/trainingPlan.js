import request from '@/utils/request'
import getRequest from '@/utils/getRequest.js'

export function getTrainingPlans(params) {
  return getRequest({
    url: '/trainingPlan/selectPage',
    params: {
      pageNum: params.pageNum,
      pageSize: params.pageSize,
      title: params.title || '',
      token: params.token,// 关键：编码 token
    },
  })
}


export function getStuMsg(params) {
	console.log(params.role)
  	return getRequest({
  		url: '/staffUser/selectAll', // 请求的url
  		params: {
			role: params.role,
			token: params.token,
		} ,// 参数
	
  	})
  }


