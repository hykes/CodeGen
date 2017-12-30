package com.github.hykes.codegen.model;

import java.io.ByteArrayInputStream;
import java.io.Serializable;

/**
 * @author hehaiyangwork@gmail.com
 * @date 2017/12/30
 */
public class ExportTemplate implements Serializable {

    private static final long serialVersionUID = 5626695243379477915L;

    public ExportTemplate() {
    }

    public ExportTemplate(String name, ByteArrayInputStream inputStream) {
        this.name = name;
        this.inputStream = inputStream;
    }

    private String name;

    private ByteArrayInputStream inputStream;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ByteArrayInputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(ByteArrayInputStream inputStream) {
        this.inputStream = inputStream;
    }
}
