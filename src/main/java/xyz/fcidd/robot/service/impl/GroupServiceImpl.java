package xyz.fcidd.robot.service.impl;

import cn.hutool.core.date.DateTime;
import com.alibaba.fastjson.JSON;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import love.forte.simbot.api.sender.Sender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.fcidd.robot.config.RobotConfig;
import xyz.fcidd.robot.dto.GroupDTO;
import xyz.fcidd.robot.service.GroupService;
import xyz.fcidd.robot.utils.mcping.MinecraftPing;
import xyz.fcidd.robot.utils.mcping.MinecraftPingOptions;
import xyz.fcidd.robot.utils.mcping.MinecraftPingReply;
import xyz.fcidd.robot.utils.url.UrlUtil;
import xyz.fcidd.robot.utils.uuid.UUIDUtils;

import java.util.HashMap;

@Service
@Log4j2
public class GroupServiceImpl implements GroupService {
    @Autowired
    private RobotConfig robotConfig;

    /**
     * 将群消息输出到控制台
     *
     * @param groupDTO 群信息
     */
    @Override
    public void groupMsg(GroupDTO groupDTO) {
        //获取群名
        String groupName = groupDTO.getGroupName();
        //获取群号
        String groupCode = groupDTO.getGroupCode();
        //获取群成员的账号昵称
        String groupAccountNickname = groupDTO.getGroupAccountNickname();
        //获取群成员的QQ号
        String groupAccountCode = groupDTO.getGroupAccountCode();
        //获取群消息
        String groupMsgText = groupDTO.getGroupMsgText();
        //输出到控制台
        log.info("[↓][群][{}({})]{}({}):{}", groupName, groupCode, groupAccountNickname, groupAccountCode, groupMsgText);
    }

    /**
     * 发送现在时间并输出到控制台
     *
     * @param groupDTO 群信息
     */
    @Override
    public void nowTime(GroupDTO groupDTO) {
        String groupMsgText = groupDTO.getGroupMsgText();
        Sender sender = groupDTO.getSender();
        String groupCode = groupDTO.getGroupCode();
        String groupName = groupDTO.getGroupName();
        String botName = groupDTO.getBotName();
        String botCode = groupDTO.getBotCode();
        //如果群消息是"现在时间"
        if (groupMsgText.equals("现在时间")) {
            //获取系统时间
            DateTime nowTime = DateTime.now();
            //将系统时间发送到群
            sender.sendGroupMsg(groupCode, String.valueOf(nowTime));
            //输出日志
            log.info("[↑][群][{}({})]{}({}):{}", groupName, groupCode, botName, botCode, nowTime);
        }
    }

    /**
     * 查询mc玩家的uuid并发送至群和输出到控制台
     *
     * @param groupDTO 群信息
     */
    @Override
    public void mcUuid(GroupDTO groupDTO) {
        //获取群消息
        String groupMsgText = groupDTO.getGroupMsgText();
        //获取sender
        Sender sender = groupDTO.getSender();
        //获取群号
        String groupCode = groupDTO.getGroupCode();
        //获取群名
        String groupName = groupDTO.getGroupName();
        //获取机器人昵称
        String botName = groupDTO.getBotName();
        //获取机器人QQ号
        String botCode = groupDTO.getBotCode();
        //将群消息以空格拆分成数组
        String[] strings = groupMsgText.trim().split(" ");
        //如果数组的长度等于2并且长度1为mcuuid
        if (strings.length == 2 && strings[0].equals("mcuuid")) {
            try {
                //请求mojang api服务器
                String mcUsername = strings[1];
                String url = "https://api.mojang.com/users/profiles/minecraft/" + mcUsername;
                String result = UrlUtil.getResult(url);
                //将响应的内容执行json解析并且返回玩家的uuid
                String uuid = JSON.parseObject(result).getString("id");
                //由于mojang api服务器将”-“处理掉了,将”-“添加回去
                String fullUUID = UUIDUtils.fullUUID(uuid);
                //将用户名和uuid发送到群
                String msg = "用户名：" + mcUsername + "\n" + "uuid：" + fullUUID;
                sender.sendGroupMsg(groupCode, msg);
                //输出日志
                log.info("[↑][群][{}({})]{}({}):{}", groupName, groupCode, botName, botCode, msg);
            } catch (Exception e) {
                //将提示信息发到群里
                sender.sendGroupMsg(groupCode, "用户名不正确或者是网络超时");
                //输出日志
                log.info("[↑][群][{}({})]{}({}):{}", groupName, groupCode, botName, botCode, "用户名不正确或者是网络超时");
            }
        }
    }

