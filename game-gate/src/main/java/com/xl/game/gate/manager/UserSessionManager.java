package com.xl.game.gate.manager;


import com.google.protobuf.Message;
import com.xl.game.gate.script.IUserScript;
import com.xl.game.gate.session.UserSession;
import com.xl.game.model.constants.Reason;
import lombok.extern.slf4j.Slf4j;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户连接会话管理类
 *
 * @author xuliang
 * @QQ 359135103 2019年12月30日 下午3:58:31
 */
@Slf4j
public class UserSessionManager {


    private static volatile UserSessionManager userSessionManager;


    @Autowired
    IUserScript iUserScript;

    /**
     * 用户session key：sessionID
     */
    private final Map<Long, UserSession> allSessions = new ConcurrentHashMap<>();

    /**
     * 用户session key：userID
     */
    private final Map<Long, UserSession> userSessions = new ConcurrentHashMap<>();

    /**
     * 用户session key：roleID
     */
    private final Map<Long, UserSession> roleSessions = new ConcurrentHashMap<>();


    private UserSessionManager() {
    }

    public static UserSessionManager getInstance() {
        if (userSessionManager == null) {
            synchronized (UserSessionManager.class) {
                if (userSessionManager == null) {
                    userSessionManager = new UserSessionManager();
                }
            }
        }
        return userSessionManager;
    }


    /**
     * 用户连接服务器
     *
     * @param userSession
     */

    public void onUserConnected(UserSession userSession) {
        if (userSession.getClientSession() != null) {
            log.info("session id : {}",userSession.getClientSession().getId());
            allSessions.put(userSession.getClientSession().getId(), userSession);
        }
    }

    /**
     * 登录大厅
     *
     * @param userSession
     */
    public void loginHallSuccess(UserSession userSession, long userId, long roleId) {
        userSession.setUserId(userId);
        userSession.setRoleId(roleId);
        userSessions.put(userId, userSession);
        roleSessions.put(roleId, userSession);
    }

    public UserSession getUserSessionByUserId(long userId) {
        return userSessions.get(userId);
    }

    public UserSession getUserSessionbyRoleId(long roleId) {
        return roleSessions.get(roleId);
    }


    /**
     * 用户session
     *
     * @param sessionId
     * @return
     * @author xuliang
     * @QQ 359135103 2019年12月21日 下午1:49:17
     */
    public UserSession getUserSessionBySessionId(long sessionId) {
        return allSessions.get(sessionId);
    }


    public void quit(UserSession userSession, Reason reason) {
        allSessions.remove(userSession.getClientSession().getId());
        userSessions.remove(userSession.getUserId());
        roleSessions.remove(userSession.getRoleId());
        log.info("用户退出，原因：{}", reason.getReason());
    }

    /**
     * 广播消息给前端客户端
     *
     * @param msg
     * @author xuliang
     * @QQ 359135103
     * 2019年12月30日 下午3:58:17
     */

    public void broadcast(Message msg) {
        allSessions.values().forEach(userSessions -> {
            userSessions.getClientSession().write(msg);
        });
    }


    /**
     * 所有在线人数
     *
     * @return
     * @author JiangZhiYong
     * @QQ 359135103
     * 2017年10月12日 下午1:36:58
     */
    public int getOlineCount() {
        return allSessions.size();
    }


    /**
     * 服务器关闭
     *
     * @author xuliang
     * @QQ 359135103
     * 2019年12月30日 下午1:49:14
     */
    public void onShutdown() {
        allSessions.values().forEach(userSessions -> {
            iUserScript.quit(userSessions.getClientSession(), Reason.ServerClose);
        });
    }


}
