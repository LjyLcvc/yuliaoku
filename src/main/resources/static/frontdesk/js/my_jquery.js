const WEBURL = "http://112.124.19.158:8091/";
$(function() {
    //设置ajax默认的设置，即每次访问时都会为header设置token
    $.ajaxSetup({
        cache:false, //是否进行数据缓存
        //contentType:"application/json",这里不能随意加，否则会导致表单（$myForm.serialize()方式）传值时，服务端可能接收不到
        crossDomain: true,
        xhrFields: {
            withCredentials: true//要进行跨域必须设置，但在服务端也必须允许。这样可以保持登录状态
        },
        complete: function(XMLHttpRequest, status) { //请求完成后最终执行参数
            if(status == 'timeout') { //超时,status还有success,error等值的情况
                alert("请求超时");
            }
        }
    });   
});