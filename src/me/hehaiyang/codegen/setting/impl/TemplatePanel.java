package me.hehaiyang.codegen.setting.impl;

import com.google.common.collect.ImmutableMap;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import lombok.Data;
import me.hehaiyang.codegen.model.CodeTemplate;
import me.hehaiyang.codegen.setting.FormatSetting;
import me.hehaiyang.codegen.setting.TemplateEditPane;
import me.hehaiyang.codegen.utils.ParseUtils;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Desc:
 * Mail: hehaiyang@terminus.io
 * Date: 2017/5/10
 */
@Data
public class TemplatePanel extends JBPanel {

    private JPanel leftPanel;
    private DefaultTableModel templateTableModel;
    private JBTable templateTable;

    private JPanel rightPanel;
    private TemplateEditPane editPane;

    public TemplatePanel(FormatSetting settings) {
        super();
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        leftPanel = new JPanel(new BorderLayout());
        rightPanel = new JPanel(new BorderLayout());

        templateTable = new JBTable();
        templateTable.setRowSelectionAllowed(true);
        templateTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  //单选
        refresh(settings);

        templateTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = templateTable.getSelectedRow(); //获得选中行索引
            if(selectedRow != -1) {
                String id = (String) templateTableModel.getValueAt(selectedRow, 0);
                rightPanel.removeAll();
                editPane = new TemplateEditPane(settings, id);
                rightPanel.add(editPane, BorderLayout.CENTER);
            }
        });

        // 使用滚动条
        JScrollPane scrollPane = new JBScrollPane(templateTable,JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setViewportView(templateTable);
        scrollPane.setPreferredSize(new Dimension(200,100));

        leftPanel.add(scrollPane,BorderLayout.CENTER);
        leftPanel.add(getTemplateActionPanel() , BorderLayout.SOUTH);

        add(leftPanel, BorderLayout.WEST);

        editPane = new TemplateEditPane();
        editPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        if(templateTable.getRowCount() > 0){
            templateTable.setRowSelectionInterval(0, 0);
        }

        rightPanel.add(editPane, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.CENTER);
    }


    /**
     * 模版列表
     * @param settings
     * @return
     */
    private DefaultTableModel getTemplateTableModel(FormatSetting settings){
        String[] columnNames = {"ID","Template List"};

        Object[][] tableVales = new String[settings.getCodeTemplates().size()][2];
        int i = 0;
        for (CodeTemplate template : settings.getCodeTemplates().values()) {
            tableVales[i][0] = template.getId();
            tableVales[i][1] = template.getDisplay();
            i++;
        }
        return new DefaultTableModel(tableVales,columnNames) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }


    /**
     * 模版操作按钮
     * @return
     */
    private JPanel getTemplateActionPanel(){
        JPanel paramActionPanel = new JPanel(new GridBagLayout());
        paramActionPanel.setPreferredSize(new Dimension(200,30));
        paramActionPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        JButton addParam = new JButton("+");
        addParam.setUI(new BasicButtonUI());// 恢复基本视觉效果
        addParam.setContentAreaFilled(false);// 设置按钮透明
        addParam.setFont(new Font("粗体", Font.PLAIN, 16));// 按钮文本样式
        addParam.setMargin(new Insets(0, 0, 0, 0));// 按钮内容与边框距离

        addParam.addActionListener( e ->{
            String id = UUID.randomUUID().toString();
            String []rowValues = {id, "Unnamed"};
            templateTableModel.addRow(rowValues);
            rightPanel.removeAll();
            editPane = new TemplateEditPane(id);
            rightPanel.add(editPane, BorderLayout.CENTER);
        });

        paramActionPanel.add(addParam);

        JButton delParam = new JButton("-");
        delParam.setUI(new BasicButtonUI());// 恢复基本视觉效果
        delParam.setContentAreaFilled(false);// 设置按钮透明
        delParam.setFont(new Font("粗体", Font.PLAIN, 16));// 按钮文本样式
        delParam.setMargin(new Insets(0, 0, 0, 0));// 按钮内容与边框距离

        delParam.addActionListener( e ->{
            int selectedRow = templateTable.getSelectedRow();
            if(selectedRow!=-1){
                FormatSetting setting = FormatSetting.getInstance();
                String id = (String) templateTableModel.getValueAt(selectedRow, 0);
                setting.removeCodeTemplate(id);
                templateTableModel.removeRow(selectedRow);

                if(templateTable.getRowCount() > 0){
                    templateTable.setRowSelectionInterval(0, 0);
                }else{
                    rightPanel.removeAll();
                    editPane = new TemplateEditPane();
                    rightPanel.add(editPane, BorderLayout.CENTER);
                }
            }
        });
        paramActionPanel.add(delParam);
        return paramActionPanel;
    }

    public void refresh(FormatSetting settings) {
        templateTableModel = getTemplateTableModel(settings);

        templateTable.setModel(templateTableModel);

        // 隐藏模版ID
        TableColumnModel columnModel = templateTable.getColumnModel();
        TableColumn column = columnModel.getColumn(0);
        column.setMinWidth(0);
        column.setMaxWidth(0);
        column.setWidth(0);
        column.setPreferredWidth(0);
    }



    public static void main(String[] args) {
        JFrame jFrame = new JFrame();
        FormatSetting setting = new FormatSetting();
        setting.setParams(ImmutableMap.of("email", "hehaiyangwork@qq.com"));

        Map<String, CodeTemplate> codeTemplates = new HashMap<>();
        try {
            String javaId = UUID.randomUUID().toString();
            String xmlId = UUID.randomUUID().toString();
            String sqlId = UUID.randomUUID().toString();
            codeTemplates.put(javaId, new CodeTemplate(javaId, "Java Template", "java", "{{model}}", getTemplateContext("/template/JavaTemplate.hbs")));
            codeTemplates.put(xmlId, new CodeTemplate(xmlId, "Xml Template", "xml", "{{model}}", getTemplateContext("/template/XmlTemplate.hbs")));
            codeTemplates.put(sqlId, new CodeTemplate(sqlId, "Sql Template", "sql", "{{model}}", getTemplateContext("/template/SqlTemplate.hbs")));

        }catch (IOException io){
            // do nothing
        }
        setting.setCodeTemplates(codeTemplates);

        jFrame.getContentPane().add(new TemplatePanel(setting), BorderLayout.CENTER);
        jFrame.setSize(300, 240);
        jFrame.setLocation(300, 200);
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private static String getTemplateContext(String path) throws IOException{
        InputStream template = FormatSetting.class.getResourceAsStream(path);
        return ParseUtils.stream2String(template);
    }

}
