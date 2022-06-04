package xyz.fcidd.robot;

import love.forte.simbot.spring.autoconfigure.EnableSimbot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 *
 * 此项目已经整合Springboot
 *
 */

@EnableSimbot
@EnableScheduling
@SpringBootApplication
public class QQRobotApplication {
    public static void main(String[] args) {
        Initializing.init();
        SpringApplication.run(QQRobotApplication.class, args);
    }
}
