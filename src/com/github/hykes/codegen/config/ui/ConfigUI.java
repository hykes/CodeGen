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


    JPanel thisPanel;
    final JRadioButton sqlRadio = new JRadioButton("Use SQL", true);
    final JRadioButton dbRadio = new JRadioButton("Use DB");
    final JTextField ignoreFields = new JTextField();

    private final SettingManager settingManager = SettingManager.getInstance();

    public ConfigUI() {
        this.thisPanel = this;
        init();
        setConfig(settingManager.getConfigSetting());
    }

    private void init() {
        setLayout(new BorderLayout());

        // 设置生成类型, SQL or DB
        sqlRadio.setMnemonic('S');
        sqlRadio.setToolTipText("generate code by SQL");
        sqlRadio.setMnemonic('D');
        sqlRadio.setToolTipText("generate code by DB");
        JPanel generate = new JPanel(new BorderLayout());
        generate.setBorder(IdeBorderFactory.createTitledBorder("Default generation type", true));
        thisPanel.add(thisPanel = new JPanel(new BorderLayout()), BorderLayout.NORTH);
        thisPanel.add(generate, BorderLayout.NORTH);
        ButtonGroup group = new ButtonGroup();
        group.add(sqlRadio);
        group.add(dbRadio);
        generate.add(sqlRadio, BorderLayout.NORTH);
        generate.add(generate = new JPanel(new BorderLayout()), BorderLayout.SOUTH);
        generate.add(dbRadio, BorderLayout.NORTH);

        // 设置需要忽略的字段
        JPanel ignorePane = new JPanel(new BorderLayout());
        ignorePane.setBorder(IdeBorderFactory.createTitledBorder("The fields you want to ignore", true));
        this.add(ignorePane, BorderLayout.CENTER);
        JLabel fieldLabel = new JLabel("Fields:");
        fieldLabel.setPreferredSize(new Dimension(50, 25));
        ignorePane.add(fieldLabel, BorderLayout.WEST);
        ignoreFields.setPreferredSize(new Dimension(300, 25));
        ignorePane.add(ignoreFields, BorderLayout.CENTER);
    }

    public void setConfig(@NotNull ConfigSetting configuration) {
        sqlRadio.setSelected(configuration.isSqlRadio());
        dbRadio.setSelected(configuration.isDbRadio());
        ignoreFields.setText(configuration.getIgnoreFields());
    }

    @Override
    public boolean isModified() {
        ConfigSetting configSetting = settingManager.getConfigSetting();
        if(configSetting.isDbRadio() && !dbRadio.isSelected()){
            return true;
        }
        if(configSetting.isSqlRadio() && !sqlRadio.isSelected()){
            return true;
        }
        if (!configSetting.getIgnoreFields().equals(ignoreFields.getText())) {
            return true;
        }
        return false;
    }

    @Override
    public void apply() {
        settingManager.getConfigSetting().setDbRadio(dbRadio.isSelected());
        settingManager.getConfigSetting().setSqlRadio(sqlRadio.isSelected());
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
