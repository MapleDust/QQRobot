package xyz.fcidd.robot;

import love.forte.simbot.spring.autoconfigure.EnableSimbot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *
 * 此项目已经整合Springboot
 *
 */
@EnableSimbot
@SpringBootApplication
public class QQRobotApplication {
    public static void main(String[] args) {
        SpringApplication.run(QQRobotApplication.class, args);
    }
}
