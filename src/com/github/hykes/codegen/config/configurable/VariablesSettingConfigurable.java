package com.github.hykes.codegen.config.configurable;

import com.github.hykes.codegen.config.ui.VariablesUI;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.UnnamedConfigurable;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * 变量配置
 *
 * @author: hehaiyangwork@qq.com
 * @date: 2017/03/17
 */
public class VariablesSettingConfigurable implements UnnamedConfigurable {

    private VariablesUI variablesUI;

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
