package me.hehaiyang.codegen.components;

import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Desc:
 * Mail: hehaiyang@terminus.io
 * Date: 2017/3/23
 */
public class ConfigPanel extends JPanel{

    private DefaultTableModel tableModel;   //表格模型对象
    private JBTable table;

    public ConfigPanel() {
        super();
        this.setLayout(new GridLayout());
        // 列名
        String[] columnNames = {"参数名","参数值"};

        // 默认数据
        String [][]tableVales={{"1","2"}, {"1","2"}, {"1","2"}};

        tableModel = new DefaultTableModel(tableVales,columnNames);
        table = new JBTable(tableModel);
        // 单选
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // 支持滚动
        JScrollPane scrollPane = new JBScrollPane(table);
        scrollPane.setViewportView(table);
        this.add(scrollPane, BorderLayout.CENTER);


        final JPanel actionPanel = new JPanel();
        this.add(actionPanel,BorderLayout.SOUTH);

        // 添加按钮
        final JButton addButton = new JButton("添加");
        addButton.addActionListener(e -> {
            //添加一行
            String []rowValues = {"NULL", "NULL"};
            tableModel.addRow(rowValues);
        });
        actionPanel.add(addButton);

        final JButton delButton = new JButton("删除");
        delButton.addActionListener( e ->{
            int selectedRow = table.getSelectedRow();//获得选中行的索引
            //存在选中行
            if(selectedRow!=-1){
                tableModel.removeRow(selectedRow);  //删除行
            }
        });
        actionPanel.add(delButton);
    }
    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        JFrame j = new JFrame();
        j.setTitle("自定义参数");
        j.setBounds(100,100,600,400);
        j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        ConfigPanel s = new ConfigPanel();

        j.getContentPane().add(s);
        j.setVisible(true);
//        DefaultTableModel tableModel = s.tableModel;
//        JBTable table = s.table;
//        int count = table.getRowCount();
//
//        for(int i = 0;i< count; i++){
//            System.out.println(tableModel.getValueAt(i, 0));
//            System.out.println(tableModel.getValueAt(i, 1));
//        }

    }

}