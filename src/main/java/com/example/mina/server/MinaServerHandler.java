package com.example.mina.server;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author 程江涛
 * @create 2019/5/27.
 */
@Component
public class MinaServerHandler extends IoHandlerAdapter {

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        System.out.println("exceptionCaught：" + "连接出现异常");
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        String msg = (String)message;
        System.out.println("服务端接收到数据：" + msg);
        Date date = new Date();
        session.write(date); // 用于写入数据并发送
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        System.out.println("messageSent：" + "发送数据");
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        System.out.println("sessionClosed：" + "session关闭");
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        System.out.println("sessionCreated：" + "创建Session");
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        System.out.println("sessionIdle：" + "处于多长时间是空闲状态");
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        System.out.println("sessionOpened：" + "打开Session用于读写数据");
    }

}
