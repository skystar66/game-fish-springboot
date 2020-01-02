package com.xl.game.config;

import com.xl.game.engine.ServerType;
import lombok.Data;
import org.simpleframework.xml.Element;

@Data
public class MinaClienConnToConfig extends BaseServerConfig{


    private ServerType type=ServerType.GATE;

    // 链接到服务器的地址
    private String host="127.0.0.1";

    // 链接到服务器的端口
    private int port=8500;




}
