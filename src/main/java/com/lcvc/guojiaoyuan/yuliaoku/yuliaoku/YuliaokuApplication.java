package com.lcvc.guojiaoyuan.yuliaoku.yuliaoku;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement // 如果mybatis中service实现类中加入事务注解，需要此处添加该注解。开启注解事务管理，等同于xml配置方式的 <tx:annotation-driven>
@MapperScan("com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.dao")//与dao层的接口@Mapper二选一写上即可(主要作用是扫包)，扫描的是mapper.xml中namespace指向值的包位置
public class YuliaokuApplication {

    public static void main(String[] args) {
        SpringApplication.run(YuliaokuApplication.class, args);
    }

//    /**
//     * http重定向到https
//     * @return
//     */
//    @Bean
//    public ServletWebServerFactory servletContainer() {
//        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
//            @Override
//            protected void postProcessContext(Context context) {
//                SecurityConstraint securityConstraint = new SecurityConstraint();
//                securityConstraint.setUserConstraint("CONFIDENTIAL");
//                SecurityCollection collection = new SecurityCollection();
//                collection.addPattern("/*");
//                securityConstraint.addCollection(collection);
//                context.addConstraint(securityConstraint);
//            }
//        };
//        tomcat.addAdditionalTomcatConnectors(httpConnector());
//        return tomcat;
//    }
//
//    @Bean
//    public Connector httpConnector() {
//        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
//        connector.setScheme("http");
//        //Connector监听的http的端口号
//        connector.setPort(8092);
//        connector.setSecure(false);
//        //监听到http的端口号后转向到的https的端口号
//        connector.setRedirectPort(8091);
//        return connector;
//    }

}
