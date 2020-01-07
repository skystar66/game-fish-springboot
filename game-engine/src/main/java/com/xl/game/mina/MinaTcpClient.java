package com.xl.game.mina;


import com.xl.game.config.MinaClienConnToConfig;
import com.xl.game.config.MinaClientConfig;
import com.xl.game.mina.code.DefaultProtocolCodecFactory;
import com.xl.game.mina.code.ProtocolCodecFactoryImpl;
import com.xl.game.mina.service.MinaClientService;
import lombok.extern.slf4j.Slf4j;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Mina客户端
 *
 * @author xuliang
 * @date 2017-04-01
 * QQ:359135103
 */
@Slf4j
public class MinaTcpClient implements Runnable {


    private NioSocketConnector connector;        //TCP连接
    private MinaClientConfig minaClientConfig;            //客户端配置
    private final IoHandler clientProtocolHandler;        //消息处理器

    private final ProtocolCodecFilter codecFilter;        //消息过滤器
    private int maxConnectCount;                        //最大连接数
    private Consumer<MinaClientConfig> sessionCreateCallBack;

    private final MinaClientService service;    //附属的客户端服务
    private final ProtocolCodecFactoryImpl factory;        //消息工厂
    private Map<String, IoFilter> filters; //过滤器

    public MinaTcpClient(MinaClientService service,
                         MinaClientConfig minaClientConfig,
                         IoHandler clientProtocolHandler,
                         ProtocolCodecFactoryImpl factory,
                         Map<String, IoFilter> filters) {
        this.service = service;
        this.minaClientConfig = minaClientConfig;
        this.clientProtocolHandler = clientProtocolHandler;
        this.factory = factory;
        this.filters = filters;
        codecFilter = new ProtocolCodecFilter(factory);
        init(clientProtocolHandler);
        setMinaClientConfig(minaClientConfig);
    }


    public MinaTcpClient(MinaClientService service, MinaClientConfig minaClientConfig, IoHandler clientProtocolHandler, ProtocolCodecFactoryImpl factory) {
        this.factory = factory;
        codecFilter = new ProtocolCodecFilter(factory);
        this.service = service;
        this.clientProtocolHandler = clientProtocolHandler;
        init(clientProtocolHandler);
        setMinaClientConfig(minaClientConfig);
    }

    /**
     * 使用默认消息解码工厂
     *
     * @param service
     * @param minaClientConfig
     * @param clientProtocolHandler
     */
    public MinaTcpClient(MinaClientService service, MinaClientConfig minaClientConfig, IoHandler clientProtocolHandler) {
        factory = new DefaultProtocolCodecFactory();
        codecFilter = new ProtocolCodecFilter(factory);
        this.service = service;
        this.clientProtocolHandler = clientProtocolHandler;
        init(clientProtocolHandler);
        setMinaClientConfig(minaClientConfig);
    }

    /**
     * 广播所有连接的消息
     *
     * @param obj
     */
    public void broadcastMsg(Object obj) {
        connector.broadcast(obj);
    }


    /**
     * 初始化tcp连接
     *
     * @param clientProtocolHandler
     */
    public void init(IoHandler clientProtocolHandler) {
        connector = new NioSocketConnector();

        DefaultIoFilterChainBuilder filterChainBuilder = connector.getFilterChain();
        filterChainBuilder.addLast("codec", codecFilter);
        if (filters != null) {
            filters.forEach((key, filter) -> {
                //ssl过滤器必须添加到首部
                if ("ssl".equalsIgnoreCase(key) || "tls".equalsIgnoreCase(key)) {
                    filterChainBuilder.addFirst(key, filter);
                } else {
                    filterChainBuilder.addLast(key, filter);
                }
            });
        }
        connector.setHandler(clientProtocolHandler);
        connector.setConnectTimeoutMillis(6000l);
        connector.setConnectTimeoutCheckInterval(10000);
    }


    /**
     * 设置连接配置
     *
     * @param minaClientConfig
     */
    public void setMinaClientConfig(MinaClientConfig minaClientConfig) {
        if (minaClientConfig == null) {
            return;
        }
        this.minaClientConfig = minaClientConfig;
        SocketSessionConfig sc = connector.getSessionConfig();
        maxConnectCount = minaClientConfig.getMaxConnectCount();
        sc.setReceiveBufferSize(minaClientConfig.getReceiveBufferSize()); // 524288
        sc.setSendBufferSize(minaClientConfig.getSendBufferSize()); // 1048576
        sc.setMaxReadBufferSize(minaClientConfig.getMaxReadSize()); // 1048576
        factory.getDecoder().setMaxReadSize(minaClientConfig.getMaxReadSize());
        sc.setSoLinger(minaClientConfig.getSoLinger()); // 0
    }


    /**
     * 连接服务器
     */
    public void connect() {


        log.info("开始连接到其他服务器,共：[{}] 个", minaClientConfig.getMaxConnectCount());

        MinaClienConnToConfig minaClienConnToConfig = minaClientConfig.getConnTo();
        if (minaClienConnToConfig == null) {
            log.info("没有连接配置");
            return;
        }
        for (int i = 0; i < minaClientConfig.getMaxConnectCount(); i++) {

            ConnectFuture future = connector.connect(new InetSocketAddress(
                    minaClienConnToConfig.getHost(), minaClienConnToConfig.getPort()
            ));

            future.awaitUninterruptibly(10000L);
            if (!future.isConnected()) {
                log.info("连接到服务器失败：" + minaClienConnToConfig);
                break;
            } else {
                log.info("连接服务器成功！ 目标类型 ：{} ", minaClienConnToConfig);
            }
        }
    }

    @Override
    public void run() {
        synchronized (this) {
            connect();
        }
    }

    /**状态监测*/
    public void checkStatus(){

        if (connector.getManagedSessionCount() < maxConnectCount ||
                connector.getManagedSessions().size() < maxConnectCount) {
            connect();
        }
    }

    public MinaClientService getService() {
        return service;
    }
}
