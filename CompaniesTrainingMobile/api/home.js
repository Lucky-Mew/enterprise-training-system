import request from '@/utils/request'

// 轮播图接口
export function selectAll(data) {
	var token = data.token
  	return request({
  		method: "get", // 请求方式
  		url: '/course/selectAll', // 请求的url
  		params: data ,// 参数
		header: {token},
	
  	})
  }
  
  // 公告接口
  export function selectNotice(data) {
  	var token = data.token
  	console.log(22222222222222)
  	console.log(data)
  	console.log(token)
    	return request({
    		method: "get", // 请求方式
    		url: '/notice/selectAll', // 请求的url
    		params: data ,// 参数
  		header: {token},
  	
    	})
    }