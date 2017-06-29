package me.hehaiyang.codegen.setting.ui;

import com.intellij.ui.IdeBorderFactory;
import me.hehaiyang.codegen.setting.SettingManager;
import me.hehaiyang.codegen.setting.UIConfigurable;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

/**
 * Desc: 通用配置设置面板
 * Mail: hehaiyang@terminus.io
 * Date: 2017/5/19
 */
public class ConfigUI extends JPanel implements UIConfigurable {

    final JRadioButton textRadio = new JRadioButton("Use Text", true);
    final JRadioButton databaseRadio = new JRadioButton("Use Database");

    private final SettingManager settingManager = SettingManager.getInstance();

    public ConfigUI() {
        init();
        setConfig(settingManager.getConfigSetting());
    }

    private void init() {
        setLayout(new BorderLayout());
        JPanel thisPanel = this;

        textRadio.setMnemonic('T');
        textRadio.setToolTipText("generate code by text");

        textRadio.setMnemonic('D');
        textRadio.setToolTipText("generate code by database");

        JPanel generate = new JPanel(new BorderLayout());
        generate.setBorder(IdeBorderFactory.createTitledBorder("Generation Type", true));
        thisPanel.add(thisPanel = new JPanel(new BorderLayout()), BorderLayout.NORTH);
        thisPanel.add(generate, BorderLayout.NORTH);

        ButtonGroup group = new ButtonGroup();
        group.add(textRadio);
        group.add(databaseRadio);

        generate.add(textRadio, BorderLayout.NORTH);
        generate.add(generate = new JPanel(new BorderLayout()), BorderLayout.SOUTH);
        generate.add(databaseRadio, BorderLayout.NORTH);
    }

    public void setConfig(@NotNull ConfigSetting configuration) {
        textRadio.setSelected(configuration.isTextRadio());
        databaseRadio.setSelected(configuration.isDatabaseRadio());
    }

    @Override
    public boolean isModified() {
        ConfigSetting configSetting = settingManager.getConfigSetting();
        if(configSetting.isDatabaseRadio() && !databaseRadio.isSelected()){
            return true;
        }
        if(configSetting.isTextRadio() && !textRadio.isSelected()){
            return true;
        }
        return false;
    }

    @Override
    public void apply() {
        settingManager.getConfigSetting().setDatabaseRadio(databaseRadio.isSelected());
        settingManager.getConfigSetting().setTextRadio(textRadio.isSelected());
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
