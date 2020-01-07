package com.xl.game.hall.handler.http;

import com.xl.game.config.MinaServerHttpConfig;
import com.xl.game.hall.manager.RoleManager;
import com.xl.game.handler.HandlerEntity;
import com.xl.game.handler.HttpHandler;
import com.xl.game.message.ServerMessage;
import com.xl.game.model.redis.key.HallKey;
import com.xl.game.redis.manager.JedisManager;
import com.xl.game.util.*;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * GM处理器，所有gm在大厅处理，捕鱼游戏等用redis发布
 * <p>
 * http://192.168.0.17:9102/gm?cmd=addGold&roleId=1&gold=10000&auth=daa0cf5b-e72d-422c-b278-450b28a702c6
 * </p>
 *
 * @author xuliang
 * @QQ 359135103
 * 2017年10月16日 下午5:07:12
 */
@HandlerEntity(path = CMDConstants.GM_CHAT_URL)
@Slf4j
public class GmHandler extends HttpHandler {


    private static final Map<String, Method> GM_METHOD = JsonUtil.getFieldMethodMap(GmHandler.class, null);

    MinaServerHttpConfig minaServerConfig;

    JedisManager jedisManager;

    @Override
    public void run() {

        if (minaServerConfig == null) {
            minaServerConfig = SpringUtil.getBean(MinaServerHttpConfig.class);
        }
        if (jedisManager == null) {
            jedisManager = SpringUtil.getBean(JedisManager.class);
        }

        String auth = getString("auth");
        if (!Config.SERVER_AUTH.equals(auth) ||
                !minaServerConfig.getIp().startsWith("192")
                || !minaServerConfig.getIp().startsWith("127")) {
            sendMsg("权限验证失败");
            return;
        }


        String result = execute();

        log.info("{}使用GM结果:{}", MsgUtil.getIp(getSession()), result);
        if (getSession() != null) {
            sendMsg(result);
        }


    }

    /**
     * 执行命令
     *
     * @return
     * @author xuliang
     * @QQ 359135103
     * 2017年10月16日 下午6:32:04
     */

    public String execute() {

        String cmd = getString("cmd");    //命令，方法名称
        if (cmd.equalsIgnoreCase("execute")) {
            return "指令错误";
        }
        String result = String.format("GM %s 执行失败", getMessage().getQueryString());
        if (GM_METHOD.containsKey(cmd.trim())) {
            Method method = GM_METHOD.get(cmd);
            try {
                result = (String) method.invoke(this);
            } catch (Exception e) {
                log.error("GM", e);
            }
        }
        return result;
    }


    /**
     * 增减金币
     *
     * @return
     * @author JiangZhiYong
     * @QQ 359135103
     * 2017年10月16日 下午5:45:05
     */
    protected String addGold() {
        long roleId = getLong("roleId");
        int gold = getInt("gold");
        String key = HallKey.Role_Map_Info.getKey(roleId);
        jedisManager.getJedisCluster().hincrBy(key, "gold", gold);
        String info = String.format("角色%d增加%d金币", roleId, gold);
        RoleManager.getInstance().publishGoldChange(roleId, gold);
        return info;
    }

    /**
     * 设置角色等级
     *
     * @return
     * @author JiangZhiYong
     * @QQ 359135103
     * 2017年10月16日 下午5:57:26
     */
    protected String setLevel() {
        long roleId = getLong("roleId");
        String level = getString("level");
        String key = HallKey.Role_Map_Info.getKey(roleId);
        jedisManager.getJedisCluster().hset(key, "level", level);
        String info = String.format("角色%d等级设为%d", roleId, level);
        //TODO 通知子游戏改变？
        return info;

    }
//
//    /**
//     * 下线玩家
//     * @author JiangZhiYong
//     * @QQ 359135103
//     * 2017年10月17日 下午4:59:54
//     * @return
//     */
//    protected String offLineRole() {
//        long roleId = getLong("roleId");
//        ServerMessage.ServerEventRequest.Builder builder=ServerMessage.ServerEventRequest.newBuilder();
//        builder.setType(1);
//        builder.setId(roleId);
//        HallServer.getInstance().getHall2GateClient().broadcastMsg(builder.build(), roleId);
//        return String.format("角色%d将被踢下线", roleId);
//    }


}
