package me.hehaiyang.codegen.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Desc:
 * Mail: hehaiyang@terminus.io
 * Date: 17/3/21
 */
public class CodeContext implements Serializable {

    private static final long serialVersionUID = 6235968905610310027L;

    public CodeContext() {}

    public CodeContext(String model, String modelName, String table, String tableName, List<Field> fields) {
        this.model = model;
        this.modelName = modelName;
        this.table = table;
        this.tableName = tableName;
        this.fields = fields;
    }

    private String model;

    private String modelName;

    private String table;

    private String tableName;

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

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }
}

