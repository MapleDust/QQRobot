package xyz.fcidd.robot.dto;

import cn.hutool.core.date.DateTime;
import lombok.Data;

import java.io.Serializable;

@Data
public class GroupMessageDTO implements Serializable {
    //群名
    private String groupName;
    //群号
    private String groupCode;
    //账号昵称
    private String accountName;
    //账号
    private String accountCode;
    //消息
    private String messageText;
    //发送时间
    private DateTime sendTime;
}
