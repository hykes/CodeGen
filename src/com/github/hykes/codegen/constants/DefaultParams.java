package com.github.hykes.codegen.constants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * 内置参数
 * @author: hehaiyangwork@qq.com
 * @date: 2017/4/16
 */
public class DefaultParams {

    public static Map<String, String> getInHouseVariables() {
        Map<String, String> params = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        params.put("Year", String.valueOf(calendar.get(Calendar.YEAR)));
        params.put("Month", String.valueOf(calendar.get(Calendar.MONTH) + 1));
        params.put("Date", String.valueOf(calendar.get(Calendar.DATE)));
        params.put("Day", String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        params.put("Hour", String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)));
        params.put("Minute", String.valueOf(calendar.get(Calendar.MINUTE)));
        params.put("Second", String.valueOf(calendar.get(Calendar.SECOND)));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        params.put("Now", formatter.format(calendar.getTime()));
        return params;
    }

}
