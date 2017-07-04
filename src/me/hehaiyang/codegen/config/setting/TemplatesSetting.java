package me.hehaiyang.codegen.config.setting;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.hehaiyang.codegen.constants.DefaultTemplates;
import me.hehaiyang.codegen.model.CodeGroup;
import me.hehaiyang.codegen.model.CodeTemplate;

import java.util.List;
import java.util.Map;

/**
 * Desc:
 * Mail: hehaiyang@terminus.io
 * Date: 2017/5/10
 */
public class TemplatesSetting {

    private List<CodeGroup> groups = Lists.newArrayList();

    public TemplatesSetting() {
        groups.addAll(DefaultTemplates.getDefaults());
    }

    public Map<String, List<CodeTemplate>> getTemplatesMap(){
        Map<String, List<CodeTemplate>> result = Maps.newHashMap();
        groups.forEach( it -> result.put(it.getId(), it.getTemplates()));
        return result;
    }

    public Map<String, CodeGroup> getGroupMap(){
        Map<String, CodeGroup> result = Maps.newHashMap();
        groups.forEach( it -> result.put(it.getId(), it));
        return result;
    }

    public List<CodeGroup> getGroups() {
        return groups;
    }

    public void setGroups(List<CodeGroup> groups) {
        this.groups = groups;
    }
}
