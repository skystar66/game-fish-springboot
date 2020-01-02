package com.xl.game.gate.script.impl;

import com.xl.game.engine.ServerType;
import com.xl.game.gate.script.IGateServerScript;
import com.xl.game.gate.server.GateTcpUserServer;
import com.xl.game.script.IInitScript;
import lombok.extern.slf4j.Slf4j;
import org.apache.mina.filter.firewall.BlacklistFilter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;


/**
 * 设置IP黑名单
 * TODO 是否需要存入数据库，通过后台调用设置
 *
 * @author xuliang
 * @QQ 359135103
 * 2019年12月30日 上午11:24:58
 */
@Component(value = "ipBlackList")
@Slf4j
public class IpBlackListScript implements IGateServerScript, IInitScript {

    private List<InetAddress> blackList;


    @Override
    public void init() {
        //添加黑名单列表
        blackList = new ArrayList<>();
        try {
//			blackList.add(InetAddress.getByName("192.168.0.17"));	//TODO 测试
        } catch (Exception e) {
            log.warn("添加IP黑名单", e);
        }
        //设置用户tcp黑名单
        GateTcpUserServer.getBlacklistFilter().setBlacklist(blackList);
    }


    @Override
    public void setIpBlackList(BlacklistFilter filter) {
        filter.setBlacklist(blackList);
    }
}
