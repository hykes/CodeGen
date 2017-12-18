package com.github.hykes.codegen.file;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import com.github.jknack.handlebars.Template;
import com.google.common.collect.Maps;

import java.io.IOException;
import java.util.Map;

/**
 * 模版引擎
 *
 * @author: hehaiyangwork@qq.com
 * @Date: 2016/04/21
 */
public class HandlebarsFactory {

    private final static Handlebars HANDLEBARS = new Handlebars();

    public static Handlebars getInstance() {

        /**
         * 首尾拼接字符
         * {{Join 'ABC' '#' '%'}}  => #ABC%
         */
        HANDLEBARS.registerHelper("Join", new Helper<String>() {
            @Override
            public CharSequence apply(String context, Options options) throws IOException {
                if (options.params == null || options.params.length != 2) {
                    return options.fn(context);
                }else{
                    String prefix = options.params[0].toString();
                    String suffix = options.params[1].toString();
                    return String.valueOf(prefix + context + suffix);
                }
            }
        });

        /**
         * 首字母小写
         * {{LowerCase 'ABC'}} => aBC
         */
        HANDLEBARS.registerHelper("LowerCase", new Helper<String>() {
            @Override
            public CharSequence apply(String value, Options options) throws IOException {
                return value.replaceFirst(value.substring(0, 1), value.substring(0, 1).toLowerCase());
            }
        });

        /**
         * 首字母大写
         * {{LowerCase 'abc'}} => Abc
         */
        HANDLEBARS.registerHelper("UpperCase", new Helper<String>() {
            @Override
            public CharSequence apply(String value, Options options) throws IOException {
                return value.replaceFirst(value.substring(0, 1), value.substring(0, 1).toUpperCase());
            }
        });

        /**
         * 驼峰转拼接字符
         * {{Split 'ABcD' '_'}} => A_bc_d
         */
        HANDLEBARS.registerHelper("Split", new Helper<String>() {
            @Override
            public CharSequence apply(String value, Options options) throws IOException {
                if (value == null || "".equals(value.trim())) {
                    return "";
                }
                if (options.params == null || options.params.length != 1) {
                    return value;
                }else {
                    String character = options.params[0].toString();
                    int len = value.length();
                    StringBuilder sb = new StringBuilder(len);
                    sb.append(value.charAt(0));
                    for (int i = 1; i < len; i++) {
                        char c = value.charAt(i);
                        if (Character.isUpperCase(c)) {
                            sb.append(character).append(Character.toLowerCase(c));
                        } else {
                            sb.append(c);
                        }
                    }
                    return sb.toString();
                }
            }
        });

        return HANDLEBARS;
    }

    public static void main(String[] args) throws Exception{
//        Template fileNameTemp = HandlebarsFactory.getInstance().compileInline("{{Split (Join (LowerCase 'SubUserRole') '$' '%') '_'}}");
        Template fileNameTemp = HandlebarsFactory.getInstance().compileInline("{{LowerCase (Join name '' 'SubUserRole')}}");
        Map<String, String> map = Maps.newHashMap();
        map.put("name", "Hykes");
        String outputName = fileNameTemp.apply(map);
        System.out.println(outputName);
    }
}
