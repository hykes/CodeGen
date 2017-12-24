package com.github.hykes.codegen.constants;

import org.apache.commons.lang.time.DateFormatUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * 内置参数
 * @author hehaiyangwork@gmail.com
 * @date 2017/4/16
 */
public class DefaultParams {

    public static Map<String, String> getInHouseVariables() {
        Map<String, String> context = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        context.put("YEAR", String.valueOf(calendar.get(Calendar.YEAR)));
        context.put("MONTH", String.valueOf(calendar.get(Calendar.MONTH) + 1));
        context.put("DAY", String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        context.put("DATE", DateFormatUtils.format(calendar.getTime(), "yyyy-MM-dd"));
        context.put("TIME", DateFormatUtils.format(calendar.getTime(), "HH:mm:ss"));
        context.put("NOW", DateFormatUtils.format(calendar.getTime(), "yyyy-MM-dd HH:mm:ss"));
        context.put("USER", System.getProperty("user.name"));
        return context;
    }

}
