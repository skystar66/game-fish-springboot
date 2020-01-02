package com.xl.game.gate.script.impl;

import com.xl.game.engine.ServerType;
import com.xl.game.gate.script.IGateServerScript;
import com.xl.game.message.Mid;
import com.xl.game.script.IInitScript;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component(value = "udpMsgRegister")
public class UdpMsgRegisterScript implements IGateServerScript,IInitScript {

    //udp 支持的消息
    private Set<Integer> udpMsgIds = new HashSet<>();

    //udp支持的服务器
    private Set<ServerType> udpServers = new HashSet<>();


    @Override
    public void init() {
        //注册udp游戏
        udpServers.add(ServerType.GAME_BYDR);


        // 注册udp消息,只需要注册返回消息
        udpMsgIds.add(Mid.MID.HeartRes_VALUE);
        udpMsgIds.add(Mid.MID.EnterRoomRes_VALUE);
        udpMsgIds.add(Mid.MID.ChatRes_VALUE);


    }

    @Override
    public boolean isUdpMsg(ServerType serverType, int msgId) {
        if (serverType == null) {
            return false;
        }
        return udpServers.contains(serverType) && udpMsgIds.contains(msgId);
    }
}
