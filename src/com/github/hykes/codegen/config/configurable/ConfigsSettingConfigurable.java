package com.github.hykes.codegen.config.configurable;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.github.hykes.codegen.config.ui.ConfigUI;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Desc: 配置
 * Mail: hehaiyangwork@qq.com
 * Date: 2017/3/17
 */
public class ConfigsSettingConfigurable implements SearchableConfigurable {

    private ConfigUI configUI;

    @Override
    @NotNull
    public String getId() {
        return "codeGen.config";
    }

    @Nullable
    @Override
    public Runnable enableSearch(String s) {
        return null;
    }

    @Override
    @Nls
    public String getDisplayName() {
        return this.getId();
    }

    @Override
    @Nullable
    public String getHelpTopic() {
        return this.getId();
    }

    @Override
    @Nullable
    public JComponent createComponent() {
        if(configUI == null) {
            configUI = new ConfigUI();
        }
        return configUI;
    }

    @Override
    public boolean isModified() {
        return configUI != null && configUI.isModified();
    }

    @Override
    public void apply() throws ConfigurationException {
        if(configUI != null){
            configUI.apply();
        }
    }

    @Override
    public void reset() {
        if(configUI != null){
            configUI.reset();
        }
    }

    @Override
    public void disposeUIResources() {
        this.configUI = null;
    }

}
