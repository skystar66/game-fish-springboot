package com.xl.game.gate.server.client;


import com.xl.game.config.MinaClientConfig;
import com.xl.game.mina.code.ProtocolCodecFactoryImpl;
import com.xl.game.mina.service.SingleMinaTcpClientService;
import lombok.extern.slf4j.Slf4j;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.service.IoHandler;
import org.springframework.cache.annotation.Cacheable;

import java.util.Map;

/**
 * 连接到集群管理
 *
 * @author xuliang
 * @date 2017-04-05 QQ:359135103
 */
@Slf4j
public class Gate2ClusterClient extends SingleMinaTcpClientService {


    public Gate2ClusterClient(MinaClientConfig minaClientConfig) {
        super(minaClientConfig);
    }
}