    /**
     * 将服务器信息发送到群并输出控制台
     *
     * @param groupDTO 群信息
     */
    @SneakyThrows
    @Override
    public void serverPing(GroupDTO groupDTO) {
        //获取群消息
        String groupMsgText = groupDTO.getGroupMsgText();
        String groupName = groupDTO.getGroupName();
        String groupCode = groupDTO.getGroupCode();
        Sender sender = groupDTO.getSender();
        String botName = groupDTO.getBotName();
        String botCode = groupDTO.getBotCode();
        try {
            //如果群消息是"server ping"
            if (groupMsgText.equalsIgnoreCase("server ping")) {
                //尝试ping目标服务器
                MinecraftPingReply serverPing = new MinecraftPing().getPing(new MinecraftPingOptions().setHostname(robotConfig.getMcServerPingIp()).setPort(robotConfig.getMcServerPingPort()));
                //获取在线玩家的昵称
                StringBuilder player = new StringBuilder();
                serverPing.getPlayers().getSample().forEach(players -> player.append("- ").append(players.getName()).append("\n"));
                //将服务器的在线玩家发送到群
                String serverPingInfoMsg = "服务端版本：" + serverPing.getVersion().getName() + "\n" + "当前在线：" + serverPing.getPlayers().getOnline() + "/" + serverPing.getPlayers().getMax() + "\n" + "在线玩家:\n" + player;
                sender.sendGroupMsg(groupCode, serverPingInfoMsg);
                //输出日志
                log.info("[↑][群][{}({})]{}({}):{}", groupName, groupCode, botName, botCode, serverPingInfoMsg);
            }
        } catch (Exception e) {
            //有可能会出现ping不到服务器的问题，将消息发送到群
            sender.sendGroupMsg(groupCode, "服务器已关闭或者连接超时");
            //输出日志
            log.info("[↑][群][{}({})]{}({}):{}", groupName, groupCode, botName, botCode, "服务器已关闭或者连接超时");
        }

    }

    private final HashMap<String, Long> hashMap = new HashMap<>();

    @SneakyThrows
    @Override
    public void randomImage(GroupDTO groupDTO) {
        String groupMsgText = groupDTO.getGroupMsgText();
        String groupName = groupDTO.getGroupName();
        String groupCode = groupDTO.getGroupCode();
        String groupAccountCode = groupDTO.getGroupAccountCode();
        Sender sender = groupDTO.getSender();
        String botName = groupDTO.getBotName();
        String botCode = groupDTO.getBotCode();

        //获取时间戳
        long time = System.currentTimeMillis();
        //冷却时间
        long coolDownTime = robotConfig.getRandomImageCd() * 1000L;
        //如果用户发送的是随机图片并且群没有存到HashMap里
        if (groupMsgText.equals("随机图片") && !hashMap.containsKey(groupCode)) {
            //初始化群最后请求的随机时间
            long lastRandomTime = 0;
            //将群号保存到key,把初始时间保存到value
            hashMap.put(groupCode, lastRandomTime);
        }
        //获取群最后请求随机图片的时间
        Long lastRandomTime = hashMap.get(groupCode);
        //如果用户发送随机图片并且群不在冷却时间
        if (groupMsgText.equals("随机图片") && time > lastRandomTime + coolDownTime) {
            //记录现在的时间戳更新到请求的群号
            hashMap.put(groupCode, time);
            //将图片发送到指定群里
            String cat = "[CAT:at,code=" + groupAccountCode + "][CAT:image,url=" + robotConfig.getRandomImageApi() + "]";
            sender.sendGroupMsg(groupCode, cat);
            log.info("[↑][群][{}({})]{}({}):{}", groupName, groupCode, botName, botCode, cat);
            //如果用户发送随机图片并且群在冷却时间内
        } else if (groupMsgText.equals("随机图片") && time < lastRandomTime + coolDownTime) {
            //冷却的剩余时间
            long cdTime = ((lastRandomTime + coolDownTime) - time) / 1000L;
            //将冷却的剩余时间发送到群内
            String message = "冷却中,还有" + cdTime + "秒可继续观看二次元[CAT:face,id=20]";
            sender.sendGroupMsg(groupCode, message);
            log.info("[↑][群][{}({})]{}({}):{}", groupName, groupCode, botName, botCode, message);
        }

    }


}
