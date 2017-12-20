package com.github.hykes.codegen.configurable.model;

import java.io.Serializable;

/**
 * 自定义配置 model
 * @author: hehaiyangwork@gmail.com
 * @date: 2017/05/10
 */
public class Configs implements Serializable {

  private static final long serialVersionUID = 8108800492342778182L;

  private String ignoreFields = "id,created_at,updated_at";

  public String getIgnoreFields() {
    return ignoreFields;
  }

  public void setIgnoreFields(String ignoreFields) {
    this.ignoreFields = ignoreFields;
  }
}
