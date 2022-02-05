package xyz.fcidd.robot.service.impl;

import cn.hutool.core.date.DateTime;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import love.forte.simbot.api.sender.Sender;
import org.springframework.stereotype.Service;
import xyz.fcidd.robot.dto.GroupDTO;
import xyz.fcidd.robot.service.GroupService;
import xyz.fcidd.robot.utils.mcping.MinecraftPing;
import xyz.fcidd.robot.utils.mcping.MinecraftPingOptions;
import xyz.fcidd.robot.utils.mcping.MinecraftPingReply;
import xyz.fcidd.robot.utils.url.UrlUtil;

@Service
@Log4j2
public class GroupServiceImpl implements GroupService {

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
                //将响应的内容执行json解析
                String result = new UrlUtil().getResult(url);
                JSONObject jsonObject = JSON.parseObject(result);
                //获取解析过后的uuid
                String uuid = jsonObject.getString("id");
                //将用户名和uuid发送到群
                String msg = "用户名：" + mcUsername + "\n" + "uuid：" + uuid;
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
		//获取群名
        String groupName = groupDTO.getGroupName();
		//获取群号
        String groupCode = groupDTO.getGroupCode();
		//获取sender
        Sender sender = groupDTO.getSender();
		//获取机器人昵称
        String botName = groupDTO.getBotName();
		//获取机器人QQ号
        String botCode = groupDTO.getBotCode();
        try {
            //如果群消息是"fz ping"
            if (groupMsgText.equalsIgnoreCase("server ping")) {
                //尝试ping目标服务器
                MinecraftPingReply serverPing = new MinecraftPing().getPing(new MinecraftPingOptions().setHostname(""));
                //获取在线玩家的昵称
                StringBuilder player = new StringBuilder();
                serverPing.getPlayers().getSample().forEach(players -> player.append("- ").append(players.getName()).append("\n"));
                //将服务器的在线玩家发送到群
                String serverPingInfoMsg ="服务端版本："+serverPing.getVersion().getName()+"\n"+
                        "当前在线：" + serverPing.getPlayers().getOnline() + "/" + serverPing.getPlayers().getMax() + "\n" +
                        "在线玩家:\n" + player;
                sender.sendGroupMsg(groupCode, serverPingInfoMsg);
                //输出日志
                log.info("[↑][群][{}({})]{}({}):{}", groupName, groupCode, botName, botCode, serverPingInfoMsg);
            }
        }catch (Exception e){
            sender.sendGroupMsg(groupCode,"服务器已关闭或者连接超时");
        }

    }


}
