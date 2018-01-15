package com.github.hykes.codegen.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Desc: code group的所在组
 * <p>
 * Mail: chk19940609@gmail.com
 * Created by IceMimosa
 * Date: 2018/1/3
 */
public class CodeRoot implements Serializable {
    private static final long serialVersionUID = -200551574080571684L;

    /**
     * 根ID
     */
    private String id;

    /**
     * 根目录组的名称
     */
    private String name;

    /**
     * 分组
     */
    private List<CodeGroup> groups;

    public CodeRoot() {
    }

    public CodeRoot(String id, String name, List<CodeGroup> groups) {
        this.id = id;
        this.name = name;
        this.groups = groups;
    }

    public static CodeRoot fromName(String name) {
        return new CodeRoot(UUID.randomUUID().toString(), name, new ArrayList<>());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CodeGroup> getGroups() {
        return groups;
    }

    public void setGroups(List<CodeGroup> groups) {
        this.groups = groups;
    }

    @Override
    public String toString() {
        return "CodeRoot{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", groups=" + groups +
                '}';
    }
}
