package xyz.fcidd.robot.utils.uuid;

public class UUIDUtils {
    /**
     * 将uid添加”-“
     *
     * @param uuid 用户使用的uuid
     * @return 处理过后的uid
     */
    public static String fullUUID(String uuid) {
        StringBuilder fullUuid = new StringBuilder();
        int end = 8;
        fullUuid.append(uuid, 0, end);
        for (int i = 0; i < 3; i++) {
            fullUuid.append("-").append(uuid, end, end += 4);
        }
        fullUuid.append("-").append(uuid.substring(end));
        return fullUuid.toString();
    }
}
