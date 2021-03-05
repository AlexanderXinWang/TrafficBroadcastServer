package com.iflytek.vivian.traffic.server.utils;

import com.iflytek.vivian.traffic.server.dto.NlpMapConfig;
import com.iflytek.vivian.traffic.server.dto.Position;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName StringUtil
 * @Author xinwang41
 * @Date 2021/1/4 15:18
 **/

@Component
public class StringUtil {
    private StringUtil() {
    }

    @Autowired
    private NlpMapConfig nlpMapConfig;

    /**
     * 正则匹配
     *
     * @param text  待匹配的文本
     * @param regex 正则表达式
     * @return
     */
    public static String regexExtra(String text, String regex) {
        Pattern pat = Pattern.compile(regex);
        Matcher mat = pat.matcher(text);
        if (mat.find()) {
            return mat.group();
        }
        return null;
    }

    /**
     * 去空格，小写转大写
     *
     * @param text
     * @return
     */
    public static String textPreprocess(String text) {
        return text.replaceAll("\\s*", "").
                replaceAll("[\\u00a0]*", "").toUpperCase();
    }


    /**
     * 提取警情概要要素，获取其出现的起始位置、结束位置
     * @param text 原文
     * @return
     */
    public Map<String, Position> getPoliceCaseElement(String text) {
        Map<String, Position> result = new HashMap<>();
        List<Position> posList = new ArrayList<>();

        for (Map.Entry<String, String> entry : nlpMapConfig.getMapProps().entrySet()) {
            String reg = "[\\u4e00-\\u9fa5]*" + entry.getValue();
            Pattern pat = Pattern.compile(reg);
            Matcher mat = pat.matcher(text);
            if (mat.find()) {
                for (int i = 1; i <= mat.groupCount(); i++) {
                    if (mat.group(i) != null) {
                        Position pos = new Position(mat.start(i), mat.end());
                        pos.setSentenceStartPos(mat.start());
                        result.put(entry.getKey(), pos);
                        posList.add(pos);
                        break;
                    }
                }
            }
        }
        correctElementsPos(posList, text.length());
        return result;
    }

    /**
     * 给位置列表排序
     *
     * @param list
     */
    private static void sortPosList(List<Position> list) {
        for (int i = 0; i < list.size(); i++) {
            for (int j = i + 1; j < list.size(); j++) {
                if (list.get(i).getStartPos() > list.get(j).getStartPos()) {
                    Position tem = list.get(i);
                    list.set(i, list.get(j));
                    list.set(j, tem);
                }
            }
        }
    }

    /**
     * 校正要素的结束位置
     *
     * @param list
     * @param textLen
     */
    private static void correctElementsPos(List<Position> list, int textLen) {
        // 根据开始位置，给list排序
        sortPosList(list);
        // 校正结束位置
        // 1.每个要素的结束位置为下一个要素的句子起始位置
        for (int i = 0; i < list.size() - 1; i++) {
            list.get(i).setEndPos(list.get(i + 1).getSentenceStartPos());
        }
        // 2.最后一个要素的结束位置是原文的结束位置
        if (!list.isEmpty()) {
            list.get(list.size() - 1).setEndPos(textLen);
        }
    }


    public String utcTime() {
        final String UTC_FORMATTER_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        DateTimeFormatter fmt = DateTimeFormat.forPattern(UTC_FORMATTER_PATTERN);
        DateTime now = DateTime.now(DateTimeZone.UTC);
        return fmt.print(now);
    }
}
