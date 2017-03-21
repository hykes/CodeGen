package me.hehaiyang.codegen.model;

import java.util.Date;
import java.util.List;

/**
 * Desc: Mapper模版实体
 * Mail: hehaiyang@terminus.io
 * Date: 16/4/21
 */

public class Mapper extends Base {

    public Mapper(){}

    public Mapper(String modelName, String tableName, List<Field> fields){
        super(modelName, new Date());
        this.tableName = tableName;
        this.fields = fields;
    }

    private String tableName;

    private List<Field> fields;

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

}
