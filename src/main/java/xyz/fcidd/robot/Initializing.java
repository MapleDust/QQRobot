package xyz.fcidd.robot;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

@Log4j2
public class Initializing {
    /*配置文件夹的位置*/
    private static final File CONFIG_FILE = new File("./config.properties");
    /*机器人配置文件的位置*/
    private static final File BOT_FILE = new File("./simbot-bots/bot1.bot");
    /*机器人配置的文件夹*/
    private static final File BOT_FOLDER = new File("./simbot-bots/");
    /*插件文件夹*/
    private static final File PLUGINS_FOLDER = new File("./plugins");
    /*读取resources目录下的bot配置文件*/
    private static final ClassPathResource BOT_PATH_RESOURCE = new ClassPathResource("simbot-bots/Bot1.bot");
    /*读取resources目录下的配置文件*/
    private static final ClassPathResource CONFIG_PATH_RESOURCE = new ClassPathResource("config.properties");
    /*输出流*/
    private static OutputStream output;
    /*输入流*/
    private static InputStream input;

    @SneakyThrows
    public static void init() {
        log.info("Application initializing....");
        byte[] data = new byte[1024 * 10];
        int len;
        //如果插件文件夹不存在
        if (!PLUGINS_FOLDER.exists()) {
            //创建插件文件夹
            PLUGINS_FOLDER.mkdir();
        }
        //如果配置文件不存在
        if (!CONFIG_FILE.exists()) {
            //输出配置文件夹
            output = new FileOutputStream(CONFIG_FILE);
            //获取字节流
            input = CONFIG_PATH_RESOURCE.getInputStream();
            //写入配置文件
            while ((len = input.read(data)) != -1) {
                output.write(data, 0, len);
            }
            log.warn("如果想正常使用mc服务器信息,请编辑config.properties输入正确的mc服务器信息");
        }
        //如果机器人配置文件不存在
        if (!BOT_FILE.exists()) {
            //创建机器人配置的文件夹
            BOT_FOLDER.mkdir();
            //输出机器人的配置文件
            output = new FileOutputStream(BOT_FILE);
            //获取字节流
            input = BOT_PATH_RESOURCE.getInputStream();
            //写入机器人配置文件内容
            while ((len = input.read(data)) != -1) {
                output.write(data, 0, len);
            }
            log.warn("请打开simbot-bots文件夹下的bot1.bot编辑机器人信息后重新运行");
            //停止运行
            throw new RuntimeException();
        }
        //输出或输入流为空会报错，这里简单的处理一下
        if (output != null || input != null) {
            input.close();
            output.close();
        }
        log.info("Application initializing successful");
    }
}
