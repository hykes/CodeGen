package com.github.hykes.codegen.configurable;

import com.github.hykes.codegen.configurable.model.Configs;
import com.github.hykes.codegen.configurable.model.Templates;
import com.github.hykes.codegen.configurable.model.Variables;
import com.intellij.openapi.components.*;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.Nullable;

/**
 * 插件配置管理
 *
 * @author: hehaiyangwork@gmail.com
 * @date: 2017/3/17
 */
@State(name = "CodeGen", storages = { @Storage(value = "codegen.xml") })
public class SettingManager implements PersistentStateComponent<SettingManager> {

    public static SettingManager getInstance() {
        return ServiceManager.getService(SettingManager.class);
    }

    private Configs configs;

    private Variables variables;

    private Templates templates;

    public SettingManager() {
        this.configs = new Configs();
        this.variables = new Variables();
        this.templates = new Templates();
    }

    @Override
    @Nullable
    public SettingManager getState() {
        return this;
    }

    @Override
    public void loadState(SettingManager formatSetting) {
        XmlSerializerUtil.copyBean(formatSetting, this);
    }

    public Configs getConfigs() {
        return configs;
    }

    public void setConfigs(Configs configs) {
        this.configs = configs;
    }

    public Variables getVariables() {
        return variables;
    }

    public void setVariables(Variables variables) {
        this.variables = variables;
    }

    public Templates getTemplates() {
        return templates;
    }

    public void setTemplates(Templates templates) {
        this.templates = templates;
    }

}
