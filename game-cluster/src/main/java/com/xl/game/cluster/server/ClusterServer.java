package com.xl.game.cluster.server;

import com.xl.game.cluster.server.http.ClusterHttpServer;
import com.xl.game.cluster.server.tcp.ClusterTcpServer;
import lombok.extern.slf4j.Slf4j;

/**
 * 集群管理服务器
 *
 * @author xuliang
 * @date 2019-12-25 QQ:359135103
 */
@Slf4j
public class ClusterServer implements Runnable {


    private final ClusterHttpServer clusterHttpServer;
    private final ClusterTcpServer clusterTcpServer;


    public ClusterServer(ClusterHttpServer clusterHttpServer,
                         ClusterTcpServer clusterTcpServer) {
        this.clusterHttpServer = clusterHttpServer;
        this.clusterTcpServer = clusterTcpServer;
    }

    @Override
    public void run() {

        //启动mina
        log.info("ClusterServer::clusterHttpServer::start!!!");
        new Thread(clusterHttpServer).start();
        log.info("ClusterServer::clusterTcpServer::start!!!");
        new Thread(clusterTcpServer).start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            log.error("", ex);
        }
        log.info("---->集群服启动成功<----");

    }
}
