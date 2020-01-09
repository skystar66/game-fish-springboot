package com.xl.game.bydr.script;


import com.xl.game.bydr.struts.role.Role;
import com.xl.game.model.constants.Reason;

import java.util.function.Consumer;

/**
 * 角色
 * @author xuliang
 * @QQ 359135103
 * 2017年8月4日 下午2:10:47
 */
public interface IRoleScript {



    /**
     * 登录
     * @author xuliang
     * @QQ 359135103
     * 2017年8月4日 下午2:12:33
     * @param roleId
     * @param reason
     * @param roleConsumer
     */
    default void login(long roleId, Reason reason, Consumer<Role> roleConsumer){

    }

    /**
     * 退出
     * @author xuliang
     * @QQ 359135103
     * 2017年8月4日 下午2:12:40
     * @param role
     * @param reason
     */
    default void quit(Role role,Reason reason){

    }

    /**
     * 修改金币
     * @author xuliang
     * @QQ 359135103
     * 2017年9月25日 下午5:20:33
     * @param role
     * @param gold
     * @param reason
     */
    default void changeGold(Role role,int gold,Reason reason) {

    }

    /**
     * 奖金币同步大大厅
     * @author xuliang
     * @QQ 359135103
     * 2017年9月26日 上午10:40:51
     * @param role
     * @param reason
     */
    default void syncGold(Role role,Reason reason) {

    }






}
