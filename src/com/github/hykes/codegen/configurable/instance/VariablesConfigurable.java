package com.github.hykes.codegen.configurable.instance;

import com.github.hykes.codegen.configurable.ui.VariableUI;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * 变量配置
 *
 * @author hehaiyangwork@gmail.com
 * @date 2017/03/17
 */
public class VariablesConfigurable implements Configurable {

    private VariableUI variableUI;

    @Override
    @Nullable
    public JComponent createComponent() {
        if(variableUI == null) {
            variableUI = new VariableUI();
        }
        return variableUI.$$$getRootComponent$$$();
    }

    @Override
    public boolean isModified() {
        return variableUI != null && variableUI.isModified();
    }

    @Override
    public void apply() throws ConfigurationException {
        if(variableUI != null){
            variableUI.apply();
        }
    }

    @Override
    public void reset() {
        if(variableUI != null){
            variableUI.reset();
        }
    }

    @Override
    public void disposeUIResources() {
        this.variableUI = null;
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "Variables";
    }
}
