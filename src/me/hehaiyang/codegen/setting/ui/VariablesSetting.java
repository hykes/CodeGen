package me.hehaiyang.codegen.setting.ui;

import lombok.Data;

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
      params.put("author", "[ your name ]");
      params.put("email", "[ your email ]");
      params.put("mobile", "[ your mobile ]");
    }

}
