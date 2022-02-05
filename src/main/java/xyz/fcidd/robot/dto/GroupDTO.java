package xyz.fcidd.robot.dto;

import lombok.Data;
import love.forte.simbot.api.sender.Sender;

import java.io.Serializable;


@Data
public class GroupDTO implements Serializable {
    //群消息
    private String groupMsgText;
    //群号
    private String groupCode;
    //群昵称
    private String groupName;
    //机器人昵称
    private String botName;
    //机器人
    private String botCode;
    //sender
    private Sender sender;
    //群成员昵称
    private String groupAccountNickname;
    //群成员QQ号
    private String groupAccountCode;
}
