package com.clockin.record.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 打卡狀態枚舉
 */
@Getter
@AllArgsConstructor
public enum ClockInStatus {

    /**
     * 正常
     */
    NORMAL(0, "正常"),

    /**
     * 遲到
     */
    LATE(1, "遲到"),

    /**
     * 早退
     */
    EARLY_LEAVE(2, "早退"),

    /**
     * 補卡
     */
    MAKEUP(3, "補卡"),

    /**
     * 異常
     */
    ABNORMAL(4, "異常");

    /**
     * 狀態值
     */
    private final Integer value;

    /**
     * 狀態描述
     */
    private final String desc;

    /**
     * 根據值獲取枚舉
     *
     * @param value 狀態值
     * @return 枚舉實例
     */
    public static ClockInStatus fromValue(Integer value) {
        for (ClockInStatus status : ClockInStatus.values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("無效的打卡狀態值: " + value);
    }
}
