package com.github.hykes.codegen.model;

import java.io.Serializable;

/**
 * @author hehaiyangwork@gmail.com
 * @date 2017/12/30
 */
public class ExportTemplate implements Serializable {

    private static final long serialVersionUID = 5626695243379477915L;

    public ExportTemplate() {
    }

    public ExportTemplate(String name, byte[] bytes) {
        this.name = name;
        this.bytes = bytes;
    }

    private String name;

    private byte[] bytes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
}
