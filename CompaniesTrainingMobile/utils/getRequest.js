import request from '@/utils/request'
// utils/getRequest.js
const getRequest = (config) => {
  // 处理 GET 参数（兼容小程序环境）
  if (config.params) {
    const queryString = Object.keys(config.params)
      .filter(key => config.params[key] !== undefined && config.params[key] !== null)
      .map(key => {
        const value = config.params[key];
        // 手动编码（兼容小程序）
        return `${encodeURIComponent(key)}=${encodeURIComponent(value)}`;
      })
      .join('&');

    if (queryString) {
      config.url += (config.url.includes('?') ? '&' : '?') + queryString;
    }
  }

  // 固定方法为 GET
  config.method = 'GET';
  
  // 调用基础请求
  return request(config);
};

export default getRequest;