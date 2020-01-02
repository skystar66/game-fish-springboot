package com.xl.game.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;


/***
 * todo 邮件收发配置
 *
 * @author xuliang
 * @QQ 2755055412 2019年12月06日 下午5:10:19
 */
@ConfigurationProperties(prefix = "mail")
@Data
@Configuration
public class MailConfig {


    /**
     * 协议地址
     */
    private String mailSmtpHost = "smtp.qq.com";

    /**
     * ssl
     */
    private String mailSmtpSslEnable = "false";

    /**
     * 验证
     */
    private String mailSmtpAuth = "true";

    /**
     * 邮件发送账号
     */
    //private String sendUser = "dyfservermail@dyfgame.com";
    private String sendUser = "2755055412@qq.com";

    /**
     * 邮件密码
     */
    //private String password = "Dyf1qaz2wsx";
    private String password = "Jzy3591";

    /**
     * 协议地址
     */
    private List<String> reciveUser = Arrays.asList("2432271225@qq.com");


}
