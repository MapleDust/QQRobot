package xyz.fcidd.robot.listener;

import love.forte.common.ioc.annotation.Beans;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.api.message.containers.GroupAccountInfo;
import love.forte.simbot.api.message.containers.GroupBotInfo;
import love.forte.simbot.api.message.containers.GroupInfo;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.Sender;
import org.springframework.stereotype.Component;
import xyz.fcidd.robot.dto.GroupDTO;
import xyz.fcidd.robot.service.GroupService;

import javax.annotation.Resource;

/**
 * 群消息监听的示例类。
 * 所有需要被管理的类都需要标注 {@link Beans} 注解。
 *
 * @author ForteScarlet
 */
@Component
public class MyGroupListen {
    @Resource
    private GroupService groupService;

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
    private GroupDTO setGroupDTO(GroupMsg groupMsg,Sender sender){
        //获取机器人的信息
        GroupBotInfo botInfo = groupMsg.getBotInfo();
        //获取群信息
        GroupInfo groupInfo = groupMsg.getGroupInfo();
        //获取群成员的账号信息
        GroupAccountInfo groupAccountInfo = groupMsg.getAccountInfo();
        GroupDTO groupDTO = new GroupDTO();
        //获取群昵称
        groupDTO.setGroupName(groupInfo.getGroupName());
        //获取群号
        groupDTO.setGroupCode(groupInfo.getGroupCode());
        //获取sender
        groupDTO.setSender(sender);
        //获取群成员的昵称
        groupDTO.setGroupAccountNickname(groupAccountInfo.getAccountNickname());
        //获取群成员的QQ号
        groupDTO.setGroupAccountCode(groupAccountInfo.getAccountCode());
        //获取群消息
        groupDTO.setGroupMsgText(groupMsg.getText());
        //获取机器人昵称
        groupDTO.setBotName(botInfo.getBotName());
        //获取机器人的QQ号
        groupDTO.setBotCode(botInfo.getBotCode());
        return groupDTO;
    }

    @OnGroup
    public void onGroupMsg(GroupMsg groupMsg, Sender sender) {
        GroupDTO groupDTO = setGroupDTO(groupMsg, sender);
        //将群消息输出到控制台
        groupService.groupMsg(groupDTO);
        //将现在时间发送至群并输出到控制台
        groupService.nowTime(groupDTO);
        //查询mc玩家的uuid并发送至群和输出到控制台
        groupService.mcUuid(groupDTO);
        //将服务器信息发送到群并输出控制台
        groupService.fzPing(groupDTO);
    }
}
