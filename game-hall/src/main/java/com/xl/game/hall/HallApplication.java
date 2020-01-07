package com.xl.game.hall;

import com.xl.game.hall.server.HallServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;


@SpringBootApplication(exclude =
        {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class},
        scanBasePackages = {"com.xl.game.hall", "com.xl.game","com.xl.game.model"})
public class HallApplication implements CommandLineRunner {


    @Autowired
    HallServer server;

    public static void main(String[] args) {
        SpringApplication.run(HallApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {

        server.start();

    }
}
