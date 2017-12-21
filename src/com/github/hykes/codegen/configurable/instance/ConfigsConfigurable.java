package com.github.hykes.codegen.configurable.instance;

import com.github.hykes.codegen.configurable.ui.ConfigUI;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * 可选项配置
 *
 * @author hehaiyangwork@gmail.com
 * @date 2017/03/17
 */
public class ConfigsConfigurable implements Configurable {

    private ConfigUI configUI;

    @Override
    @Nullable
    public JComponent createComponent() {
        if(configUI == null) {
            configUI = new ConfigUI();
        }
        return configUI.$$$getRootComponent$$$();
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

    @Nls
    @Override
    public String getDisplayName() {
        return "CodeGen";
    }
}
