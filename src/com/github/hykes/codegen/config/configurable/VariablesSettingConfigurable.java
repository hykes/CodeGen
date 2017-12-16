package com.github.hykes.codegen.config.configurable;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.github.hykes.codegen.config.ui.VariablesUI;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Desc: 变量
 * Mail: hehaiyangwork@qq.com
 * Date: 2017/3/17
 */
public class VariablesSettingConfigurable implements SearchableConfigurable {

    private VariablesUI variablesUI;

    @Override
    @NotNull
    public String getId() {
        return "codeGen.variables";
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
        if(variablesUI == null) {
            variablesUI = new VariablesUI();
        }
        return variablesUI;
    }

    @Override
    public boolean isModified() {
        return variablesUI != null && variablesUI.isModified();
    }

    @Override
    public void apply() throws ConfigurationException {
        if(variablesUI != null){
            variablesUI.apply();
        }
    }

    @Override
    public void reset() {
        if(variablesUI != null){
            variablesUI.reset();
        }
    }

    @Override
    public void disposeUIResources() {
        this.variablesUI = null;
    }

}
