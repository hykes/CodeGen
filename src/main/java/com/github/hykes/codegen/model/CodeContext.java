package com.github.hykes.codegen.model;

import java.io.Serializable;
import java.util.List;

/**
 * Desc:
 * Mail: hehaiyangwork@gmail.com
 * Date: 17/3/21
 */
public class CodeContext implements Serializable {

    private static final long serialVersionUID = 6235968905610310027L;

    public CodeContext(String model, String table, String comment, List<Field> fields) {
        this.model = model;
        this.table = table;
        this.comment = comment;
        this.fields = fields;
    }

    private String model;

    private String table;

    private String comment;

    private List<Field> fields;

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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

