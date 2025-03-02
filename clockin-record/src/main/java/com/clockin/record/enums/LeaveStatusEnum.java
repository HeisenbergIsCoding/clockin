package com.clockin.record.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 請假狀態枚舉
 */
@Getter
@AllArgsConstructor
public enum LeaveStatusEnum {

    /**
     * 待審批
     */
    PENDING(0, "待審批"),

    /**
     * 已通過
     */
    APPROVED(1, "已通過"),

    /**
     * 已拒絕
     */
    REJECTED(2, "已拒絕"),

    /**
     * 已取消
     */
    CANCELLED(3, "已取消");

    /**
     * 狀態值
     */
    private final Integer value;

    /**
     * 狀態名稱
     */
    private final String name;

    /**
     * 根據狀態值獲取枚舉
     *
     * @param value 狀態值
     * @return 請假狀態枚舉
     */
    public static LeaveStatusEnum getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (LeaveStatusEnum status : LeaveStatusEnum.values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        return null;
    }

    /**
     * 根據狀態值獲取狀態名稱
     *
     * @param value 狀態值
     * @return 狀態名稱
     */
    public static String getNameByValue(Integer value) {
        LeaveStatusEnum status = getByValue(value);
        return status == null ? null : status.getName();
    }
}
