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

    public static Map<String, String> getInHouseVariables() {
        Map<String, String> params = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        params.put("YEAR", String.valueOf(calendar.get(Calendar.YEAR)));
        params.put("MONTH", String.valueOf(calendar.get(Calendar.MONTH) + 1));
        params.put("WEEK_OF_YEAR", String.valueOf(calendar.get(Calendar.WEEK_OF_YEAR)));
        params.put("WEEK_OF_MONTH", String.valueOf(calendar.get(Calendar.WEEK_OF_MONTH)));
        params.put("DATE", String.valueOf(calendar.get(Calendar.DATE)));
        params.put("DAY_OF_MONTH", String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        params.put("DAY_OF_YEAR", String.valueOf(calendar.get(Calendar.DAY_OF_YEAR)));
        params.put("DAY_OF_WEEK", String.valueOf(calendar.get(Calendar.DAY_OF_WEEK)));
        params.put("Hour", String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)));
        params.put("Minute", String.valueOf(calendar.get(Calendar.MINUTE)));
        params.put("Second", String.valueOf(calendar.get(Calendar.SECOND)));
        params.put("Week", String.valueOf(calendar.get(Calendar.DAY_OF_WEEK)));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        params.put("Now", formatter.format(calendar.getTime()));
        return params;
    }

    public static Map<String, String> getPreDefinedVariables() {
        Map<String, String> params = new HashMap<>();
        params.put("author", "[ your name ]");
        params.put("email", "[ your email ]");
        params.put("mobile", "[ your mobile ]");
        return params;
    }

}
