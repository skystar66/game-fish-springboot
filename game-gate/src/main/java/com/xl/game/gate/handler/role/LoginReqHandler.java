package com.xl.game.gate.handler.role;


import com.xl.game.engine.ServerInfo;
import com.xl.game.engine.ServerType;
import com.xl.game.gate.config.MinaServerTcpUserConfig;
import com.xl.game.gate.manager.ServerManager;
import com.xl.game.gate.manager.UserSessionManager;
import com.xl.game.gate.session.UserSession;
import com.xl.game.handler.HandlerEntity;
import com.xl.game.handler.TcpHandler;
import com.xl.game.message.IDMessage;
import com.xl.game.message.Mid;
import com.xl.game.message.hall.HallLoginMessage;
import com.xl.game.message.system.SystemMessage;
import com.xl.game.util.MsgUtil;
import com.xl.game.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.mina.core.session.IoSession;

/**
 * 登录请求
 * <p>
 * 保存用户session 设置UserSession 大厅ID，大厅session<br>
 * TODO 重连处理？？？？
 * </p>
 *
 * @author xuliang
 * @QQ 359135103 2017年7月21日 下午1:31:55
 */
@HandlerEntity(mid = Mid.MID.LoginReq_VALUE, desc = "登陆", msg = HallLoginMessage.LoginRequest.class)
@Slf4j
public class LoginReqHandler extends TcpHandler {

    private  MinaServerTcpUserConfig minaServerTcpUserConfig;

    @Override
    public void run() {
        log.info("登录请求处理器。。。。。。");
        HallLoginMessage.LoginRequest request = getMsg();
        UserSession userSession = UserSessionManager.getInstance().getUserSessionBySessionId(session.getId());
        if (userSession == null) {
            session.write(
                    ServerManager.getInstance().buildSystemErrorResponse(SystemMessage.SystemErroCode.ConectReset, "连接会话已失效，请重连"));
            log.warn("连接会话已失效，请重连");
            return;
        }

        ServerInfo serverInfo = ServerManager.getInstance().getIdleGameServer(ServerType.HALL, userSession);
        if (serverInfo == null) {
            SystemMessage.SystemErrorResponse.Builder sysBuilder = SystemMessage.SystemErrorResponse.newBuilder();
            sysBuilder.setErrorCode(SystemMessage.SystemErroCode.HallNotFind);
            sysBuilder.setMsg("未开启大厅服");
            getSession().write(sysBuilder.build());
            log.warn("大厅服不可用");
            return;
        }
        IoSession hallSession = serverInfo.getMostIdleIoSession();

        HallLoginMessage.LoginRequest.Builder builder = request.toBuilder();
        builder.setSessionId(session.getId());
        builder.setIp(MsgUtil.getIp(session));


        if (minaServerTcpUserConfig == null) {
            minaServerTcpUserConfig = SpringUtil.getBean(MinaServerTcpUserConfig.class);
        }
        //todo 稍后添加
        builder.setGateId(minaServerTcpUserConfig.getId());
        if (hallSession == null) {
            log.warn("大厅服务器未准备就绪");
            session.write(ServerManager.getInstance().buildSystemErrorResponse(SystemMessage.SystemErroCode.HallNotFind, "没可用大厅服"));
            return;
        }

        userSession.setHallServerId(serverInfo.getId());
        userSession.setHallSession(hallSession);
        userSession.setVersion(request.getVersion());

        IDMessage idMessage = new IDMessage(hallSession, builder.build(), 0);
        idMessage.run();
    }
}
