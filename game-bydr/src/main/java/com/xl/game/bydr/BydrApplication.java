package com.xl.game.bydr;

import com.xl.game.bydr.server.BydrServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;


@SpringBootApplication(exclude =
        {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class},
        scanBasePackages = {"com.xl.game.bydr", "com.xl.game", "com.xl.game.model"})
public class BydrApplication implements CommandLineRunner {


    @Autowired
    BydrServer bydrServer;

    public static void main(String[] args) {
        SpringApplication.run(BydrApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        bydrServer.start();
    }
}
