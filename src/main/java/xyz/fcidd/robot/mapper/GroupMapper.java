package xyz.fcidd.robot.mapper;

import org.apache.ibatis.annotations.Mapper;
import xyz.fcidd.robot.dto.GroupMessageDTO;

@Mapper
public interface GroupMapper {
    void groupMessageInsert(GroupMessageDTO groupMessageDTO);
}
