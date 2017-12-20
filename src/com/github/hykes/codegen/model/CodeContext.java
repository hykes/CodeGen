package com.github.hykes.codegen.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Desc:
 * Mail: hehaiyangwork@gmail.com
 * Date: 17/3/21
 */
public class CodeContext implements Serializable {

    private static final long serialVersionUID = 6235968905610310027L;

    public CodeContext() {}

    public CodeContext(String model, String table, List<Field> fields) {
        this.model = model;
        this.table = table;
        this.fields = fields;
    }

    private String model;

    private String table;

    private List<Field> fields;

    private Map<String, String> $;

    public Map<String, String> get$() {
        return $;
    }

    public void set$(Map<String, String> $) {
        this.$ = $;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }
}

