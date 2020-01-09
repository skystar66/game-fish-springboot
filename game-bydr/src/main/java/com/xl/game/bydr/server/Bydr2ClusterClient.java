package com.xl.game.bydr.server;


import com.xl.game.config.MinaClientConfig;
import com.xl.game.mina.service.SingleMinaTcpClientService;

/**
 * 连接集群 tcp客户端
 * @author xuliang
 * @QQ 359135103
 * 2020年1月7日 下午4:16:19
 */
public class Bydr2ClusterClient extends SingleMinaTcpClientService {


    public Bydr2ClusterClient(MinaClientConfig minaClientConfig) {

        super(minaClientConfig);
    }
}
