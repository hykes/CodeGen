package me.hehaiyang.codegen.handlebars;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;

import java.io.IOException;

/**
 * Desc:
 * Mail: hehaiyang@terminus.io
 * Date: 16/4/21
 */
public class HandlebarsFactory {

    private final static Handlebars handlebars = new Handlebars();

    public static Handlebars getInstance() {

        handlebars.registerHelper("Rich", new Helper<String>() {
            public CharSequence apply(String value, Options options) throws IOException {
                return String.valueOf("#{"+value+"}");
            }
        });

        /**
         * 首字母小写
         */
        handlebars.registerHelper("LowerCase", new Helper<String>() {
            public CharSequence apply(String value, Options options) throws IOException {
                return value.replaceFirst(value.substring(0, 1),value.substring(0, 1).toLowerCase());
            }
        });

        handlebars.registerHelper("UpperCase", new Helper<String>() {
            public CharSequence apply(String value, Options options) throws IOException {
                return value.replaceFirst(value.substring(0, 1),value.substring(0, 1).toUpperCase());
            }
        });

        return handlebars;
    }

}
