package com.github.hykes.codegen.config.setting;

/**
 * Desc:
 * Mail: hehaiyangwork@qq.com
 * Date: 2017/5/10
 */
public class ConfigSetting {

  private boolean sqlRadio = false;
  private boolean dbRadio = true;

  private String ignoreFields = "id,created_at,updated_at";

  public boolean isSqlRadio() {
    return sqlRadio;
  }

  public void setSqlRadio(boolean sqlRadio) {
    this.sqlRadio = sqlRadio;
  }

  public boolean isDbRadio() {
    return dbRadio;
  }

  public void setDbRadio(boolean dbRadio) {
    this.dbRadio = dbRadio;
  }

  public String getIgnoreFields() {
    return ignoreFields;
  }

  public void setIgnoreFields(String ignoreFields) {
    this.ignoreFields = ignoreFields;
  }
}
