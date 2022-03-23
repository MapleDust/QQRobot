package xyz.fcidd.robot.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Configuration
@PropertySource(value = {"file:./config.properties"})

@Data
@Component
public class RobotConfig {
    @Value("${robot.mc.server.ping.ip}")
    private String mcServerPingIp;

    @Value("${robot.mc.server.ping.port}")
    private Integer mcServerPingPort;

    @Value("${robot.random.image.cd}")
    private long randomImageCd;

    @Value("${robot.random.image.api}")
    private String randomImageApi;
}
