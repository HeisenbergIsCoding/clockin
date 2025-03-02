package com.clockin.common.validation.annotation;

import com.clockin.common.validation.DateRangeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * 日期範圍驗證註解
 * 用於驗證實體類中兩個日期欄位的先後順序
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateRangeValidator.class)
@Documented
public @interface DateRange {

    /**
     * 錯誤消息
     */
    String message() default "結束日期必須在開始日期之後";

    /**
     * 分組
     */
    Class<?>[] groups() default {};

    /**
     * 負載
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * 開始日期欄位名稱
     */
    String startDate();

    /**
     * 結束日期欄位名稱
     */
    String endDate();

    /**
     * 是否允許兩個日期相等
     */
    boolean allowEqual() default true;
}
