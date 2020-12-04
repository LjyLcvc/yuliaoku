axios = axios.create({
  baseURL: 'http://182.92.128.70:8081',//设置基本的请求路径，这样在实例中就不比在写
  withCredentials:true//设置保存cookie
});
var cartNumber=0;//用于记录保存的数量