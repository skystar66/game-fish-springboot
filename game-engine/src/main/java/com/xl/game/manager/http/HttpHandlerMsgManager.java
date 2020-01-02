package com.xl.game.manager.http;

import com.xl.game.handler.HandlerEntity;
import com.xl.game.handler.IHandler;
import com.xl.game.handler.TcpHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class HttpHandlerMsgManager {


    // http handler容器
    final Map<String, Class<? extends IHandler>> httpHandlerMap = new ConcurrentHashMap<>(0);
    final Map<String, HandlerEntity> httpHandlerEntityMap = new ConcurrentHashMap<>(0);


    private static final HttpHandlerMsgManager instance = new HttpHandlerMsgManager();


    public HttpHandlerMsgManager() {
    }

    public static HttpHandlerMsgManager getInstance() {
        return instance;
    }


    public Map<String, Class<? extends IHandler>> getHttpHandlerMap() {
        return httpHandlerMap;
    }

    public Map<String, HandlerEntity> getHttpHandlerEntityMap() {
        return httpHandlerEntityMap;
    }

    /**
     * 获取消息处理器
     */
    public Class<? extends IHandler> getHttpHandler(String path) {
        return getHttpHandlerMap().get(path);
    }

    /**
     * 获取handler 配置
     */
    public HandlerEntity getHandlerEntity(String path) {
        return getHttpHandlerEntityMap().get(path);
    }


    /**
     * 添加handler
     *
     * @param clazz
     * @author xuliang
     * @QQ 2755055412
     * 2017年7月24日 下午1:36:27
     */
    public void addHandler(Class<? extends IHandler> clazz) {
        if (IHandler.class.isAssignableFrom(clazz)) {
            HandlerEntity handlerEntity = clazz.getAnnotation(HandlerEntity.class);
            if (handlerEntity != null) {
                httpHandlerMap.put(handlerEntity.path(), clazz);
                httpHandlerEntityMap.put(handlerEntity.path(), handlerEntity);
                log.info("[{}]加载到http handler容器", clazz.getName());
            } else {
                log.warn("handler[{}]未添加注解", clazz.getSimpleName());
            }
        }
    }


}
