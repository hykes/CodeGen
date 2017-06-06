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

    private JCheckBox markdownBox;
    private JCheckBox databaseBox;
    private JCheckBox sqlScriptBox;

    private final SettingManager settingManager = SettingManager.getInstance();

    public ConfigUI() {
        init();
        setConfig(settingManager.getConfigSetting());
    }

    private void init() {
        setLayout(new BorderLayout());
        JPanel c = this;

        markdownBox = new JCheckBox("Use MarkDown Schema");
        markdownBox.setMnemonic('M');
        markdownBox.setToolTipText("generate code by markdown schema");
        markdownBox.addActionListener(e -> markdownChanged());
        markdownBox.setEnabled(false);

        databaseBox = new JCheckBox("Use Database Schema");
        databaseBox.setMnemonic('D');
        databaseBox.setToolTipText("generate code by database schema");
        databaseBox.addActionListener(e -> databaseChanged());

        sqlScriptBox = new JCheckBox("Use SqlScript Schema");
        sqlScriptBox.setMnemonic('S');
        sqlScriptBox.setToolTipText("generate code by sqlScript schema");
        sqlScriptBox.setEnabled(false);

        JPanel generate = new JPanel(new BorderLayout());
        generate.setBorder(IdeBorderFactory.createTitledBorder("Generate", true));
        c.add(c = new JPanel(new BorderLayout()), BorderLayout.NORTH);
        c.add(generate, BorderLayout.NORTH);

        generate.add(markdownBox, BorderLayout.NORTH);
        generate.add(generate = new JPanel(new BorderLayout()), BorderLayout.SOUTH);
        generate.add(databaseBox, BorderLayout.NORTH);
        generate.add(generate = new JPanel(new BorderLayout()), BorderLayout.SOUTH);
        generate.add(sqlScriptBox, BorderLayout.NORTH);
    }

    public void setConfig(@NotNull ConfigSetting configuration) {
        markdownBox.setSelected(configuration.isMarkdownBox());
        databaseBox.setSelected(configuration.isDatabaseBox());
        databaseChanged();
    }

    private void markdownChanged() {
        databaseBox.setSelected(!markdownBox.isSelected());
    }

    private void databaseChanged() {
        markdownBox.setSelected(!databaseBox.isSelected());
    }

    @Override
    public boolean isModified() {
        ConfigSetting setting = settingManager.getConfigSetting();
        if(setting.isDatabaseBox() && !databaseBox.isSelected()){
            return true;
        }
        if(setting.isMarkdownBox() && !markdownBox.isSelected()){
            return true;
        }
        return false;
    }

    @Override
    public void apply() {
        settingManager.getConfigSetting().setDatabaseBox(databaseBox.isSelected());
        settingManager.getConfigSetting().setMarkdownBox(markdownBox.isSelected());
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
