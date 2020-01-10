package com.xl.game.server;

import com.xl.game.thread.ExecutorPool;
import com.xl.game.thread.ServerThread;
import com.xl.game.thread.ThreadType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public abstract class Service implements Runnable {

    /**
     * 线程容器
     */
    public final Map<ThreadType, Executor> serverThreads = new ConcurrentHashMap<>();


    @Override
    public void run() {
        //将线程加载到关闭程序时 调用的shutDownHook 方法中，用于关闭线程操作
        Runtime.getRuntime().addShutdownHook(new Thread(new CloseByExit(this)));
        //初始化线程
        initThread();
        //运行线程
        running();
    }

    /**
     * 初始化线程
     */
    protected void initThread() {
    }

    /**
     * 运行中
     */
    protected abstract void running();


    /**
     * 关闭程序时回调
     */

    public void onShutDown() {
        serverThreads.values().forEach(executor -> {

            try {
                if (null != executor) {
                    if (executor instanceof ServerThread) {
                        //全局同步线程池
                        if (((ServerThread) executor).isAlive()) {
                            ((ServerThread) executor).stop(true);
                        }
                    } else if (executor instanceof ThreadPoolExecutor) {
                        //处理IO线程池
                        if (!((ThreadPoolExecutor) executor).isShutdown()) {
                            ((ThreadPoolExecutor) executor).shutdown();

                            while (!((ThreadPoolExecutor) executor).awaitTermination(5, TimeUnit.SECONDS)) {
                                log.error("线程池剩余线程:" + ((ThreadPoolExecutor) executor).getActiveCount());
                            }

                        }
                    } else if (executor instanceof ExecutorPool) {
                        //自定义线程
                        ((ExecutorPool) executor).stop();
                    }
                }
            } catch (Exception ex) {
                log.error("关闭线程,出现异常", ex);
            }
        });
    }


    /**
     * 获得线程
     *
     * @param threadType
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends Executor> T getExecutor(ThreadType threadType) {

        if (MapUtils.isEmpty(serverThreads)) {
            return null;
        }
        if (serverThreads.get(threadType) == null) {
            return null;
        }
        return (T) serverThreads.get(threadType);
    }


    public Map<ThreadType, Executor> getServerThreads() {
        return serverThreads;
    }

    private static final class CloseByExit implements Runnable {
        private final Service service;

        public CloseByExit(Service service) {
            this.service = service;
        }

        @Override
        public void run() {
            service.onShutDown();
        }
    }


}
