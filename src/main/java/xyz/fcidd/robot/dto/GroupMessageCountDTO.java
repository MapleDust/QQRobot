package xyz.fcidd.robot.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class GroupMessageCountDTO implements Serializable {
    private String time;
    private String groupCode;
}
