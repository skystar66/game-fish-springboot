package com.xl.game.mina;


import com.xl.game.config.MinaServerTcpConfig;
import com.xl.game.mina.code.DefaultProtocolCodecFactory;
import com.xl.game.mina.code.ProtocolCodecFactoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.executor.OrderedThreadPoolExecutor;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;

/**
 * TCP服务器
 *
 * @author xuliang
 * @date 2019-12-06 QQ:2755055412
 */
@Slf4j
public class TcpServer implements Runnable {


    private MinaServerTcpConfig minaServerConfig;

    private NioSocketAcceptor acceptor;

    private IoHandler ioHandler;

    private ProtocolCodecFactory factory;

    //消息处理线程，使用有序线程池，保证所有session消息线程的处理有序性，
    // 比如先执行消息，在执行发送消息，
    //最后关闭事件
    private OrderedThreadPoolExecutor orderedThreadPoolExecutor;

    private Map<String, IoFilter> filters; // 过滤器

    protected boolean isRunning; // 服务器是否运行


    /**
     * @param minaServerConfig 配置
     * @param ioHandler        消息处理器
     */
    public TcpServer(MinaServerTcpConfig minaServerConfig, IoHandler ioHandler) {
        this.minaServerConfig = minaServerConfig;
        this.ioHandler = ioHandler;
        acceptor = new NioSocketAcceptor();
    }

    /**
     * @param minaServerConfig 配置
     * @param ioHandler        消息处理器
     */
    public TcpServer(MinaServerTcpConfig minaServerConfig, IoHandler ioHandler, Map<String, IoFilter> filters) {
        this.minaServerConfig = minaServerConfig;
        this.ioHandler = ioHandler;
        acceptor = new NioSocketAcceptor();
        this.filters = filters;
    }

    public TcpServer(MinaServerTcpConfig minaServerConfig, IoHandler ioHandler, ProtocolCodecFactory factory) {
        this(minaServerConfig, ioHandler);
        this.factory = factory;
    }


    /**
     * @param minaServerConfig
     * @param ioHandler
     * @param factory
     * @param filters          不要包含消息解码、线程池过滤器，已默认添加
     */
    public TcpServer(MinaServerTcpConfig minaServerConfig, IoHandler ioHandler, ProtocolCodecFactory factory,
                     Map<String, IoFilter> filters) {
        this(minaServerConfig, ioHandler, factory);
        this.filters = filters;
    }


    /**
     * 广播所有连接的消息
     */
    public void broadcastMsg(Object message) {
        acceptor.broadcast(message);
    }


    @Override
    public void run() {

        synchronized (this) {
            //校验该服务器是否正在运行
            if (!isRunning) {
                isRunning = true;
                DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
                if (factory == null) {
                    //使用默认的消息编解码器
                    factory = new DefaultProtocolCodecFactory();
                }

                if (factory instanceof DefaultProtocolCodecFactory) {
                    ProtocolCodecFactoryImpl defaultFactory = (ProtocolCodecFactoryImpl) factory;
                    //设置解码器 最大消息长度
                    defaultFactory.getDecoder().setMaxReadSize(minaServerConfig.getMaxReadSize());
                    defaultFactory.getEncoder().setMaxScheduledWriteMessages(minaServerConfig.getMaxScheduledWriteMessages());
                }
                //设置过滤器
                chain.addLast("codec", new ProtocolCodecFilter(factory));
                orderedThreadPoolExecutor = new OrderedThreadPoolExecutor(minaServerConfig.getOrderedThreadPoolExecutorSize());
                //设置处理session 的线程池模型
                chain.addLast("threadPool", new ExecutorFilter(orderedThreadPoolExecutor));
                //设置过滤器 ssl等认证
                if (filters != null) {
                    filters.forEach((key, filter) -> {
                        //添加SSL 认证等 ssl过滤器必须添加到首部
                        if ("ssl".equalsIgnoreCase(key) || "tls".equalsIgnoreCase(key)) {
                            chain.addFirst(key, filter);
                        } else {
                            chain.addLast(key, filter);
                        }
                    });
                }

                acceptor.setReuseAddress(minaServerConfig.isReuseAddress()); // 允许地址重用
                SocketSessionConfig sc = acceptor.getSessionConfig();
                sc.setReuseAddress(minaServerConfig.isReuseAddress());
                sc.setReceiveBufferSize(minaServerConfig.getReceiveBufferSize());
                sc.setSendBufferSize(minaServerConfig.getSendBufferSize());
                sc.setTcpNoDelay(minaServerConfig.isTcpNoDelay());
                sc.setSoLinger(minaServerConfig.getSoLinger());
                sc.setIdleTime(IdleStatus.READER_IDLE, minaServerConfig.getReaderIdleTime());
                sc.setIdleTime(IdleStatus.WRITER_IDLE, minaServerConfig.getWriterIdleTime());

                acceptor.setHandler(ioHandler);
                try {
                    acceptor.bind(new InetSocketAddress(minaServerConfig.getPort()));
                    log.warn("已开始监听TCP端口：{}", minaServerConfig.getPort());
                } catch (IOException e) {
                    log.warn("监听TCP端口：{}已被占用", minaServerConfig.getPort());
                    log.error("TCP 服务异常", e);
                }
            }
        }
    }


    /**
     * 关服
     */

    public void stop() {
        synchronized (this) {
            if (!isRunning) {
                log.info("Server " + minaServerConfig.getName() + "is already stoped.");
                return;
            }
            isRunning = false;
            try {

                if (orderedThreadPoolExecutor != null) {
                    orderedThreadPoolExecutor.shutdown();
                }

                acceptor.unbind();
                acceptor.dispose();
                log.info("Server is stoped.");
            } catch (Exception ex) {
                log.error("", ex);
            }
        }
    }


}
