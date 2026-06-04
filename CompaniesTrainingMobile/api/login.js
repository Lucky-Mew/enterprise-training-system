import request from '@/utils/request'
 
export function login(data) {
  	return request({
  		method: "post", // 请求方式
  		url: '/login', // 请求的url
  		data: data ,// 参数
  	})
  }