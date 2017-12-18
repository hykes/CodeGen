package com.github.hykes.codegen.config.setting;

import com.github.hykes.codegen.constants.DefaultParams;
import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.Map;

/**
 * Desc:
 * Mail: hehaiyangwork@qq.com
 * Date: 2017/5/10
 */
public class VariablesSetting {

    private Map<String, String> params = new HashMap<>();

    public VariablesSetting() {
        Map<String, String> params = Maps.newHashMap();
        params.put("author", "[ your name ]");
        params.put("email", "[ your email ]");
        params.putAll(params);
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }
}
