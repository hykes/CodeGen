package com.github.hykes.codegen.model;

import java.io.Serializable;
import java.util.List;

/**
 * Desc:
 * Mail: hehaiyangwork@qq.com
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
}
