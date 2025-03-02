package com.clockin.record.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 缺勤類型枚舉
 */
@Getter
@AllArgsConstructor
public enum AbsenceType {

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
     * 曠工
     */
    ABSENCE(3, "曠工"),

    /**
     * 請假
     */
    LEAVE(4, "請假"),

    /**
     * 外勤
     */
    OUTSIDE_WORK(5, "外勤");

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
    public static AbsenceType fromValue(Integer value) {
        for (AbsenceType type : AbsenceType.values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("無效的缺勤類型值: " + value);
    }
}
