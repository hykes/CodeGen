package com.github.hykes.codegen.config;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.github.hykes.codegen.config.setting.ConfigSetting;
import com.github.hykes.codegen.config.setting.DatabasesSetting;
import com.github.hykes.codegen.config.setting.TemplatesSetting;
import com.github.hykes.codegen.config.setting.VariablesSetting;
import org.jetbrains.annotations.Nullable;

/**
 * Desc:
 * Mail: hehaiyangwork@qq.com
 * Date: 2017/3/17
 */
@State(name = "CodeGen", storages = { @Storage(value = "$APP_CONFIG$/format.xml") })
public class SettingManager implements PersistentStateComponent<SettingManager> {

    public static SettingManager getInstance() {
        return ServiceManager.getService(SettingManager.class);
    }

    private ConfigSetting configSetting;

    private VariablesSetting variablesSetting;

    private TemplatesSetting templatesSetting;

    private DatabasesSetting databasesSetting;

    public SettingManager() {
        this.configSetting = new ConfigSetting();
        this.variablesSetting = new VariablesSetting();
        this.templatesSetting = new TemplatesSetting();
        this.databasesSetting = new DatabasesSetting();
    }

    @Nullable
    public SettingManager getState() {
        return this;
    }

    public void loadState(SettingManager formatSetting) {
        XmlSerializerUtil.copyBean(formatSetting, this);
    }

    public ConfigSetting getConfigSetting() {
        return configSetting;
    }

    public void setConfigSetting(ConfigSetting configSetting) {
        this.configSetting = configSetting;
    }

    public VariablesSetting getVariablesSetting() {
        return variablesSetting;
    }

    public void setVariablesSetting(VariablesSetting variablesSetting) {
        this.variablesSetting = variablesSetting;
    }

    public TemplatesSetting getTemplatesSetting() {
        return templatesSetting;
    }

    public void setTemplatesSetting(TemplatesSetting templatesSetting) {
        this.templatesSetting = templatesSetting;
    }

    public DatabasesSetting getDatabasesSetting() {
        return databasesSetting;
    }

    public void setDatabasesSetting(DatabasesSetting databasesSetting) {
        this.databasesSetting = databasesSetting;
    }
}
