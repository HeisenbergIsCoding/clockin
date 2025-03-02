package com.clockin.common.util;

import org.apache.commons.lang3.StringUtils;

import java.util.UUID;
import java.util.regex.Pattern;

/**
 * 字符串工具類，基於 Apache Commons
 */
public class StringUtil {

    /**
     * 判斷字符串是否為空
     *
     * @param str 字符串
     * @return 是否為空
     */
    public static boolean isEmpty(String str) {
        return StringUtils.isEmpty(str);
    }

    /**
     * 判斷字符串是否為空（包括空白字符）
     *
     * @param str 字符串
     * @return 是否為空
     */
    public static boolean isBlank(String str) {
        return StringUtils.isBlank(str);
    }

    /**
     * 判斷字符串是否不為空
     *
     * @param str 字符串
     * @return 是否不為空
     */
    public static boolean isNotEmpty(String str) {
        return StringUtils.isNotEmpty(str);
    }

    /**
     * 判斷字符串是否不為空（包括空白字符）
     *
     * @param str 字符串
     * @return 是否不為空
     */
    public static boolean isNotBlank(String str) {
        return StringUtils.isNotBlank(str);
    }

    /**
     * 去除字符串兩端空白
     *
     * @param str 字符串
     * @return 處理後的字符串
     */
    public static String trim(String str) {
        return StringUtils.trim(str);
    }

    /**
     * 將字符串轉換為小寫
     *
     * @param str 字符串
     * @return 轉換後的字符串
     */
    public static String toLowerCase(String str) {
        return StringUtils.lowerCase(str);
    }

    /**
     * 將字符串轉換為大寫
     *
     * @param str 字符串
     * @return 轉換後的字符串
     */
    public static String toUpperCase(String str) {
        return StringUtils.upperCase(str);
    }

    /**
     * 首字母大寫
     *
     * @param str 字符串
     * @return 轉換後的字符串
     */
    public static String capitalize(String str) {
        return StringUtils.capitalize(str);
    }

    /**
     * 首字母小寫
     *
     * @param str 字符串
     * @return 轉換後的字符串
     */
    public static String uncapitalize(String str) {
        return StringUtils.uncapitalize(str);
    }

    /**
     * 判斷字符串是否包含子字符串
     *
     * @param str    字符串
     * @param substr 子字符串
     * @return 是否包含
     */
    public static boolean contains(String str, String substr) {
        return StringUtils.contains(str, substr);
    }

    /**
     * 判斷字符串是否以指定字符串開頭
     *
     * @param str    字符串
     * @param prefix 前綴
     * @return 是否以指定字符串開頭
     */
    public static boolean startsWith(String str, String prefix) {
        return StringUtils.startsWith(str, prefix);
    }

    /**
     * 判斷字符串是否以指定字符串結尾
     *
     * @param str    字符串
     * @param suffix 後綴
     * @return 是否以指定字符串結尾
     */
    public static boolean endsWith(String str, String suffix) {
        return StringUtils.endsWith(str, suffix);
    }

    /**
     * 替換字符串
     *
     * @param str         字符串
     * @param searchStr   查找字符串
     * @param replacement 替換字符串
     * @return 替換後的字符串
     */
    public static String replace(String str, String searchStr, String replacement) {
        return StringUtils.replace(str, searchStr, replacement);
    }

    /**
     * 子字符串出現次數
     *
     * @param str    字符串
     * @param substr 子字符串
     * @return 出現次數
     */
    public static int countMatches(String str, String substr) {
        return StringUtils.countMatches(str, substr);
    }

    /**
     * 獲取子字符串
     *
     * @param str   字符串
     * @param start 開始位置
     * @param end   結束位置
     * @return 子字符串
     */
    public static String substring(String str, int start, int end) {
        return StringUtils.substring(str, start, end);
    }

    /**
     * 獲取字符串長度，null 返回 0
     *
     * @param str 字符串
     * @return 長度
     */
    public static int length(String str) {
        return StringUtils.length(str);
    }

    /**
     * 字符串重複
     *
     * @param str   字符串
     * @param times 重複次數
     * @return 重複後的字符串
     */
    public static String repeat(String str, int times) {
        return StringUtils.repeat(str, times);
    }

    /**
     * 字符串連接
     *
     * @param array  字符串數組
     * @param delimiter 分隔符
     * @return 連接後的字符串
     */
    public static String join(String[] array, String delimiter) {
        return StringUtils.join(array, delimiter);
    }

    /**
     * 獲取 UUID（無橫線）
     *
     * @return UUID
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 正則表達式匹配
     *
     * @param str   字符串
     * @param regex 正則表達式
     * @return 是否匹配
     */
    public static boolean matches(String str, String regex) {
        if (str == null) {
            return false;
        }
        return Pattern.matches(regex, str);
    }

    /**
     * 截取字符串，超出部分用 ... 表示
     *
     * @param str    字符串
     * @param maxLen 最大長度
     * @return 截取後的字符串
     */
    public static String ellipsis(String str, int maxLen) {
        if (str == null) {
            return null;
        }
        if (str.length() <= maxLen) {
            return str;
        }
        return str.substring(0, maxLen) + "...";
    }

    /**
     * 駝峰命名轉下劃線命名
     *
     * @param str 駝峰命名字符串
     * @return 下劃線命名字符串
     */
    public static String camelToUnderline(String str) {
        if (str == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (Character.isUpperCase(c)) {
                if (i > 0) {
                    sb.append('_');
                }
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 下劃線命名轉駝峰命名
     *
     * @param str              下劃線命名字符串
     * @param capitalizeFirst 首字母是否大寫
     * @return 駝峰命名字符串
     */
    public static String underlineToCamel(String str, boolean capitalizeFirst) {
        if (str == null) {
            return null;
        }
        String[] parts = str.split("_");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            if (i == 0 && !capitalizeFirst) {
                sb.append(part);
            } else {
                sb.append(StringUtils.capitalize(part));
            }
        }
        return sb.toString();
    }

    /**
     * 判斷字符串是否是數字
     *
     * @param str 字符串
     * @return 是否是數字
     */
    public static boolean isNumeric(String str) {
        return StringUtils.isNumeric(str);
    }

    /**
     * 判斷字符串是否是字母
     *
     * @param str 字符串
     * @return 是否是字母
     */
    public static boolean isAlpha(String str) {
        return StringUtils.isAlpha(str);
    }

    /**
     * 判斷字符串是否是字母或數字
     *
     * @param str 字符串
     * @return 是否是字母或數字
     */
    public static boolean isAlphanumeric(String str) {
        return StringUtils.isAlphanumeric(str);
    }
}
