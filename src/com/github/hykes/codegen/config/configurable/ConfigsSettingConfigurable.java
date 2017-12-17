package com.github.hykes.codegen.config.configurable;

import com.github.hykes.codegen.config.ui.ConfigUI;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.UnnamedConfigurable;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * 可选项配置
 *
 * @author: hehaiyangwork@qq.com
 * @date: 2017/03/17
 */
public class ConfigsSettingConfigurable implements UnnamedConfigurable {

    private ConfigUI configUI;

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
