package com.xl.game.hall.manager;


import com.xl.game.hall.script.IUserScript;
import com.xl.game.model.struct.User;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Consumer;

/**
 * 用户管理
 *
 * @author JiangZhiYong
 * @QQ 359135103 2017年7月7日 下午4:00:13
 */
@Slf4j
public class UserManager {


    private static volatile UserManager userManager;

    private IUserScript userScript;

    private UserManager() {

    }

    public static UserManager getInstance() {
        if (userManager == null) {
            synchronized (UserManager.class) {
                if (userManager == null) {
                    userManager = new UserManager();
                }
            }
        }
        return userManager;
    }

    /**
     * 创建角色
     *
     * @param userConsumer
     * @return
     */
    public User createUser(Consumer<User> userConsumer) {

        return userScript.createUser(userConsumer);
    }


}
