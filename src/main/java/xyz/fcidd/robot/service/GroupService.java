package xyz.fcidd.robot.service;

import xyz.fcidd.robot.dto.GroupDTO;

public interface GroupService {
    //将群消息输出到控制台
    void groupMsg(GroupDTO groupDTO);
    //发送现在时间并输出到控制台
    void nowTime(GroupDTO groupDTO);
    //查询mc玩家的uuid并发送至群和输出到控制台
    void mcUuid(GroupDTO groupDTO);
    //将服务器信息发送到群并输出控制台
    void serverPing(GroupDTO groupDTO);
}
