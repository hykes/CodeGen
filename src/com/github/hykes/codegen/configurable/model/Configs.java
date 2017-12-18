package com.github.hykes.codegen.configurable.model;

import java.io.Serializable;

/**
 * 自定义配置 model
 * @author: hehaiyangwork@qq.com
 * @date: 2017/05/10
 */
public class Configs implements Serializable {

  private static final long serialVersionUID = 8108800492342778182L;

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
