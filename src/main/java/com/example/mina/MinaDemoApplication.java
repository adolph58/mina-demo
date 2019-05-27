package com.example.mina;

import com.example.mina.server.MinaServerHandler;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

@SpringBootApplication
public class MinaDemoApplication {

	private final int PORT = 5678;

	public static void main(String[] args) {
		SpringApplication.run(MinaDemoApplication.class, args);
	}

	@Bean
	public IoAcceptor ioAcceptor() throws Exception {
		IoAcceptor acceptor = new NioSocketAcceptor();
		acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"), LineDelimiter.WINDOWS.getValue(), LineDelimiter.WINDOWS.getValue())));
		// 设置缓冲区
		acceptor.getSessionConfig().setReadBufferSize(1024); // 大小
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 100); // 时间 100S
		acceptor.setHandler(new MinaServerHandler());
		acceptor.bind(new InetSocketAddress(PORT));
		System.out.println("服务器在端口：" + PORT + "已经启动");
		return acceptor;
	}

}
