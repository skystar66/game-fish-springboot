package com.xl.game.gate;

import com.xl.game.config.MinaClientConfig;
import com.xl.game.config.MongoClientConfig;
import com.xl.game.gate.manager.MongoManager;
import com.xl.game.gate.server.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;


@SpringBootApplication(exclude =
        {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class},
        scanBasePackages = {"com.xl.game.gate", "com.xl.game.config"})
public class GateApplication implements CommandLineRunner {


    @Autowired
    Server server;





    public static void main(String[] args) {
        SpringApplication.run(GateApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {

        server.start();

    }
}
