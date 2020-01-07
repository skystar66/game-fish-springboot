package com.xl.game.hall.script.impl;

import com.xl.game.hall.script.IUserScript;
import com.xl.game.model.mongo.dao.HallInfoDao;
import com.xl.game.model.struct.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
@Slf4j
public class CreateUserScript implements IUserScript {


    @Override
    public User createUser(Consumer<User> userConsumer) {
        User user = new User();
        user.setId(HallInfoDao.getUserId());
        if (userConsumer != null) {
            userConsumer.accept(user);
        }
        return user;
    }
}
