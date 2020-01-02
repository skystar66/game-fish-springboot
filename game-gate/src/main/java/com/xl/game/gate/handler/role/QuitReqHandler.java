package com.xl.game.gate.handler.role;


import com.xl.game.gate.script.IUserScript;
import com.xl.game.handler.HandlerEntity;
import com.xl.game.handler.TcpHandler;
import com.xl.game.message.Mid;
import com.xl.game.message.hall.HallLoginMessage;
import com.xl.game.model.constants.Reason;
import com.xl.game.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 退出游戏
 * @author xuliang
 * @QQ 359135103
 * 2017年7月26日 下午5:27:23
 */
@HandlerEntity(mid=Mid.MID.QuitReq_VALUE,msg=HallLoginMessage.QuitRequest.class)
@Slf4j
public class QuitReqHandler extends TcpHandler {
    IUserScript iUserScript ;

    @Override
    public void run() {

        log.info("退出游戏处理器。。。。。。");

        if (iUserScript == null) {
            iUserScript=SpringUtil.getBean(IUserScript.class);
        }
        iUserScript.quit(session,Reason.UserQuit);

    }
}
