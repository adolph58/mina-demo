package com.example.mina.client;

import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.SocketConnector;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

public class MinaClientConn {

	private static String host = "127.0.0.1";
	private static int port = 5678;
	private SocketConnector connector = null;
	private IoSession session = null;

	public MinaClientConn() {
	}

	public boolean connect() {
		try {
			connector = new NioSocketConnector(); // 提供客户端实现
			connector.setConnectTimeoutMillis(3000); // 设置超时时间
			// 设置过滤器（编码和解码）
			ProtocolCodecFilter codecFilter = new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"), LineDelimiter.WINDOWS.getValue(), LineDelimiter.WINDOWS.getValue()));
			connector.getFilterChain().addLast("codec", codecFilter);// 配置CodecFactory

			// 业务处理
			connector.setHandler(new MinaClientHandle());
			// 设置 session 属性，获取服务端连接
			ConnectFuture future = connector.connect(new InetSocketAddress(host, port));
			future.awaitUninterruptibly(); // 等待我们的连接
			session = future.getSession();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}


	public void sentMsg(String message) throws InterruptedException {
		WriteFuture wf = session.write(message);
		wf.await(3L, TimeUnit.SECONDS);
		// 发送成功，处理发送状态
	}

	boolean close() {
		CloseFuture future = session.getCloseFuture();
		future.awaitUninterruptibly(1000); // 等待关闭连接
		connector.dispose(); // 释放资源
		return true;
	}

	public SocketConnector getConnector() {
		return connector;
	}

	public IoSession getSession() {
		return session;
	}

	public static void main(String[] args) throws InterruptedException {
		MinaClientConn conn = new MinaClientConn();
		conn.connect();
		conn.sentMsg("Hello");
	}

}