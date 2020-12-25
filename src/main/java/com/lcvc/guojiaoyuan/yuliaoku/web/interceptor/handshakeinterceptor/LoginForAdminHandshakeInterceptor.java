package com.lcvc.guojiaoyuan.yuliaoku.web.interceptor.handshakeinterceptor;

import com.lcvc.guojiaoyuan.yuliaoku.model.Admin;
import com.lcvc.guojiaoyuan.yuliaoku.model.base.Constant;
import com.lcvc.guojiaoyuan.yuliaoku.model.base.JsonCode;
import com.lcvc.guojiaoyuan.yuliaoku.service.AdminService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Component
public class LoginForAdminHandshakeInterceptor implements HandshakeInterceptor {
    @Autowired
    private AdminService adminService;

    /**
     * websocket握手之前执行，若返回false，则不建立链接 *
     * @param request
     * @param response
     * @param wsHandler
     * @param attributes
     * @return
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse
            response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception{
        boolean flag = false;//默认验证失败，即拦截请求
        //将request对象进行强制转换
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest httpRequest=requestAttributes.getRequest();
        // 尝试获取到当前Websocket链接的HttpSession
        HttpSession httpSession = (HttpSession)httpRequest.getSession();
        Object adminObject=httpSession.getAttribute("admin");
        attributes.put("sid","123");
        if(adminObject==null){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(Constant.JSON_CODE, JsonCode.LOGIN.getValue());
            map.put(Constant.JSON_MESSAGE, "请先登录");
            JSONObject jsonObject= JSONObject.fromObject(map);
            //注意，必须加上这个，才能让前端JS认为是JSON格式来进行相应处理
            HttpServletResponse httpResponse=requestAttributes.getResponse();
            httpResponse.setContentType("application/json;charset=UTF-8");
            PrintWriter out = httpResponse.getWriter();
            out.print(jsonObject.toString());
            out.flush();
            out.close();
            flag=false;
        }else{//如果已经登录
            //更新账户信息，后期要改为redis，减少数据库交互次数
            Admin admin=adminService.get(((Admin)adminObject).getUsername());
            httpSession.setAttribute("admin",admin);//更新Admin的值
            flag=true;
        }
        return true;
    }

    /**
     * 在握手之后执行该方法. 无论是否握手成功都指明了响应状态码和相应头.
     * @param request
     * @param response
     * @param wsHandler
     * @param exception
     */
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse
            response, WebSocketHandler wsHandler, Exception exception) {
        System.out.println("握手成功啦。。。。。。");
    }
}
