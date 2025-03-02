package com.clockin.record.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 請假類型枚舉
 */
@Getter
@AllArgsConstructor
public enum LeaveTypeEnum {

    /**
     * 年假
     */
    ANNUAL(1, "年假"),

    /**
     * 事假
     */
    PERSONAL(2, "事假"),

    /**
     * 病假
     */
    SICK(3, "病假"),

    /**
     * 婚假
     */
    MARRIAGE(4, "婚假"),

    /**
     * 產假
     */
    MATERNITY(5, "產假"),

    /**
     * 喪假
     */
    FUNERAL(6, "喪假"),

    /**
     * 調休
     */
    ADJUST_REST(7, "調休"),

    /**
     * 其他
     */
    OTHER(8, "其他");

    /**
     * 類型ID
     */
    private final Integer value;

    /**
     * 類型名稱
     */
    private final String name;

    /**
     * 根據類型ID獲取枚舉
     *
     * @param value 類型ID
     * @return 請假類型枚舉
     */
    public static LeaveTypeEnum getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (LeaveTypeEnum type : LeaveTypeEnum.values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        return null;
    }

    /**
     * 根據類型ID獲取類型名稱
     *
     * @param value 類型ID
     * @return 類型名稱
     */
    public static String getNameByValue(Integer value) {
        LeaveTypeEnum type = getByValue(value);
        return type == null ? null : type.getName();
    }
}
