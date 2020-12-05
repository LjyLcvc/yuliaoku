package com.lcvc.guojiaoyuan.yuliaoku.util.string;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyStringUtil {

    /**
     *去掉字符串头尾的空格，并且让中间空格最多只保留一个
     * 例如： hello,a b    c    d
     * 结果：hello,a b c d
     * @param s 要处理的字符串
     * @return 处理好的字符串
     */
    public static String trimBeginEndAndRetainOneSpaceInMiddle(String s){
        return s.trim().replaceAll("\\s{1,}", " ");
    }


    /**
     * 过滤特殊字符，并去掉空格
     * @param str
     * @return
     */
    public static String stringFilterOfLetterNumber(String str){
        //String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        String regEx ="[^a-zA-Z0-9]";//只允许字母和数字
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }
}
