package me.hehaiyang.codegen.setting;

import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTabbedPane;
import com.intellij.ui.table.JBTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import me.hehaiyang.codegen.model.CodeTemplate;
import me.hehaiyang.codegen.setting.impl.ConfigPanel;
import me.hehaiyang.codegen.setting.impl.ParamsPanel;
import me.hehaiyang.codegen.setting.impl.TemplatePanel;
import me.hehaiyang.codegen.setting.impl.WikiTextArea;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.Map;
import java.util.UUID;

/**
 * Desc: 配置面板
 * Mail: hehaiyangwork@qq.com
 * Date: 2017/3/17
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class FormatForm extends JPanel{

    private JBTabbedPane tabbedPanel;

    private ParamsPanel paramPanel;
    private TemplatePanel templatePanel;
    private ConfigPanel configPanel;

    public FormatForm(FormatSetting settings) {
        super();
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension());

        tabbedPanel = new JBTabbedPane();

        add(tabbedPanel, BorderLayout.CENTER);

        configPanel = new ConfigPanel(settings);
        tabbedPanel.add("Config", configPanel);

        paramPanel = new ParamsPanel(settings);
        tabbedPanel.add("Variables", paramPanel);

        templatePanel = new TemplatePanel(settings);
        tabbedPanel.add("Templates", templatePanel);

        tabbedPanel.add("Wiki", new JBScrollPane(new WikiTextArea()));
    }


    /**
     * 文档居中处理
     * @param frameW 文档宽度
     * @param frameH 文档高度
     * @return
     */
    public Point getCenterPoint(int frameW,int frameH){

        Toolkit toolkit=Toolkit.getDefaultToolkit();
        Dimension dimension=toolkit.getScreenSize();
        Integer screenW = dimension.width;
        Integer screenH = dimension.height;

        Integer centerX = screenW/2-frameW/2;
        Integer centerY = screenH/2-frameH/2;

        return new Point(centerX,centerY);
    }


    public static void main(String[] args) {
        JFrame f = new JFrame(" JTreeDemo1 ");
        f.getContentPane().add(new FormatForm(FormatSetting.getInstance()), BorderLayout.CENTER);
        f.setSize(300, 240);
        //f.pack();
        f.setLocation(300, 200);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
