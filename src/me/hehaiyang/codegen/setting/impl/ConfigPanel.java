package me.hehaiyang.codegen.setting.impl;

import com.google.common.collect.ImmutableMap;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import me.hehaiyang.codegen.setting.FormatSetting;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;

/**
 * Desc:
 * Mail: hehaiyang@terminus.io
 * Date: 2017/5/10
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ConfigPanel extends JBPanel {

    public ConfigPanel(FormatSetting settings) {
        super();
        setLayout(new BorderLayout());

    }

    public static void main(String[] args) {
        JFrame jFrame = new JFrame();
        FormatSetting setting = new FormatSetting();
        setting.setParams(ImmutableMap.of("email", "hehaiyangwork@qq.com"));
        jFrame.getContentPane().add(new ConfigPanel(setting), BorderLayout.CENTER);
        jFrame.setSize(300, 240);
        jFrame.setLocation(300, 200);
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
