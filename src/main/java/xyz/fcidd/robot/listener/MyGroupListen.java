package xyz.fcidd.robot.listener;

import cn.hutool.core.date.DateTime;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import love.forte.common.ioc.annotation.Beans;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.api.message.containers.GroupAccountInfo;
import love.forte.simbot.api.message.containers.GroupBotInfo;
import love.forte.simbot.api.message.containers.GroupInfo;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.Sender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.fcidd.robot.utils.mcping.MinecraftPing;
import xyz.fcidd.robot.utils.mcping.MinecraftPingOptions;
import xyz.fcidd.robot.utils.mcping.MinecraftPingReply;
import xyz.fcidd.robot.utils.url.UrlUtil;

import java.io.IOException;

/**
 * 群消息监听的示例类。
 * 所有需要被管理的类都需要标注 {@link Beans} 注解。
 *
 * @author ForteScarlet
 */
@Beans
public class MyGroupListen {
    /**
     * log
     */
    private static final Logger LOG = LoggerFactory.getLogger(MyGroupListen.class);

    /**
     * 此监听函数代表，收到消息的时候，将消息的各种信息打印出来。
     * <p>
     * 此处使用的是模板注解 {@link OnGroup}, 其代表监听一个群消息。
     * <p>
     * 由于你监听的是一个群消息，因此你可以通过 {@link GroupMsg} 作为参数来接收群消息内容。
     *
     * <p>
     * 注意！ 假如你发现你群消息发不出去（或者只有一些很短的消息能发出去）且没有任何报错，
     * 但是尝试后，发现 <b>私聊</b> 一切正常，能够发送，那么这是 <b>正常现象</b>！
     * <p>
     * 参考：
     */
    @OnGroup
    public void onGroupMsg(GroupMsg groupMsg, Sender sender) throws IOException {
        //获取机器人的信息
        GroupBotInfo botInfo = groupMsg.getBotInfo();
        //获取群信息
        GroupInfo groupInfo = groupMsg.getGroupInfo();
        //获取群成员的账号信息
        GroupAccountInfo groupAccountInfo = groupMsg.getAccountInfo();
        //获取群消息
        String groupMsgText = groupMsg.getText();
        //将接收的消息输出到控制台
        LOG.info("[↓][群][{}({})]{}({}):{}", groupInfo.getGroupName(), groupInfo.getGroupCode(), groupAccountInfo.getAccountNickname(), groupAccountInfo.getAccountCode(), groupMsgText);
        //如果群消息是"现在时间"
        if (groupMsgText.equals("现在时间")) {
            //获取系统时间
            DateTime nowTime = DateTime.now();
            //将系统时间发送到群
            sender.sendGroupMsg(groupInfo.getGroupCode(), String.valueOf(nowTime));
            //输出日志
            LOG.info("[↑][群][{}({})]{}({}):{}", groupInfo.getGroupName(), groupInfo.getGroupCode(), botInfo.getBotName(), botInfo.getBotCode(), nowTime);
        }
        //如果群消息是"fz ping"
        if (groupMsgText.equalsIgnoreCase("server ping")) {
            //尝试ping目标服务器
            MinecraftPingReply serverPing = new MinecraftPing().getPing(new MinecraftPingOptions().setHostname(""));
            //获取在线玩家的昵称
            StringBuilder player = new StringBuilder();
            serverPing.getPlayers().getSample().forEach(players -> player.append("- ").append(players.getName()).append("\n"));
            //将服务器的在线玩家发送到群
            String serverPingInfo = "当前在线：" + serverPing.getPlayers().getOnline() + "/" + serverPing.getPlayers().getMax() + "\n" +
                    "在线玩家:\n" + player;
            sender.sendGroupMsg(groupInfo.getGroupCode(), serverPingInfo);
            //输出日志
            LOG.info("[↑][群][{}({})]{}({}):{}", groupInfo.getGroupName(), groupInfo.getGroupCode(), botInfo.getBotName(), botInfo.getBotCode(), serverPingInfo);
        }
        String[] strings = groupMsgText.trim().split(" ");
        //如果输入的长度等于2并且长度1为mcuuid
        if (strings.length == 2 && strings[0].equals("mcuuid")) {
            try {
                //请求mojang api服务器
                String mcUsername = strings[1];
                String url = "https://api.mojang.com/users/profiles/minecraft/" + mcUsername;
                //将响应的内容执行json解析
                String result = new UrlUtil().getResult(url);
                JSONObject jsonObject = JSON.parseObject(result);
                //获取解析过后的uuid
                String uuid = jsonObject.getString("id");
                //将用户名和uuid发送到群
                String msg = "用户名：" + mcUsername + "\n" + "uuid：" + uuid;
                sender.sendGroupMsg(groupInfo.getGroupCode(), msg);
                //输出日志
                LOG.info("[↑][群][{}({})]{}({}):{}", groupInfo.getGroupName(), groupInfo.getGroupCode(), botInfo.getBotName(), botInfo.getBotCode(), msg);
            } catch (NullPointerException e) {
                //将提示信息发到群里
                sender.sendGroupMsg(groupInfo.getGroupCode(), "用户名不正确或者是网络超时");
                //输出日志
                LOG.info("[↑][群][{}({})]{}({}):{}", groupInfo.getGroupName(), groupInfo.getGroupCode(), botInfo.getBotName(), botInfo.getBotCode(), "用户名不正确或者是网络超时");
            }
        }
    }
}
