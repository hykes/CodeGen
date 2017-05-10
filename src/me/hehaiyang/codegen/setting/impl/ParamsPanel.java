package me.hehaiyang.codegen.setting.impl;

import com.google.common.collect.ImmutableMap;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import lombok.Data;
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
public class ParamsPanel extends JBPanel {

    private DefaultTableModel paramsTableModel;
    private JBTable paramsTable;

    public ParamsPanel(FormatSetting settings) {
        super();
        setLayout(new BorderLayout());

        paramsTable = new JBTable();
        paramsTableModel = getParamsTableModel(settings);

        paramsTable.setModel(paramsTableModel);
        // 单选
        paramsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // 支持滚动
        JScrollPane paramScrollPane = new JBScrollPane(paramsTable);
        paramScrollPane.setViewportView(paramsTable);

        add(paramScrollPane, BorderLayout.CENTER);
        add(getParamActionPanel(), BorderLayout.SOUTH);
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
     * 自定义参数操作按钮
     * @return
     */
    private JPanel getParamActionPanel(){
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
            }
        });
        actionPanel.add(delButton);
        return actionPanel;
    }

    public static void main(String[] args) {
        JFrame jFrame = new JFrame();
        FormatSetting setting = new FormatSetting();
        setting.setParams(ImmutableMap.of("email", "hehaiyangwork@qq.com"));
        jFrame.getContentPane().add(new ParamsPanel(setting), BorderLayout.CENTER);
        jFrame.setSize(300, 240);
        jFrame.setLocation(300, 200);
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
