package com.xl.game.hall.handler.login;

import com.xl.game.hall.manager.RoleManager;
import com.xl.game.handler.HandlerEntity;
import com.xl.game.handler.TcpHandler;
import com.xl.game.message.Mid;
import com.xl.game.message.hall.HallLoginMessage;
import com.xl.game.model.constants.Reason;
import lombok.extern.slf4j.Slf4j;


/**
 * 退出游戏
 * @author xuliang
 * @QQ 359135103
 * 2020年1月2日 上午9:49:28
 */
@HandlerEntity(mid=Mid.MID.QuitReq_VALUE,msg=HallLoginMessage.QuitRequest.class)
@Slf4j
public class QuitHandler extends TcpHandler {


    @Override
    public void run() {

        HallLoginMessage.QuitRequest req=getMsg();

        RoleManager.getInstance().quit(this.rid, Reason.UserQuit);

        HallLoginMessage.QuitResponse.Builder builder=HallLoginMessage.QuitResponse.newBuilder();
        builder.setResult(0);
        sendIdMsg(builder.build());




    }
}
