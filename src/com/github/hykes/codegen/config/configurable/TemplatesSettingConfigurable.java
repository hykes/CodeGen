package com.github.hykes.codegen.config.configurable;

import com.github.hykes.codegen.config.ui.TemplatesUI;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Desc: 模版
 * Mail: hehaiyangwork@qq.com
 * Date: 2017/3/17
 */
public class TemplatesSettingConfigurable implements SearchableConfigurable {

    private TemplatesUI templatesUI;

    @Override
    @NotNull
    public String getId() {
        return "codeGen.template";
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
        if(templatesUI == null) {
            templatesUI = new TemplatesUI();
        }
        return templatesUI;
    }

    @Override
    public boolean isModified() {
        return templatesUI != null && templatesUI.isModified();
    }

    @Override
    public void apply() throws ConfigurationException {
        if(templatesUI != null){
            templatesUI.apply();
        }
    }

    @Override
    public void reset() {
        if(templatesUI != null){
            templatesUI.reset();
        }
    }

    @Override
    public void disposeUIResources() {
        this.templatesUI = null;
    }

}
