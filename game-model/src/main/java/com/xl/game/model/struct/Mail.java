package com.xl.game.model.struct;

import com.xl.game.util.Config;
import lombok.Data;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;

import java.util.Date;
import java.util.Set;

/**
 * 邮件 <br>
 * 直接操作mongodb数据库
 * 
 * @author JiangZhiYong
 * @QQ 359135103 2017年9月18日 下午2:19:59
 */
@Entity(value = "mail", noClassnameStored = true)
@Data
public class Mail {

	/** ID */
	@Id
	@Indexed
	private long id;

	/** 发送者Id */
	private long senderId;

	/** 接收者 */
	@Indexed
	private long receiverId;

	/** 标题 */
	private String title;

	/** 内容 */
	private String content;

	/** 状态 {@link MailState} */
	private int state;

	/** 邮件类型 {@link MailType} */
	private int type;

	/** 创建时间 */
	private Date createTime;

	/** 删除时间 */
	private Date deleteTime;

	/** 已阅读，领取的玩家Id ，为系统邮件时使用 */
	private Set<Long> acquiredRoles;

	public Mail() {
        id = Config.getId();
	}

	/**
	 * 邮件状态
	 * <br>优先级依次递增
	 * @author JiangZhiYong
	 * @QQ 359135103 2017年9月21日 下午3:12:11
	 */
	public enum MailState {
		/** 未读新邮件 */
		NEW,
		/** 已读邮件 */
		READ,
		/** 领取物品 */
		GET_ITEMS,
		/** 删除 */
		DELETE
	}

	/**
	 * 邮件类型
	 * 
	 * @author JiangZhiYong
	 * @QQ 359135103 2017年9月21日 下午3:14:51
	 */
	public enum MailType {
		/** 私人邮件 */
		PRIVATE,
		/** 公共系统邮件，全服只存储一封 */
		PUBLIC_SYSTEM,
	}
}
