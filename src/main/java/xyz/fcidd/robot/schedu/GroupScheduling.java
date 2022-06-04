package xyz.fcidd.robot.schedu;

import cn.hutool.core.date.DateTime;
import lombok.extern.log4j.Log4j2;
import love.forte.simbot.api.message.containers.BotInfo;
import love.forte.simbot.api.message.results.GroupList;
import love.forte.simbot.api.sender.BotSender;
import love.forte.simbot.bot.BotManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import xyz.fcidd.robot.dto.GroupMessageCountDTO;
import xyz.fcidd.robot.mapper.GroupMapper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Component
@EnableScheduling
@Log4j2
public class GroupScheduling {
    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private BotManager botManager;

    /**
     * 每日0点自动执行消息报数
     */
    @Scheduled(cron = "0 0 0 * * ?")
    private void messageCount() {

        BotSender sender = botManager.getDefaultBot().getSender();
        BotInfo botInfo = sender.getBotInfo();
        //获取群列表
        GroupList groupList = sender.GETTER.getGroupList();
        //获取机器人账号
        String botCode = botInfo.getBotCode();
        //获取机器人昵称
        String botName = botInfo.getBotName();
        //将获取的群列表循环
        groupList.forEach(group -> {
            //获取群号
            String groupCode = group.getGroupCode();
            //获取群昵称
            String groupName = group.getGroupName();
            //获取现在的时间
            Date date = new Date();
            //创建calendar工具类实例
            Calendar calendar = Calendar.getInstance();
            //将时间设置为现在时间
            calendar.setTime(date);
            //将现在时间减去1天
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            //设置时间格式
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            //获取格式后的时间
            String time = simpleDateFormat.format(calendar.getTime());
            //从数据库中查询发送的消息条数
            GroupMessageCountDTO groupMessageCountDTO=new GroupMessageCountDTO();
            groupMessageCountDTO.setGroupCode(groupCode);
            groupMessageCountDTO.setTime(time+"%");
            Integer messageCount = groupMapper.groupMessageCount(groupMessageCountDTO);
            String message="截至 " + DateTime.now() + " 机器人在线时间段，昨天群友在本群一共发送了" + messageCount + "条消息";
            //将从数据库中查询的条数发送提示到群中
            sender.SENDER.sendGroupMsg(groupCode, message);
            log.info("[↑][群][{}({})]{}({}):{}", groupName, groupCode, botName, botCode,message);
        });
    }
}
