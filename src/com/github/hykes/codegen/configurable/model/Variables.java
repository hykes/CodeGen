package com.github.hykes.codegen.configurable.model;

import com.google.common.collect.Maps;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义参数 model
 * @author: hehaiyangwork@qq.com
 * @date: 2017/05/10
 */
public class Variables implements Serializable {

    private static final long serialVersionUID = -2336608097560710669L;

    private Map<String, String> params = new HashMap<>();

    public Variables() {
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
