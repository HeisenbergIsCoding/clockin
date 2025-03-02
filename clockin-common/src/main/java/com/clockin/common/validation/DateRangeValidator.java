package com.clockin.common.validation;

import com.clockin.common.validation.annotation.DateRange;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 日期範圍驗證器實現
 */
public class DateRangeValidator implements ConstraintValidator<DateRange, Object> {

    private String startDateField;
    private String endDateField;
    private boolean allowEqual;

    @Override
    public void initialize(DateRange constraintAnnotation) {
        this.startDateField = constraintAnnotation.startDate();
        this.endDateField = constraintAnnotation.endDate();
        this.allowEqual = constraintAnnotation.allowEqual();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        BeanWrapperImpl beanWrapper = new BeanWrapperImpl(value);
        Object startDateValue = beanWrapper.getPropertyValue(startDateField);
        Object endDateValue = beanWrapper.getPropertyValue(endDateField);

        // 如果開始日期或結束日期為空，則視為有效
        if (startDateValue == null || endDateValue == null) {
            return true;
        }

        // 根據日期類型進行比較
        if (startDateValue instanceof Date && endDateValue instanceof Date) {
            return validateDateRange((Date) startDateValue, (Date) endDateValue);
        } else if (startDateValue instanceof LocalDate && endDateValue instanceof LocalDate) {
            return validateLocalDateRange((LocalDate) startDateValue, (LocalDate) endDateValue);
        } else if (startDateValue instanceof LocalDateTime && endDateValue instanceof LocalDateTime) {
            return validateLocalDateTimeRange((LocalDateTime) startDateValue, (LocalDateTime) endDateValue);
        }

        // 如果不是支持的日期類型，則返回 true
        return true;
    }

    /**
     * 驗證 java.util.Date 類型的日期範圍
     *
     * @param startDate 開始日期
     * @param endDate   結束日期
     * @return 是否有效
     */
    private boolean validateDateRange(Date startDate, Date endDate) {
        if (allowEqual) {
            return !startDate.after(endDate);
        } else {
            return startDate.before(endDate);
        }
    }

    /**
     * 驗證 LocalDate 類型的日期範圍
     *
     * @param startDate 開始日期
     * @param endDate   結束日期
     * @return 是否有效
     */
    private boolean validateLocalDateRange(LocalDate startDate, LocalDate endDate) {
        if (allowEqual) {
            return !startDate.isAfter(endDate);
        } else {
            return startDate.isBefore(endDate);
        }
    }

    /**
     * 驗證 LocalDateTime 類型的日期範圍
     *
     * @param startDateTime 開始日期時間
     * @param endDateTime   結束日期時間
     * @return 是否有效
     */
    private boolean validateLocalDateTimeRange(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (allowEqual) {
            return !startDateTime.isAfter(endDateTime);
        } else {
            return startDateTime.isBefore(endDateTime);
        }
    }
}
