package me.hehaiyang.codegen;

import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 * Desc:
 * Mail: hehaiyang@terminus.io
 * Date: 2017/3/23
 */
//维护表格
public class JTableDefaultTableModelTest extends JFrame{

    private DefaultTableModel tableModel;   //表格模型对象
    private JBTable table;
    private JTextField aTextField;
    private JTextField bTextField;

    public JTableDefaultTableModelTest()
    {
        super();
        setTitle("表格");
        setBounds(100,100,500,400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        String[] columnNames = {"A","B"};   //列名
        String [][]tableVales={{"A1","B1"},{"A2","B2"},{"A3","B3"},{"A4","B4"},{"A5","B5"}}; //数据
        tableModel = new DefaultTableModel(tableVales,columnNames);
        table = new JBTable(tableModel);
        JScrollPane scrollPane = new JBScrollPane(table);   //支持滚动
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        //jdk1.6
        //排序:
        //table.setRowSorter(new TableRowSorter(tableModel));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  //单选
        table.addMouseListener(new MouseAdapter(){    //鼠标事件
            public void mouseClicked(MouseEvent e){
                int selectedRow = table.getSelectedRow(); //获得选中行索引
                Object oa = tableModel.getValueAt(selectedRow, 0);
                Object ob = tableModel.getValueAt(selectedRow, 1);
                aTextField.setText(oa.toString());  //给文本框赋值
                bTextField.setText(ob.toString());
            }
        });
        scrollPane.setViewportView(table);
        final JPanel panel = new JPanel();
        getContentPane().add(panel,BorderLayout.SOUTH);
        panel.add(new JLabel("A: "));
        aTextField = new JTextField("A4",10);
        panel.add(aTextField);
        panel.add(new JLabel("B: "));
        bTextField = new JTextField("B4",10);
        panel.add(bTextField);
        final JButton addButton = new JButton("添加");   //添加按钮
        addButton.addActionListener(new ActionListener(){//添加事件
            public void actionPerformed(ActionEvent e){
                String []rowValues = {aTextField.getText(),bTextField.getText()};
                tableModel.addRow(rowValues);  //添加一行
                int rowCount = table.getRowCount() +1;   //行数加上1
                aTextField.setText("A"+rowCount);
                bTextField.setText("B"+rowCount);
            }
        });
        panel.add(addButton);

        final JButton updateButton = new JButton("修改");   //修改按钮
        updateButton.addActionListener(new ActionListener(){//添加事件
            public void actionPerformed(ActionEvent e){
                int selectedRow = table.getSelectedRow();//获得选中行的索引
                if(selectedRow!= -1)   //是否存在选中行
                {
                    //修改指定的值：
                    tableModel.setValueAt(aTextField.getText(), selectedRow, 0);
                    tableModel.setValueAt(bTextField.getText(), selectedRow, 1);
                    //table.setValueAt(arg0, arg1, arg2)
                }
            }
        });
        panel.add(updateButton);

        final JButton delButton = new JButton("删除");
        delButton.addActionListener(new ActionListener(){//添加事件
            public void actionPerformed(ActionEvent e){
                int selectedRow = table.getSelectedRow();//获得选中行的索引
                if(selectedRow!=-1)  //存在选中行
                {
                    tableModel.removeRow(selectedRow);  //删除行
                }
            }
        });
        panel.add(delButton);
    }
    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        JTableDefaultTableModelTest jTableDefaultTableModelTest = new JTableDefaultTableModelTest();
        jTableDefaultTableModelTest.setVisible(true);
    }

}