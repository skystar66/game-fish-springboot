package com.xl.game.cluster;

import com.xl.game.cluster.init.HandlerInit;
import com.xl.game.cluster.server.ClusterServer;
import com.xl.game.cluster.server.http.ClusterHttpServer;
import com.xl.game.cluster.server.tcp.ClusterTcpServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication(exclude =
        {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class},
        scanBasePackages = {"com.xl.game.cluster","com.xl.game.config"})
public class ClusterApplication implements CommandLineRunner {


    @Autowired
    ClusterHttpServer clusterHttpServer;
    @Autowired
    ClusterTcpServer clusterTcpServer;

    public static void main(String[] args) {
        SpringApplication.run(ClusterApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        Thread thread = new Thread(new HandlerInit());
        thread.setDaemon(true);
        thread.start();
        Thread thread1 = new Thread(new ClusterServer(clusterHttpServer, clusterTcpServer));
        thread1.setDaemon(true);
        thread1.start();
    }
}
