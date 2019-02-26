package com.github.hykes.codegen.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Desc:
 * Mail: hehaiyangwork@gmail.com
 * Date: 2017/6/6
 */
public class CodeGroup implements Serializable {

    private static final long serialVersionUID = -8843957173498883576L;

    public CodeGroup() {}

    public CodeGroup(String id, String name, Integer level, List<CodeTemplate> templates) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.templates = templates;
    }

    public CodeGroup(String name, Integer level) {
        this(UUID.randomUUID().toString(), name, level, new ArrayList<>());
    }

    /**
     * 分组ID
     */
    private String id;

    /**
     * 分组名称
     */
    private String name;

    /**
     * 优先级
     */
    private Integer level;

    /**
     * 模版列表
     */
    private List<CodeTemplate> templates;

    /**
     * 组所在root的名称
     */
    private String root;

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

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public List<CodeTemplate> getTemplates() {
        return templates;
    }

    public void setTemplates(List<CodeTemplate> templates) {
        this.templates = templates;
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    @Override
    public String toString() {
        return "CodeGroup{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", level=" + level +
                ", templates=" + templates +
                ", root='" + root + '\'' +
                '}';
    }
}
