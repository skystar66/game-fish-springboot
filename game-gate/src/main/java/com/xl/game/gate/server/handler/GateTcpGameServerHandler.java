package com.xl.game.gate.server.handler;

import com.xl.game.gate.manager.UserSessionManager;
import com.xl.game.gate.script.IGateServerScript;
import com.xl.game.gate.session.UserSession;
import com.xl.game.mina.handler.DefaultProtocolHandler;
import com.xl.game.server.Service;
import com.xl.game.util.MsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

@Slf4j
public class GateTcpGameServerHandler extends DefaultProtocolHandler {


    IGateServerScript iGateServerScript;

    private Service service;

    public GateTcpGameServerHandler(int messageHeaderLength, IGateServerScript iGateServerScript) {
        super(messageHeaderLength);
        this.iGateServerScript = iGateServerScript;
    }


    @Override
    protected void forward(IoSession session, int msgID, byte[] bytes) {
        long rid = MsgUtil.getMessageRID(bytes, 0);
        if (rid > 0) {
            UserSession userSession = UserSessionManager.getInstance()
                    .getUserSessionbyRoleId(rid);
            if (null != userSession) {

                //udp 转发
                if (userSession.getClientUdpSession() != null) {
                    if (iGateServerScript.isUdpMsg(userSession.getServerType(), msgID)) {
                        userSession.sendToClientUdp(Arrays.copyOfRange(bytes, 8, bytes.length));
                        return;
                    }
                }
                //tcp 转发
                userSession.sendToClient(Arrays.copyOfRange(bytes, 8, bytes.length));// 前8字节为角色ID
                return;
            }
        }
    }

    @Override
    public void sessionCreated(IoSession ioSession) throws Exception {
        log.info("网关创建子游戏连接");
        super.sessionCreated(ioSession);
    }

    @Override
    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }


}
