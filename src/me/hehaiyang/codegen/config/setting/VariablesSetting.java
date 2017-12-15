package me.hehaiyang.codegen.config.setting;

import me.hehaiyang.codegen.constants.DefaultParams;

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
        params.putAll(DefaultParams.getPreDefinedVariables());
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }
}
