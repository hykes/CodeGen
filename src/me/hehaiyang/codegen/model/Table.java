package me.hehaiyang.codegen.model;

import me.hehaiyang.codegen.utils.BuilderUtil;

import java.io.Serializable;
import java.util.List;

/**
 * Desc: 表对象
 * Mail: chk19940609@gmail.com
 * Created by IceMimosa
 * Date: 2017/7/11
 */
public class Table implements Serializable {

    private static final long serialVersionUID = 6739294315192751908L;

    /**
     * 表对应的model名称（驼峰）
     */
    private String modelName;

    /**
     * 表名称
     */
    private String tableName;

    /**
     * 表中的字段
     */
    private List<Field> fields;


    public Table(List<Field> fields) {
        this.fields = fields;
    }

    public Table(String tableName, List<Field> fields) {
        this.setTableName(tableName);
        this.fields = fields;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
        this.tableName = BuilderUtil.camelToUnderline(modelName);
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
        this.modelName = BuilderUtil.underlineToCamel(tableName);
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }
}
