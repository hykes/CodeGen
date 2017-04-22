package me.hehaiyang.codegen.constants;

import com.google.common.collect.Maps;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

/**
 * Desc: 内置参数
 * Mail: hehaiyang@terminus.io
 * Date: 2017/4/16
 */
public class DefaultParams {

    public static Map<String, String> getInstance () {
        Map<String, String> params = Maps.newHashMap();
        Calendar calendar = Calendar.getInstance();
        params.put("Year", String.valueOf(calendar.get(Calendar.YEAR)));
        params.put("Month", String.valueOf(calendar.get(Calendar.MONTH) + 1));
        params.put("Date", String.valueOf(calendar.get(Calendar.DATE)));
        params.put("Hour", String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)));
        params.put("Minute", String.valueOf(calendar.get(Calendar.MINUTE)));
        params.put("Second", String.valueOf(calendar.get(Calendar.SECOND)));
        params.put("Week", String.valueOf(calendar.get(Calendar.DAY_OF_WEEK)));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        params.put("Now", formatter.format(calendar.getTime()));
        return params;
    }

}
