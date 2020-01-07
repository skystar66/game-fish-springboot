package com.xl.game.hall.script;


import com.xl.game.model.constants.Reason;
import com.xl.game.model.struct.Role;

import java.util.function.Consumer;

/**
 * 玩家脚本
 *
 * @author xuliang
 * @date 2017-03-30 QQ:359135103
 */
public interface IRoleScript {


    /**
     * 登录游戏
     */

    default void login(Role role, Reason reason) {
    }


    /**
     * 创建角色
     *
     * @param userId
     * @return
     */

    default Role createRole(long userId, Consumer<Role> roleConsumer) {
        return null;
    }


    /**
     * 角色退出游戏
     * @author xuliang
     * @QQ 359135103
     * 2017年9月18日 下午6:01:08
     * @param role
     */
    default void quit(Role role,Reason reason) {

    }




}
