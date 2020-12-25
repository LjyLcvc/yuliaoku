package com.lcvc.guojiaoyuan.yuliaoku.web.service;

import net.sf.json.JSONObject;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 用简易聊天室来进行websocket测试
 */
//@ServerEndpoint("/api/backstage/webSocket/{sid}")
//@Component
public class WebSocketTestService {
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static AtomicInteger onlineNum = new AtomicInteger();//AtomicInteger可以保证不同现成访问的是同一个内存
    //concurrent包的线程安全，用来存放每个客户端对应的WebSocketServer对象。
    private static ConcurrentHashMap<String, Session> sessionPools = new ConcurrentHashMap<>();//ConcurrentHashMap可以保证现成安全

    //private MyService myServiceImpl = (MyService) MyApplicationContextAware.getApplicationContext().getBean("myServiceImpl");

    /**
     * 客户端与本服务地址（/webSocket/{sid}）建立连接成功调用
     */
    @OnOpen
    public void onOpen(Session session, @PathParam(value = "sid") String userName){
        sessionPools.put(userName, session);
        addOnlineCount();//在线用户+1
        System.out.println(userName + "加入webSocket！当前人数为" + onlineNum);
        try {
            sendMessage(session, "欢迎" + userName + "加入连接！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭连接时调用
     * @param userName
     */
    @OnClose
    public void onClose(@PathParam(value = "sid") String userName){
        sessionPools.remove(userName);
        subOnlineCount();
        System.out.println(userName + "断开webSocket连接！当前人数为" + onlineNum);
    }

    //收到客户端信息
    @OnMessage
    public void onMessage(String message) throws IOException{
        System.out.println("收到客户端信息：" + message);//message传递过来是JSON格式
        JSONObject jo = JSONObject.fromObject(message);
        String userName =jo.getString("toUserId");//获取客户端要发送到的对象
        String content =jo.getString("contentText");//获取客户端发送的内容
        sendMessage(userName, content);//向指定对象（客户端）发送信息
       /* for (Session session: sessionPools.values()) {//发送给所有在线用户
            try {
                sendMessage(session, message);
            } catch(Exception e){
                e.printStackTrace();
                continue;
            }
        }*/
    }

    //错误时调用
    @OnError
    public void onError(Session session, Throwable throwable){
        System.out.println("发生错误");
        throwable.printStackTrace();
    }

    //发送消息
    private void sendMessage(Session session, String message) throws IOException {
        if(session != null){
            synchronized (session) {
//                System.out.println("发送数据：" + message);
                session.getBasicRemote().sendText(message);
            }
        }
    }

    //给指定用户发送信息
    private void sendMessage(String userName, String message){
        Session session = sessionPools.get(userName);
        try {
            sendMessage(session, message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void addOnlineCount(){
        onlineNum.incrementAndGet();
    }

    private static void subOnlineCount() {
        onlineNum.decrementAndGet();
    }
}
