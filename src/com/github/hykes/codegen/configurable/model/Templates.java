package com.github.hykes.codegen.configurable.model;

import com.github.hykes.codegen.constants.DefaultTemplates;
import com.github.hykes.codegen.model.CodeTemplate;
import com.github.hykes.codegen.model.CodeGroup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自定义模版 model
 * @author: hehaiyangwork@gmail.com
 * @date: 2017/05/10
 */
public class Templates implements Serializable {

    private static final long serialVersionUID = 8790162098133834684L;

    private List<CodeGroup> groups = new ArrayList<>();

    public Templates() {
        groups.addAll(DefaultTemplates.getDefaults());
    }

    public Map<String, List<CodeTemplate>> getTemplatesMap(){
        Map<String, List<CodeTemplate>> result = new HashMap<>();
        groups.forEach( it -> result.put(it.getId(), it.getTemplates()));
        return result;
    }

    public List<CodeGroup> getGroups() {
        return groups;
    }

    public void setGroups(List<CodeGroup> groups) {
        this.groups = groups;
    }
}
