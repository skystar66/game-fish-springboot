package com.xl.game.manager.tcp;

import com.xl.game.handler.HandlerEntity;
import com.xl.game.handler.IHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class TcpHandlerMsgManager {


    // tcp handler容器
    final Map<Integer, Class<? extends IHandler>> tcpHandlerMap = new ConcurrentHashMap<>(0);
    final Map<Integer, HandlerEntity> tcpHandlerEntityMap = new ConcurrentHashMap<>(0);


    private static final TcpHandlerMsgManager instance = new TcpHandlerMsgManager();


    public TcpHandlerMsgManager() {
    }

    public static TcpHandlerMsgManager getInstance() {
        return instance;
    }


    public Map<Integer, Class<? extends IHandler>> getTcpHandlerMap() {
        return tcpHandlerMap;
    }

    /**
     * 获取消息处理器
     *
     * @param mid 消息ID
     * @return
     */
    public Class<? extends IHandler> getTcpHandler(int mid) {
        return tcpHandlerMap.get(mid);
    }

    /**
     * tcp消息是否已经注册
     *
     * @param mid 消息ID
     * @return
     */
    public boolean tcpMsgIsRegister(int mid) {
        return tcpHandlerMap.containsKey(mid);
    }


    /**
     * 获取handler配置
     *
     * @param mid
     * @return
     */
    public HandlerEntity getTcpHandlerEntity(int mid) {
        return tcpHandlerEntityMap.get(mid);
    }


    /**
     * 添加handler
     *
     * @param clazz
     * @author JiangZhiYong
     * @QQ 359135103
     * 2017年7月24日 下午1:36:27
     */
    public void addHandler(Class<? extends IHandler> clazz) {
        if (IHandler.class.isAssignableFrom(clazz)) {
            HandlerEntity handlerEntity = clazz.getAnnotation(HandlerEntity.class);
            if (handlerEntity != null) {
                tcpHandlerMap.put(handlerEntity.mid(), clazz);
                tcpHandlerEntityMap.put(handlerEntity.mid(), handlerEntity);
                log.info("[{}]加载到tcp handler容器", clazz.getName());
            } else {
                log.info("handler[{}]未添加注解", clazz.getSimpleName());
            }
        }
    }

}
