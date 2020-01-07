package com.xl.game.hall.handler.chat;


import com.xl.game.hall.manager.RoleManager;
import com.xl.game.hall.script.IGmScript;
import com.xl.game.hall.server.HallServer;
import com.xl.game.handler.HandlerEntity;
import com.xl.game.handler.TcpHandler;
import com.xl.game.message.Mid;
import com.xl.game.message.hall.HallChatMessage;
import com.xl.game.model.struct.Role;
import com.xl.game.util.MsgUtil;
import com.xl.game.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 聊天处理 <br>
 * 消息发往网关进行转发
 *
 * @author xuliang
 * @QQ 359135103 2017年9月13日 下午3:15:49
 */
@HandlerEntity(mid = Mid.MID.ChatReq_VALUE, msg = HallChatMessage.ChatRequest.class)
@Slf4j
public class ChatHandler extends TcpHandler {


    private IGmScript gmScript;

    private HallServer hallServer;

    @Override
    public void run() {
        HallChatMessage.ChatRequest req = getMsg();
        Role role = RoleManager.getInstance().getRole(this.rid);
        if (role == null) {
            log.warn("{}-{}未登陆", rid, MsgUtil.getIp(session));
            return;
        }

        if (gmScript == null) {
            gmScript = SpringUtil.getBean(IGmScript.class);
        }

        // TODO 消息内容过滤


        if (gmScript.isGMCmd(req.getMsg())) {

            gmScript.executeGm(rid, req.getMsg());

        }

        switch (req.getChatType()) {


            case PRIVATE:
                chatPrivate(req, role);
                break;
            case WORLD:
                chatWorld(req, role);
                break;
            default:
                break;
        }


    }

    /**
     * 私聊
     *
     * @author xuliang
     * @QQ 359135103 2020年1月3日 下午3:23:41
     */

    public void chatPrivate(HallChatMessage.ChatRequest req, Role role) {
        // TODO 接收是否在线等验证
        if (req.getReceiverId() < 1) {
            log.warn("消息错误，接受者ID为：{}", req.getReceiverId());
            return;
        }


        if (hallServer == null) {

            hallServer = SpringUtil.getBean(HallServer.class);

        }


        HallChatMessage.ChatResponse.Builder builder = HallChatMessage.ChatResponse.newBuilder();

        //聊天类型
        builder.setChatType(req.getChatType());
        //发送者头像
        builder.setSenderHead(role.getHead() == null ? "" : role.getHead());
        //发送者id
        builder.setSenderId(role.getId());
        //发送者昵称
        builder.setSenderNick(role.getNick());
        //消息
        builder.setMsg(req.getMsg());


        //todo 发送消息

        hallServer.getHall2GateClient().broadcastMsg(builder.build(), req.getReceiverId());


    }


    /**
     * 世界聊天
     *
     * @param req
     * @param role
     * @author xuliang
     * @QQ 359135103 2020年1月3日 下午3:46:32
     */

    private void chatWorld(HallChatMessage.ChatRequest req, Role role) {


        if (hallServer == null) {

            hallServer = SpringUtil.getBean(HallServer.class);

        }

        HallChatMessage.ChatResponse.Builder builder = HallChatMessage.ChatResponse.newBuilder();
        builder.setChatType(req.getChatType());
        builder.setSenderHead(role.getHead() == null ? "" : role.getHead());
        builder.setSenderId(role.getId());
        builder.setSenderNick(role.getNick());
        builder.setMsg(req.getMsg());

        //todo
        hallServer.getHall2GateClient().broadcastMsg(builder.build());


    }


}
