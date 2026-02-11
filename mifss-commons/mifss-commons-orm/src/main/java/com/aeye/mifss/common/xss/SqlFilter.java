package com.aeye.mifss.common.xss;

import cn.hsa.hsaf.core.framework.web.exception.AppException;
import cn.hutool.core.util.StrUtil;

public class SqlFilter {

    /**
     * 过滤通过页面表单提交的字符
      */
    private static String[][] FilterChars={{"<","&lt;"},{">","&gt;"},{" ","&nbsp;"},{"\"","&quot;"},{"&","&amp;"},
            {"/","&#47;"},{"\\","&#92;"},{"\n","<br>"}};
    /**
     * 过滤通过javascript脚本处理并提交的字符
     */
    private static String[][] FilterScriptChars={{"\n"," "},
            {"\r"," "},{"\\"," "},
            {"\'"," "}};

    /**
     * SQL注入过滤
     * @param str  待验证的字符串
     */
    public static String sqlInject(String str) throws Exception{
        if(StrUtil.isBlank(str)){
            return null;
        }
        str = stringFilterScriptChar(stringFilter(str));

        //转换成小写
        str = str.toLowerCase();

        //非法字符
        String[] keywords = {"master", "truncate", "insert", "select", "delete", "update", "declare", "alter", "drop"};

        //判断是否包含非法字符
        for(String keyword : keywords){
            if(str.indexOf(keyword) != -1){
                throw new AppException("包含非法字符");
            }
        }

        return str;
    }

    /**
     * 过滤字符串里的的特殊字符
     * @param str 要过滤的字符串
     * @return 过滤后的字符串
     */
    public static String stringFilter(String str){
        String[] strArr=stringSpilit(str,"");
        for(int i=0;i<strArr.length;i++){
            for(int j=0;j<FilterChars.length;j++){
                if(FilterChars[j][0].equals(strArr[i])){
                    strArr[i]=FilterChars[j][1];
                }
            }
        }
        return (stringConnect(strArr,"")).trim();
    }
    /**
     * 过滤脚本中的特殊字符（包括回车符(\n)和换行符(\r)）
     * @param str 要进行过滤的字符串
     * @return 过滤后的字符串
     * 2004-12-21 闫
     */
    public static String stringFilterScriptChar(String str){
        String[] strArr=stringSpilit(str,"");
        for(int i=0;i<strArr.length;i++){
            for (int j = 0; j < FilterScriptChars.length; j++) {
                if (FilterScriptChars[j][0].equals(strArr[i])){
                    strArr[i] = FilterScriptChars[j][1];
                }
            }
        }
        return(stringConnect(strArr,"")).trim();
    }

    /**
     * 分割字符串
     * @param str 要分割的字符串
     * @param spilitSign 字符串的分割标志
     * @return 分割后得到的字符串数组
     */
    public static String[] stringSpilit(String str,String spilitSign){
        String[] spilitString=str.split(spilitSign);
        if(spilitString[0].equals(""))
        {
            String[] newString=new String[spilitString.length-1];
            for(int i=1;i<spilitString.length;i++) {
                newString[i-1]=spilitString[i];
            }
            return newString;
        } else {
            return spilitString;
        }
    }

    /**
     * 用特殊的字符连接字符串
     * @param strings 要连接的字符串数组
     * @param spilitSign 连接字符
     * @return 连接字符串
     */
    public static String stringConnect(String[] strings,String spilitSign){
        String str="";
        for(int i=0;i<strings.length;i++){
            str+=strings[i]+spilitSign;
        }
        return str;
    }
}
