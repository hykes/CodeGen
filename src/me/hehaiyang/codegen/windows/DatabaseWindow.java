package me.hehaiyang.codegen.windows;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.MessageType;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.util.ui.JBUI;
import me.hehaiyang.codegen.model.Field;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.util.List;
/**
 * Desc:
 * Mail: hehaiyang@terminus.io
 * Date: 2017/4/23
 */
public class DatabaseWindow extends JFrame{

    public DatabaseWindow(){
        setLayout(new BorderLayout());
        setSize(JBUI.size(200, 200));
        JFrame thisFrame = this;

        final JPanel mainPanel = new JPanel(new GridLayout(1, 1));
        mainPanel.setPreferredSize(JBUI.size(200, 200));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 0));

        JPanel configPanel = new JPanel();

        JTextField urlField = new JTextField();
        urlField.setColumns(10);

        JTextField userField = new JTextField();
        userField.setColumns(10);

        JTextField passwordField = new JTextField();
        passwordField.setColumns(10);

        JButton connectBtn = new JButton("connect");

        GridBagConstraints s= new GridBagConstraints();
        s.fill = GridBagConstraints.BOTH;

        configPanel.add(new JLabel(" url:"));
        configPanel.add(urlField, s);
        configPanel.add(new JLabel(" user:"));
        configPanel.add(userField, s);
        configPanel.add(new JLabel(" password:"));
        configPanel.add(passwordField, s);
        configPanel.add(connectBtn, s);
        ComboBox comboBox=new ComboBox();
        comboBox.addItem("请选择");
        comboBox.setEnabled(false);
        // 驱动程序名
        String driver = "com.mysql.jdbc.Driver";
        // URL指向要访问的数据库名
//        String url = "jdbc:mysql://127.0.0.1:3306/hsh";

        connectBtn.addActionListener( e ->{
            String url = urlField.getText();
            String user = userField.getText();
            String password = passwordField.getText();
            DBOperation op = new DBOperation();
            Connection conn = op.getConnection(driver, url, user, password);

            List<String> list = op.getAllTables(conn);
            comboBox.removeAllItems();
            for(String str: list){
                comboBox.addItem(str);
            }
            comboBox.setEnabled(true);

        });

        configPanel.add(comboBox);

        JButton tableColumnBtn = new JButton("getColumn");
        tableColumnBtn.addActionListener( e ->{
            String url = urlField.getText();
            String user = userField.getText();
            String password = passwordField.getText();
            String table = comboBox.getSelectedItem().toString();
            DBOperation op = new DBOperation();
            Connection conn = op.getConnection(driver, url, user, password);
            List<Field> fields = op.getTableColumn(table, conn);
            ColumnEditorFrame frame = new ColumnEditorFrame(fields);
            frame.setSize(800, 400);
            frame.setAlwaysOnTop(false);
            frame.setLocationRelativeTo(null);

            thisFrame.setVisible(false);
            frame.setVisible(true);

        });

        configPanel.add(tableColumnBtn);


        final JPanel localPanel = new JPanel(new BorderLayout());
        localPanel.setBorder(IdeBorderFactory.createTitledBorder("User Defined Variables", false));
        localPanel.add(configPanel, BorderLayout.CENTER);
        mainPanel.add(localPanel);

        add(mainPanel, BorderLayout.CENTER);

        final JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        infoPanel.add(new JLabel("You can defined some variables for template.",
                MessageType.INFO.getDefaultIcon(), SwingConstants.LEFT));
        add(infoPanel, BorderLayout.SOUTH);
    }

    public static void main(String[] args){
        DatabaseWindow codeGenWindow = new DatabaseWindow();
        codeGenWindow.setSize(800, 400);
        codeGenWindow.setAlwaysOnTop(false);
        codeGenWindow.setLocationRelativeTo(null);
        codeGenWindow.setVisible(true);
    }

}
