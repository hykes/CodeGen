package me.hehaiyang.codegen.setting;

import com.intellij.openapi.ui.Messages;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import lombok.Data;
import me.hehaiyang.codegen.model.CodeTemplate;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Desc: 配置面板
 * Mail: hehaiyangwork@qq.com
 * Date: 2017/3/17
 */
@Data
public class FormatForm {

    private JPanel mainPanel;

    private JTabbedPane tabbedPanel;

    private JPanel paramPanel;
    private DefaultTableModel paramsTableModel;
    private JBTable paramsTable;

    private JPanel templatePanel;
    private JPanel leftPanel;
    private DefaultTableModel templateTableModel;
    private JBTable templateTable;

    private JPanel rightPanel;
    private TemplateEditPane editPane;

    private void createUIComponents() {
        // TODO: place custom component creation code here

        paramPanel = new JPanel();
        paramPanel.setLayout(new BorderLayout());

        paramsTable = new JBTable();
        // 单选
        paramsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // 支持滚动
        JScrollPane scrollPane = new JBScrollPane(paramsTable);
        scrollPane.setViewportView(paramsTable);
        paramPanel.add(scrollPane, BorderLayout.CENTER);

    }

    public FormatForm(FormatSetting settings) {
        templatePanel = new JPanel(new BorderLayout());
        templatePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        leftPanel = new JPanel(new BorderLayout());
        rightPanel = new JPanel(new BorderLayout());

        templateTable = new JBTable();
        templateTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  //单选
        refresh(settings);

        templateTable.addMouseListener(new MouseAdapter(){    //鼠标事件
            public void mouseClicked(MouseEvent e){
                int selectedRow = templateTable.getSelectedRow(); //获得选中行索引
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
        leftPanel.add(getParamActionPanel() , BorderLayout.SOUTH);

        templatePanel.add(leftPanel, BorderLayout.WEST);

        Set<String> keSet= settings.getCodeTemplates().keySet();
        if(keSet.iterator().hasNext()){
            editPane = new TemplateEditPane(settings, keSet.iterator().next());
        }else{
            editPane = new TemplateEditPane(settings, null);
        }

        editPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        rightPanel.add(editPane, BorderLayout.CENTER);

        templatePanel.add(rightPanel, BorderLayout.CENTER);

        tabbedPanel.add("Template", templatePanel);

        // 自定义参数 操作面板
        paramsTableModel = getParamsTableModel(settings);
        paramsTable.setModel(paramsTableModel);

        paramPanel.add(actionPanel(), BorderLayout.SOUTH);
    }

    /**
     * 参数列表
     * @param settings
     * @return
     */
    private DefaultTableModel getParamsTableModel(FormatSetting settings){
        // 列名
        String[] columnNames = {"Parameter Key","Parameter Value"};
        // 默认数据
        Map<String, String> map = settings.getParams();
        Object[][] tableVales = new String[map.size()][2];
        Object[] keys = map.keySet().toArray();
        Object[] values = map.values().toArray();
        for (int row = 0; row < tableVales.length; row++) {
            tableVales[row][0] = keys[row];
            tableVales[row][1] = values[row];
        }
        return new DefaultTableModel(tableVales,columnNames);
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

    private JPanel actionPanel(){
        JPanel actionPanel = new JPanel();
        // 添加参数
        final JButton addButton = new JButton("添加");

        addButton.setUI(new BasicButtonUI());// 恢复基本视觉效果
        addButton.setPreferredSize(new Dimension(80, 27));// 设置按钮大小
        addButton.setContentAreaFilled(false);// 设置按钮透明
        addButton.setFont(new Font("粗体", Font.PLAIN, 15));// 按钮文本样式
        addButton.setMargin(new Insets(0, 0, 0, 0));// 按钮内容与边框距离

        addButton.addActionListener(e -> {
            String []rowValues = {"NULL", "NULL"};
            paramsTableModel.addRow(rowValues);
        });
        actionPanel.add(addButton);

        // 删除参数
        final JButton delButton = new JButton("删除");

        delButton.setUI(new BasicButtonUI());// 恢复基本视觉效果
        delButton.setPreferredSize(new Dimension(80, 27));// 设置按钮大小
        delButton.setContentAreaFilled(false);// 设置按钮透明
        delButton.setFont(new Font("粗体", Font.PLAIN, 15));// 按钮文本样式
        delButton.setMargin(new Insets(0, 0, 0, 0));// 按钮内容与边框距离

        delButton.addActionListener( e ->{
            int selectedRow = paramsTable.getSelectedRow();
            if(selectedRow!=-1){
                paramsTableModel.removeRow(selectedRow);
            }else{
                Messages.showMessageDialog("请选择一行参数", "警告", Messages.getWarningIcon());
            }
        });
        actionPanel.add(delButton);
        return actionPanel;
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

    private JPanel getParamActionPanel(){
        JPanel paramActionPanel = new JPanel(new GridBagLayout());
        paramActionPanel.setPreferredSize(new Dimension(200,30));
        paramActionPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        JButton addParam = new JButton("+");
        addParam.setUI(new BasicButtonUI());// 恢复基本视觉效果
        addParam.setContentAreaFilled(false);// 设置按钮透明
        addParam.setFont(new Font("粗体", Font.PLAIN, 12));// 按钮文本样式
        addParam.setMargin(new Insets(0, 0, 0, 0));// 按钮内容与边框距离
        paramActionPanel.add(addParam);

        JButton delParam = new JButton("-");
        delParam.setUI(new BasicButtonUI());// 恢复基本视觉效果
        delParam.setContentAreaFilled(false);// 设置按钮透明
        delParam.setFont(new Font("粗体", Font.PLAIN, 12));// 按钮文本样式
        delParam.setMargin(new Insets(0, 0, 0, 0));// 按钮内容与边框距离
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

}
