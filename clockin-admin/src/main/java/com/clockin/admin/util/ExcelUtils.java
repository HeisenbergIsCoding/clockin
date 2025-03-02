package com.clockin.admin.util;

import com.clockin.admin.dto.HolidayDTO;
import com.clockin.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Excel 工具類
 */
@Slf4j
public class ExcelUtils {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 解析假日 Excel 文件
     *
     * @param file Excel 文件
     * @return 假日 DTO 列表
     */
    public static List<HolidayDTO> parseHolidayExcel(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("上傳的文件為空");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !(originalFilename.endsWith(".xlsx") || originalFilename.endsWith(".xls"))) {
            throw new BusinessException("文件格式不正確，請上傳 Excel 文件");
        }

        List<HolidayDTO> holidays = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            int rowCount = 0;

            for (Row row : sheet) {
                // 跳過表頭
                if (rowCount == 0) {
                    rowCount++;
                    continue;
                }

                HolidayDTO holiday = parseHolidayRow(row);
                if (holiday != null) {
                    holidays.add(holiday);
                }

                rowCount++;
            }

        } catch (IOException e) {
            log.error("解析 Excel 文件失敗", e);
            throw new BusinessException("解析 Excel 文件失敗：" + e.getMessage());
        }

        if (holidays.isEmpty()) {
            throw new BusinessException("Excel 文件中未找到有效的假日數據");
        }

        return holidays;
    }

    /**
     * 解析假日數據行
     *
     * @param row Excel 行
     * @return 假日 DTO
     */
    private static HolidayDTO parseHolidayRow(Row row) {
        String dateStr = getCellValueAsString(row.getCell(0));
        String name = getCellValueAsString(row.getCell(1));
        String typeStr = getCellValueAsString(row.getCell(2));
        String remark = getCellValueAsString(row.getCell(3));

        if (StringUtils.isBlank(dateStr) || StringUtils.isBlank(name) || StringUtils.isBlank(typeStr)) {
            return null;
        }

        LocalDate date;
        try {
            date = LocalDate.parse(dateStr, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            log.error("日期格式不正確: {}", dateStr, e);
            return null;
        }

        int type;
        try {
            type = Integer.parseInt(typeStr);
            if (type < 1 || type > 4) {
                log.error("假日類型不正確: {}", typeStr);
                return null;
            }
        } catch (NumberFormatException e) {
            log.error("假日類型不是數字: {}", typeStr, e);
            return null;
        }

        HolidayDTO holiday = new HolidayDTO();
        holiday.setHolidayDate(date);
        holiday.setHolidayName(name);
        holiday.setHolidayType(type);
        holiday.setYear(date.getYear());
        holiday.setMonth(date.getMonthValue());
        holiday.setDay(date.getDayOfMonth());
        holiday.setStatus(1);
        holiday.setRemark(remark);

        return holiday;
    }

    /**
     * 獲取單元格值轉為字符串
     *
     * @param cell 單元格
     * @return 字符串值
     */
    private static String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toLocalDate().toString();
                }
                return String.valueOf((int) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                try {
                    return String.valueOf(cell.getNumericCellValue());
                } catch (Exception e) {
                    return cell.getStringCellValue();
                }
            default:
                return "";
        }
    }
}
