package me.hehaiyang.codegen.handlebars;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import com.github.jknack.handlebars.Template;

import java.io.IOException;

/**
 * Desc:
 * Mail: hehaiyang@terminus.io
 * Date: 16/4/21
 */
public class HandlebarsFactory {

    private final static Handlebars handlebars = new Handlebars();

    public static Handlebars getInstance() {

        /**
         * 首尾拼接字符
         * {{Join 'ABC' '#' '%'}}  => #ABC%
         */
        handlebars.registerHelper("Join", new Helper<String>() {
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
        handlebars.registerHelper("LowerCase", new Helper<String>() {
            public CharSequence apply(String value, Options options) throws IOException {
                return value.replaceFirst(value.substring(0, 1), value.substring(0, 1).toLowerCase());
            }
        });

        /**
         * 首字母大写
         * {{LowerCase 'abc'}} => Abc
         */
        handlebars.registerHelper("UpperCase", new Helper<String>() {
            public CharSequence apply(String value, Options options) throws IOException {
                return value.replaceFirst(value.substring(0, 1), value.substring(0, 1).toUpperCase());
            }
        });

        /**
         * 驼峰转拼接字符
         * {{Split 'ABcD' '_'}} => A_Bc_D
         */
        handlebars.registerHelper("Split", new Helper<String>() {
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

        return handlebars;
    }

    public static void main(String[] args) throws Exception{
        Template fileNameTemp = HandlebarsFactory.getInstance().compileInline("{{Split (Join (LowerCase 'SubUserRole') '$' '%') '_'}}");
        String outputName = fileNameTemp.apply(null);
        System.out.println(outputName);
    }
}
