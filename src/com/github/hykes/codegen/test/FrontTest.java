package com.github.hykes.codegen.test;

import java.util.HashMap;
import java.util.Map;

/**
 * Desc:
 * Mail: hehaiyangwork@gmail.com
 * Date: 2017/7/12
 */
public class FrontTest {

    final static String template = "---\n" +
            "layout: false\n" +
            "title: \"Hello world\"\n" +
            "---\n" +
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit.";

    public static void main(String[] args) {
        String str2 = getText(template, "---");
        System.out.println(str2);

        Map<String, String> map = getmap(str2);
        System.out.println(map);

        String str3 = getTemp(template, "---");
        System.out.println(str3);

    }

    private static String getText(String str, String c) {
        int n = 0;
        int pos = -1;
        while (n < 1) {
            pos = str.indexOf(c, pos + 1);
            if (pos == -1) {
                return "";
            }
            n++;
        }
        int st_pos = pos;
        while (n < 2) {
            pos = str.indexOf(c, pos + 1);
            if (pos == -1) {
                return str.substring(pos + 1);
            }
            n++;
        }
        return str.substring(st_pos + c.length(), pos);
    }

    private static String getTemp(String str, String c) {
        int n = 0;
        int pos = -1;
        while (n < 1) {
            pos = str.indexOf(c, pos + 1);
            if (pos == -1) {
                return "";
            }
            n++;
        }
        while (n < 2) {
            pos = str.indexOf(c, pos + 1);
            if (pos == -1) {
                return str.substring(pos + 1);
            }
            n++;
        }
        return str.substring(pos+c.length());
    }

    private static Map<String, String> getmap(String str){
        Map<String, String> result = new HashMap<>();
        String attr[] = str.trim().split("\n");
        for(String s: attr){
            int pos = s.indexOf(":");
            if (pos == -1) {
                break;
            }
            String key = s.substring(0, pos).trim();
            String value = s.substring(pos+1).trim();
            result.put(key, value);
        }
        return result;
    }

}
