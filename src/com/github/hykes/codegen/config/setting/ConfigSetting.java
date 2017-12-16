package com.github.hykes.codegen.config.setting;

/**
 * Desc:
 * Mail: hehaiyangwork@qq.com
 * Date: 2017/5/10
 */
public class ConfigSetting {

  private String ignoreFields = "id,created_at,updated_at";

  public String getIgnoreFields() {
    return ignoreFields;
  }

  public void setIgnoreFields(String ignoreFields) {
    this.ignoreFields = ignoreFields;
  }
}
