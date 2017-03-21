package me.hehaiyang.codegen.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Desc: 基础定义
 * Mail: hehaiyang@terminus.io
 * Date: 16/4/22
 */
public class Base implements Serializable {

    private static final long serialVersionUID = -3208394491599592217L;

    public Base(){}

    public Base(String modelName, Date createdAt){
        this.modelName = modelName;
        this.createdAt = createdAt;
    }

    /**
     * model名称
     */
    private String modelName;

    /**
     * 生成时间
     */
    private Date createdAt;

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
