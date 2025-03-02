package com.clockin.record.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 打卡類型枚舉
 */
@Getter
@AllArgsConstructor
public enum ClockInType {

    /**
     * 上班打卡
     */
    CLOCK_IN(1, "上班打卡"),

    /**
     * 下班打卡
     */
    CLOCK_OUT(2, "下班打卡");

    /**
     * 類型值
     */
    private final Integer value;

    /**
     * 類型描述
     */
    private final String desc;

    /**
     * 根據值獲取枚舉
     *
     * @param value 類型值
     * @return 枚舉實例
     */
    public static ClockInType fromValue(Integer value) {
        for (ClockInType type : ClockInType.values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("無效的打卡類型值: " + value);
    }
}
