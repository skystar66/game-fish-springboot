package com.xl.game.hall.handler.guild;

import com.xl.game.handler.HandlerEntity;
import com.xl.game.handler.TcpHandler;
import com.xl.game.message.Mid;
import com.xl.game.message.hall.HallGuildMessage;
import com.xl.game.model.mongo.dao.GuildDao;
import com.xl.game.model.struct.Guild;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;


/**
 * 创建公会
 *
 * @author xulaing
 * @QQ 359135103
 * 2017年9月22日 上午11:51:15
 */
@HandlerEntity(mid = Mid.MID.CreateGuildReq_VALUE, msg = HallGuildMessage.CreateGuildRequest.class)
@Slf4j
public class CreateGuildHandler extends TcpHandler {


    @Override
    public void run() {

        HallGuildMessage.CreateGuildRequest req = getMsg();
        Guild guild = new Guild();
        guild.setMasterId(rid);
        guild.getMembers().add(rid);
        guild.setCreateTime(new Date());
        GuildDao.saveGuild(guild);


    }
}
