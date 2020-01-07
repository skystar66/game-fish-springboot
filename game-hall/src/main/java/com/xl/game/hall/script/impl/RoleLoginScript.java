package com.xl.game.hall.script.impl;

import com.xl.game.hall.manager.PacketManager;
import com.xl.game.hall.manager.RoleManager;
import com.xl.game.hall.script.IPacketScript;
import com.xl.game.hall.script.IRoleScript;
import com.xl.game.model.constants.Reason;
import com.xl.game.model.mongo.dao.RoleDao;
import com.xl.game.model.struct.Role;
import com.xl.game.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * 登陆脚本 <br>
 * 临时数据处理，如发临时发补偿，数据处理等
 *
 * @author JiangZhiYong
 * @QQ 359135103 2017年9月18日 下午6:02:29
 */
@Component(value = "roleLogin")
@Slf4j
public class RoleLoginScript implements IRoleScript {


    private IPacketScript packetScript;


    @Override
    public void login(Role role, Reason reason) {
        RoleDao.saveRole(role);
        // 设置用户session
        RoleManager.getInstance().getRoles().put(role.getId(), role);

        tempInit(role);

    }


    /**
     * 加载数据
     *
     * @param role
     * @author JiangZhiYong
     * @QQ 359135103 2017年9月18日 下午5:28:12
     */
    public void loadData(Role role) {

    }


    /**
     * 临时初始化
     *
     * @param role
     * @author JiangZhiYong
     * @QQ 359135103 2017年9月18日 下午6:30:10
     */
    private void tempInit(Role role) {

        if (packetScript == null) {
            packetScript = SpringUtil.getBean(IPacketScript.class);
        }

        // 道具
        if (role.getItemCount() < 1) {
            PacketManager.getInstance(packetScript).addItem(role, 1, 20, Reason.UserLogin, null);
        }
        //邮件
//        List<Mail> mails = MailDao.getMails(role.getId());
//        if(mails==null||mails.size()<1) {
//            MailManager.getInstance().sendMail(-1, role.getId(), "测试", "系统测试", MailType.PRIVATE, null);


    }


}
