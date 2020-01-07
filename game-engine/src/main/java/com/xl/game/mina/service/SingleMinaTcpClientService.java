package com.xl.game.mina.service;

import com.xl.game.config.MinaClientConfig;
import com.xl.game.mina.MinaTcpClient;
import com.xl.game.mina.code.ProtocolCodecFactoryImpl;
import com.xl.game.mina.handler.DefaultClientProtocolHandler;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.service.IoHandler;

import java.util.Map;

public class SingleMinaTcpClientService extends MinaClientService {


    private MinaTcpClient tcpClient;




    public SingleMinaTcpClientService(MinaClientConfig minaClientConfig,
                                      ProtocolCodecFactoryImpl factory,
                                      IoHandler ioHandler,
                                      Map<String, IoFilter> filters) {
        super(minaClientConfig);
        tcpClient = new MinaTcpClient(this,  minaClientConfig, ioHandler,factory,filters);
    }


    /**
     * 默认消息处理器
     *
     * @param minaClientConfig
     */
    public SingleMinaTcpClientService(MinaClientConfig minaClientConfig) {
        super(minaClientConfig);
        tcpClient = new MinaTcpClient(this, minaClientConfig, new DefaultClientProtocolHandler(this));
    }


    @Override
    public void checkStatus() {
        tcpClient.checkStatus();
    }

    @Override
    protected void running() {
        tcpClient.run();
    }
}
