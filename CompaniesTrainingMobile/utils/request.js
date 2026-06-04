const request = (config) => {
	// // 添加token
	// config.header = {
	// 	'Authorization': uni.getStorageSync('user.token')
	// }
	// 拼接完整的接口路径
	config.url = 'http://localhost:9999' + config.url;
	//这里拼接的是访问后端接口的地址，http://mm.test.cn/prod-api/test
	console.log(config.url)
	//判断是都携带参数
	if (!config.data) {
		config.data = {};
	}
	// request.js
	let promise = new Promise(function(resolve, reject) {
		uni.request(config).then(response => { // response 是单对象，不是数组
			// 1. 检查 HTTP 状态码
			if (response.statusCode !== 200) {
				reject({
					message: `请求失败（${response.statusCode}）`
				});
				return;
			}

			// 2. 获取实际数据（假设后端返回格式为 { data: {...} }）
			const data = response.data;

			// 3. 进一步检查业务状态码（根据你的接口设计）
			// 例如：if (data.code !== 200) { reject(data.message); }

			resolve(data);
		}).catch(error => {
			reject(error);
		});
	});
	return promise;
};

export default request;