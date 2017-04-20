package me.hehaiyang.codegen.constants;

import com.google.common.collect.Maps;
import org.joda.time.DateTime;

import java.util.Map;

/**
 * Desc:
 * Mail: hehaiyang@terminus.io
 * Date: 2017/4/16
 */
public class DefaultParams {

    public static Map<String, String> params = Maps.newHashMap();

    public DefaultParams() {
        DateTime dte = DateTime.now();
        params.put("Year", String.valueOf(dte.getYear()));
        params.put("YearOfCentury", String.valueOf(dte.getYearOfCentury()));
        params.put("YearOfEra", String.valueOf(dte.getYearOfEra()));
        params.put("Month", String.valueOf(dte.getMonthOfYear()));
        params.put("DayOfMonth", String.valueOf(dte.getDayOfMonth()));
        params.put("DayOfWeek", String.valueOf(dte.getDayOfWeek()));
        params.put("Hours", String.valueOf(dte.getHourOfDay()));
        params.put("Mills", String.valueOf(dte.getMinuteOfHour()));
        params.put("Second", String.valueOf(dte.getSecondOfMinute()));
        params.put("Now", dte.toString());
    }
}
