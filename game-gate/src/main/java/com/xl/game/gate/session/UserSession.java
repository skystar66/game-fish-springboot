package com.xl.game.gate.session;


import com.xl.game.engine.ServerType;
import com.xl.game.gate.manager.UserSessionManager;
import com.xl.game.message.IDMessage;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.mina.core.session.IoSession;

/**
 * 用户连接会话
 *
 * @author xuliang
 * @QQ 359135103 2019年12月30日 下午2:13:54
 */
@Data
@Slf4j
public class UserSession {


    /**用户id*/
    private long userId;

    /**角色id*/
    private long roleId;

    /**游戏前端用户session*/
    private IoSession clientSession;

    /**游戏前端udp会话*/
    private IoSession clientUdpSession;

    /** 当前连接的子游戏 session */
    private IoSession gameSession;

    /** 当前连接大厅session */
    private IoSession hallSession;

    /** 登录的游戏类型 */
    private ServerType serverType;

    /** 登录的游戏ID */
    private int serverId;


    /** 登录的大厅ID */
    private int hallServerId;

    /**客户端使用服务器的版本号*/
    private String version;



    public UserSession(IoSession clientSession) {
        this.clientSession = clientSession;
        UserSessionManager.getInstance().onUserConnected(this);
    }

    /**
     * 发送给游戏客户端
     *
     * @param msg
     * @return
     */
    public boolean sendToClient(Object msg) {
        try {
            if (clientSession != null && clientSession.isConnected()) {
                clientSession.write(msg);
                return true;
            }
        } catch (Exception e) {
            log.error("sendToClient:", e);
        }
        return false;
    }

    /**
     * 发送给游戏客户端,udp
     *
     * @param msg
     * @return
     */
    public boolean sendToClientUdp(Object msg) {
        try {
            if (getClientUdpSession() != null && getClientUdpSession().isConnected()) {
                // LOGGER.debug(" bytes:{}", msg);
                getClientUdpSession().write(msg);
                return true;
            }
        } catch (Exception e) {
            log.error("sendToClientUdp:", e);
        }
        return false;
    }

    /**
     * 发送给游戏客户端
     *
     * @param msg
     * @return
     */
    public boolean sendToGame(Object msg) {
        try {
            if (getUserId() < 1) {
                return false;
            }
            if (getGameSession() != null && getGameSession().isConnected()) {
                IDMessage idMessage = new IDMessage(getGameSession(), msg, roleId < 1 ? userId : roleId);
                idMessage.run();
                return true;
            }
        } catch (Exception e) {
            log.error("sendToGame:", e);
        }
        return false;
    }

    /**
     * 发送给大厅游戏客户端
     *
     * @param msg
     * @return
     */
    public boolean sendToHall(Object msg) {
        try {
            if (getUserId() < 1) {
                return false;
            }

            if (getHallSession() != null && getHallSession().isConnected()) {
                IDMessage idMessage = new IDMessage(getHallSession(), msg, roleId < 1 ? userId : roleId);
                idMessage.run();
                return true;
            }
        } catch (Exception e) {
            log.error("sendToGame:", e);
        }
        return false;
    }


    /**
     * 移除游戏连接状态
     * @author JiangZhiYong
     * @QQ 359135103
     * 2017年7月27日 上午9:41:13
     */
    public void removeGame(){
        setGameSession(null);
        setServerId(0);
        setServerType(null);
    }

    public void removeHall(){
        hallSession =null;
        hallServerId =0;
    }


}
