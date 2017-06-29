package me.hehaiyang.codegen.constants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Desc: 内置参数
 * Mail: hehaiyang@terminus.io
 * Date: 2017/4/16
 */
public class DefaultParams {

    public static Map<String, String> getDefaults() {
        Map<String, String> params = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        params.put("Year", String.valueOf(calendar.get(Calendar.YEAR)));
        params.put("Month", String.valueOf(calendar.get(Calendar.MONTH) + 1));
        params.put("Day_Of_Month", String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        params.put("Day_Of_Week", String.valueOf(calendar.get(Calendar.DAY_OF_WEEK)));
        params.put("Day_Of_Week_In_Month", String.valueOf(calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH)));
        params.put("Date", String.valueOf(calendar.get(Calendar.DATE)));
        params.put("Hour", String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)));
        params.put("Minute", String.valueOf(calendar.get(Calendar.MINUTE)));
        params.put("Second", String.valueOf(calendar.get(Calendar.SECOND)));
        params.put("Week", String.valueOf(calendar.get(Calendar.DAY_OF_WEEK)));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        params.put("Now", formatter.format(calendar.getTime()));
        return params;
    }

    public static Map<String, String> getDefaultVariables() {
        Map<String, String> params = new HashMap<>();
        params.put("author", "[ your name ]");
        params.put("email", "[ your email ]");
        params.put("mobile", "[ your mobile ]");
        return params;
    }

}
