package com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.util.string;

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
}
