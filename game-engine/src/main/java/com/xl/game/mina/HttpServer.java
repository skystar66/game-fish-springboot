package com.xl.game.mina;

import com.xl.game.config.MinaServerHttpConfig;
import com.xl.game.config.MinaServerTcpConfig;
import com.xl.game.handler.HttpServerIoHandler;
import com.xl.game.mina.code.HttpServerCodecImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.executor.OrderedThreadPoolExecutor;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.net.InetSocketAddress;

@Slf4j
public class HttpServer implements Runnable {


    private final MinaServerHttpConfig minaServerConfig;
    private final NioSocketAcceptor acceptor;

    private final HttpServerIoHandler httpServerIoHandler;

    protected boolean isRunning;    //通信是否在运行

    private OrderedThreadPoolExecutor threadpool;    //默认线程池

    public HttpServer(MinaServerHttpConfig minaServerConfig, HttpServerIoHandler httpServerIoHandler) {
        this.minaServerConfig = minaServerConfig;
        this.httpServerIoHandler = httpServerIoHandler;
        acceptor = new NioSocketAcceptor();
    }


    @Override
    public void run() {

        synchronized (this) {

            if (!isRunning) {
                isRunning = true;
                new Thread(new BindServer()).start();
            }

        }
    }


    public void stop() {


        synchronized (this) {
            if (!isRunning) {
                log.info("HttpServer " + minaServerConfig.getName() + "is already stoped.");
                return;
            }

            try {

                if (threadpool != null) {
                    threadpool.shutdown();
                }
                acceptor.unbind();
                acceptor.dispose();

                log.info("HHTP Server is stoped.");
            } catch (Exception ec) {
                log.error("HHTP Server error", ec);
            }
        }
    }


    /**
     * 绑定端口
     *
     * @author xuliang
     * @date 2019-12-20
     * QQ:2755055412
     */

    public class BindServer implements Runnable {


        @Override
        public void run() {


            DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();


            chain.addFirst("codec", new HttpServerCodecImpl());
            //线程池队列
            OrderedThreadPoolExecutor threadpool = new OrderedThreadPoolExecutor(minaServerConfig.getOrderedThreadPoolExecutorSize());
            chain.addLast("threadPool", new ExecutorFilter(threadpool));
            acceptor.setReuseAddress(minaServerConfig.isReuseAddress()); // 允许地址重用
            SocketSessionConfig sc = acceptor.getSessionConfig();
            sc.setReuseAddress(minaServerConfig.isReuseAddress());
            sc.setReceiveBufferSize(minaServerConfig.getMaxReadSize());
            sc.setSendBufferSize(minaServerConfig.getSendBufferSize());
            sc.setTcpNoDelay(minaServerConfig.isTcpNoDelay());
            sc.setSoLinger(minaServerConfig.getSoLinger());
            sc.setIdleTime(IdleStatus.READER_IDLE, minaServerConfig.getReaderIdleTime());
            sc.setIdleTime(IdleStatus.WRITER_IDLE, minaServerConfig.getWriterIdleTime());
            //设置消息处理器
            acceptor.setHandler(httpServerIoHandler);

            try {

                acceptor.bind(new InetSocketAddress(minaServerConfig.getHttpPort()));
                log.warn("已开始监听HTTP端口：{}", minaServerConfig.getHttpPort());

            } catch (Exception ex) {
                log.error("监听HTTP端口 发生异常：{}", ex);
            }
        }
    }


}
