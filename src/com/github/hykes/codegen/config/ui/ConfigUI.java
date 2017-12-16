package com.github.hykes.codegen.config.ui;

import com.github.hykes.codegen.config.SettingManager;
import com.github.hykes.codegen.config.UIConfigurable;
import com.github.hykes.codegen.config.setting.ConfigSetting;
import com.intellij.ui.IdeBorderFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

/**
 * Desc: 通用配置设置面板
 * Mail: hehaiyangwork@qq.com
 * Date: 2017/5/19
 */
public class ConfigUI extends JPanel implements UIConfigurable {

    final JTextField ignoreFields = new JTextField();

    private final SettingManager settingManager = SettingManager.getInstance();

    private JPanel thisPanel;

    public ConfigUI() {
        thisPanel = this;
        init();
        setConfig(settingManager.getConfigSetting());
    }

    private void init() {
        setLayout(new BorderLayout());

        // 设置需要忽略的字段
        JPanel ignorePane = new JPanel(new BorderLayout());
        ignorePane.setBorder(IdeBorderFactory.createTitledBorder("The fields you want to ignore", true));
        thisPanel.add(ignorePane, BorderLayout.CENTER);
        JLabel fieldLabel = new JLabel("Fields:");
        fieldLabel.setPreferredSize(new Dimension(50, 25));
        ignorePane.add(fieldLabel, BorderLayout.WEST);
        ignoreFields.setPreferredSize(new Dimension(300, 25));
        ignorePane.add(ignoreFields, BorderLayout.CENTER);
    }

    public void setConfig(@NotNull ConfigSetting configuration) {
        ignoreFields.setText(configuration.getIgnoreFields());
    }

    @Override
    public boolean isModified() {
        ConfigSetting configSetting = settingManager.getConfigSetting();
        if (!configSetting.getIgnoreFields().equals(ignoreFields.getText())) {
            return true;
        }
        return false;
    }

    @Override
    public void apply() {
        settingManager.getConfigSetting().setIgnoreFields(ignoreFields.getText());
    }

    @Override
    public void reset() {
        setConfig(settingManager.getConfigSetting());
    }

    public static void main(String[] args) {
        JFrame test = new JFrame();
        test.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        final JPanel comp = new JPanel(new BorderLayout());
        comp.add(new ConfigUI(), BorderLayout.CENTER);
        test.getContentPane().add(comp);
        test.setSize(450, 450);
        test.setVisible(true);
    }
}
