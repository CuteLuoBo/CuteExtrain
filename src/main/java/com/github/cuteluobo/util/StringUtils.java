package com.github.cuteluobo.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 * @author CuteLuoBo
 * @date 2022/10/10 1:10
 */
public class StringUtils {
    /**
     * 重复逗号匹配
     */
    private static final Pattern comma = Pattern.compile(",,+");
    private static final Pattern chineseTextCheck = Pattern.compile("[\u4e00-\u9fa5]");
    public static final Pattern oneArg = Pattern.compile("\"([^\"]*)\"");

    /**
     * 用于Tags的逗号处理
     * @param text 传入文本
     * @return 处理后文本
     */
    public static String tagsCommaHandler(String text) {
        //中英文逗号替换
        text = text.replace("，", ",");
        //路径符号替换
        text = text.replace("/", "");
        //去除重复逗号
        text = comma.matcher(text).replaceAll(",");
        return text;
    }

    /**
     * 是否包含中文字符
     * @param str 待检查的字符串
     * @return 检查结果
     */
    public static boolean isContainChinese(String str) {
        Matcher m = chineseTextCheck.matcher(str);
        return m.find();
    }

}
