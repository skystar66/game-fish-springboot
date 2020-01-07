package com.xl.game.hall.server;


import com.xl.game.config.MinaClientConfig;
import com.xl.game.mina.service.SingleMinaTcpClientService;
import lombok.extern.slf4j.Slf4j;

/**
 * 连接集群服 tcp客户端
 *
 * @author xuliang
 * @QQ 2755055412
 * 2020年1月2日 下午4:16:19
 */

@Slf4j
public class Hall2ClusterClient extends SingleMinaTcpClientService {


    public Hall2ClusterClient(MinaClientConfig minaClientConfig) {
        super(minaClientConfig);
    }

}
