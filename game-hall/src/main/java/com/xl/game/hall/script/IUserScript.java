package com.xl.game.hall.script;


import com.xl.game.model.struct.User;

import java.util.function.Consumer;

/**
 * 用户接口
 *
 * @author xuliang
 * @QQ 359135103 2017年7月7日 下午4:16:57
 */

public interface IUserScript {


    /**
     * 创建用户
     *
     * @param userConsumer
     * @return
     */
    public default User createUser(Consumer<User> userConsumer) {
        return null;
    }




}
