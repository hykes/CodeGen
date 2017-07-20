package me.hehaiyang.codegen.config.setting;

import me.hehaiyang.codegen.constants.DefaultTemplates;
import me.hehaiyang.codegen.model.CodeGroup;
import me.hehaiyang.codegen.model.CodeTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Desc:
 * Mail: hehaiyang@terminus.io
 * Date: 2017/5/10
 */
public class TemplatesSetting {

    private List<CodeGroup> groups = new ArrayList<>();

    public TemplatesSetting() {
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
