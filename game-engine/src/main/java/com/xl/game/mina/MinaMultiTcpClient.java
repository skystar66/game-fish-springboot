package com.xl.game.mina;


import com.xl.game.config.MinaClientConfig;
import com.xl.game.mina.service.MinaClientService;
import lombok.extern.slf4j.Slf4j;
import org.apache.mina.core.service.IoHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * 多客戶端管理,连接多个服务器
 *
 * @author xuliang
 * @date 2020-01-02 QQ:359135103
 */
@Slf4j
public class MinaMultiTcpClient {

    /**
     * 客户端列表 key：服务器ID
     */
    public final Map<Integer, MinaTcpClient> tcpClients = new HashMap<>();

    /**
     * 添加客户端
     *
     * @param service
     * @param config
     * @param clientProtocolHandler
     */
    public void addTcpClient(MinaClientService service, MinaClientConfig config, IoHandler clientProtocolHandler) {
        MinaTcpClient tcpClient = null;
        if (tcpClients.containsKey(config.getId())) {
            tcpClient = tcpClients.get(config.getId());
            tcpClient.setMinaClientConfig(config);
            return;
        }
        tcpClient = new MinaTcpClient(service, config, clientProtocolHandler);
        tcpClients.put(config.getId(), tcpClient);
    }

    /**
     * 移除客户端
     *
     * @param id
     */
    public void removeTcpClient(Integer id) {
        tcpClients.remove(id);
    }


    /**
     * 校验客户端是否存在
     *
     * @param id
     */
    public boolean containsKey(Integer id) {
        return tcpClients.containsKey(id);
    }

    /**
     * 向服务器发送数据
     *
     * @param sid 客户端ID
     * @param obj
     * @return
     */
    public boolean sendMsg(Integer sid, Object obj) {
        MinaTcpClient tcpClient = null;
        if (tcpClients == null) {
            log.info("客户端列表为空！");
            return false;
        }
        if (!tcpClients.containsKey(sid)) {
            log.info("服务id：{} ，不存在！", sid);
            return false;
        }
        tcpClient = tcpClients.get(sid);
        if (tcpClient == null) {
            return false;
        }
        tcpClient.getService().sendMsg(obj);
        return true;
    }


    /**
     * 状态监测
     */
    public void checkStatus() {
        tcpClients.values().forEach(c -> c.checkStatus());
    }


}
