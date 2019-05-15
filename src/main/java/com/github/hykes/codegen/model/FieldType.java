package com.github.hykes.codegen.model;

import java.io.Serializable;

/**
 * Desc: 数据库字段类型对应的语言字段类型
 *  如 java, kotlin类型
 * Mail: chk19940609@gmail.com
 * Created by IceMimosa
 * Date: 2017/7/4
 */
public class FieldType implements Serializable {
    private static final long serialVersionUID = -4088740233253463308L;

    /**
     * java类型
     */
    private String javaType;

    /**
     * kotlin类型
     */
    private String kotlinType;

    // 其他类型...

    public FieldType(String javaType, String kotlinType) {
        this.javaType = javaType;
        this.kotlinType = kotlinType;
    }

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public String getKotlinType() {
        return kotlinType;
    }

    public void setKotlinType(String kotlinType) {
        this.kotlinType = kotlinType;
    }

    /**
     * 创建对象
     */
    public static FieldType build(String javaType, String kotlinType) {
        return new FieldType(javaType, kotlinType);
    }
    public static FieldType build(String type) {
        return build(type, type);
    }
}
