package com.lcvc.guojiaoyuan.yuliaoku.web.interceptor;


import com.lcvc.guojiaoyuan.yuliaoku.model.Admin;
import com.lcvc.guojiaoyuan.yuliaoku.model.base.Constant;
import com.lcvc.guojiaoyuan.yuliaoku.model.base.JsonCode;
import com.lcvc.guojiaoyuan.yuliaoku.service.AdminService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户登陆拦截，用于拦截websocket的接口
 * 说明：
 * spring mvc的拦截器对websocket接口拦截无效，必须通过该接口来实现拦截
 */
public class LoginForAdminInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private AdminService adminService;
    //private static final String[] IGNORE_URI = {};//定义不拦截的资源

    /**
     * 在请求处理之前进行调用（Controller方法调用之前）
     * 基于URL实现的拦截器
     * 在执行action里面的处理逻辑之前执行，它返回的是boolean，这里如果我们返回true在接着执行postHandle和afterCompletion，如果我们返回false则中断执行。
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = request.getRequestURL().toString();//获取被读取的接口路径,如http://127.0.0.1:8088/ebuy_springboot/backstage/logout
        boolean flag = false;//默认验证失败，即拦截请求
        String path = request.getServletPath();
        HttpSession session=request.getSession();
        if (flag) {//对要拦截的路径进行判断（当前不需要）
            //不需要的拦截直接过
            flag=true;
        } else {
            //对账户是否登陆进行验证
            //返回错误信息
            Object adminObject=session.getAttribute("admin");
           if(adminObject==null){
                Map<String, Object> map = new HashMap<String, Object>();
                map.put(Constant.JSON_CODE, JsonCode.LOGIN.getValue());
                map.put(Constant.JSON_MESSAGE, "请先登录");
                JSONObject jsonObject= JSONObject.fromObject(map);
                //注意，必须加上这个，才能让前端JS认为是JSON格式来进行相应处理
                response.setContentType("application/json;charset=UTF-8");
                PrintWriter out = response.getWriter();
                out.print(jsonObject.toString());
                out.flush();
                out.close();
                flag=false;
            }else{//如果已经登录
               //更新账户信息，后期要改为redis，减少数据库交互次数
               Admin admin=adminService.get(((Admin)adminObject).getUsername());
               session.setAttribute("admin",admin);//更新Admin的值
               flag=true;
           }
        }
        return flag;
    }
}
