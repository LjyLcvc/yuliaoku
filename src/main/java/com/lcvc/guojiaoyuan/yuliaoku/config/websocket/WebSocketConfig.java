package com.lcvc.guojiaoyuan.yuliaoku.config.websocket;

import com.lcvc.guojiaoyuan.yuliaoku.web.interceptor.handshakeinterceptor.LoginForAdminHandshakeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * 开启WebSocket支持
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Autowired
    private LoginForAdminHandshakeInterceptor loginForAdminHandshakeInterceptor;

    @Bean
    public MyWebSocketHandler myHandler() {
        return new MyWebSocketHandler();
    }


   @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry
                //添加myHandler消息处理对象，和websocket访问地址
                .addHandler(myHandler(), "/api/backstage/webSocket/{sid}")
                //设置允许跨域访问
                .setAllowedOrigins("*")
                //添加拦截器可实现用户链接前进行权限校验等操作
                .addInterceptors(loginForAdminHandshakeInterceptor);
    }



    /**
     * ServerEndpointExporter 作用
     * 这个Bean会自动注册使用@ServerEndpoint注解声明的websocket endpoint
     * 如果不进行配置，将无法扫描工程内的websocket配置
     * @return
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

}
