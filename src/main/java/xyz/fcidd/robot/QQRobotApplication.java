package xyz.fcidd.robot;

import love.forte.simbot.annotation.SimbotApplication;
import love.forte.simbot.core.SimbotApp;

/**
 * simbot 启动类。
 * <p>
 * 此处的注解配置了两个配置文件：
 * <ul>
 *     <li>simbot.yml</li>
 * </ul>
 * 其中，{@code simbot-dev.yml} 是一个测试环境的配置文件，只有当启动参数中存在 {@code --Sdev} 的时候才会被使用。
 * 如果你不需要一些特殊的配置文件，那么可以直接使用 {@code @SimbotApplication}.
 * <p>
 * 默认情况下，默认的配置文件名称为 {@code simbot.yml} 或 {@code simbot.properties}
 */
@SimbotApplication
public class QQRobotApplication {
    public static void main(String[] args) {
        SimbotApp.run(QQRobotApplication.class, args);
    }
}
