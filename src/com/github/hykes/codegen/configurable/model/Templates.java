package com.github.hykes.codegen.configurable.model;

import com.github.hykes.codegen.model.CodeGroup;
import com.github.hykes.codegen.model.CodeRoot;
import com.github.hykes.codegen.model.CodeTemplate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自定义模版 model
 * @author hehaiyangwork@gmail.com
 * @date 2017/05/10
 */
public class Templates implements Serializable {

    private static final long serialVersionUID = 8790162098133834684L;

    /**
     * 模板的根root
     */
    private List<CodeRoot> roots = new ArrayList<>();

    public Templates() {}

    /**
     * 获取组和模板的映射map
     */
    public Map<String, List<CodeGroup>> getGroupsMap(){
        Map<String, List<CodeGroup>> result = new HashMap<>();
        roots.forEach(it -> result.put(it.getId(), it.getGroups()));
        return result;
    }


    /**
     * 获取组和模板的映射map
     */
    public Map<String, List<CodeTemplate>> getTemplatesMap(){
        Map<String, List<CodeTemplate>> result = new HashMap<>();
        roots.forEach(root ->
                root.getGroups().forEach(it -> result.put(it.getId(), it.getTemplates())));
        return result;
    }

    public List<CodeRoot> getRoots() {
        return roots;
    }

    public void setRoots(List<CodeRoot> roots) {
        this.roots = roots;
    }
}
