package me.hehaiyang.codegen.config.setting;

import lombok.Data;
import me.hehaiyang.codegen.constants.DefaultParams;

import java.util.HashMap;
import java.util.Map;

/**
 * Desc:
 * Mail: hehaiyang@terminus.io
 * Date: 2017/5/10
 */
@Data
public class VariablesSetting {

   private Map<String, String> params = new HashMap<>();

    public VariablesSetting() {
        params.putAll(DefaultParams.getDefaultVariables());
    }

}
