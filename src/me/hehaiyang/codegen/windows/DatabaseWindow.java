package me.hehaiyang.codegen.windows;

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
        super();
        setLayout(new BorderLayout());

        JPanel configPanel = new JPanel();

        JTextField driverField = new JTextField();
        driverField.setColumns(15);

        JTextField urlField = new JTextField();
        urlField.setColumns(10);

        JTextField userField = new JTextField();
        userField.setColumns(10);

        JTextField passwordField = new JTextField();
        passwordField.setColumns(10);

        JButton connectBtn = new JButton();

        GridBagConstraints s= new GridBagConstraints();
        s.fill = GridBagConstraints.BOTH;

        configPanel.add(new JLabel("driver:"));
        configPanel.add(driverField, s);
        configPanel.add(new JLabel(" url:"));
        configPanel.add(urlField, s);
        configPanel.add(new JLabel(" user:"));
        configPanel.add(userField, s);
        configPanel.add(new JLabel(" password:"));
        configPanel.add(passwordField, s);
        configPanel.add(connectBtn, s);

        add(configPanel, BorderLayout.NORTH);

        // 驱动程序名
//        String driver = "com.mysql.jdbc.Driver";
//        // URL指向要访问的数据库名scutcs
//        String url = "jdbc:mysql://127.0.0.1:3306/hsh";
//
//        // MySQL配置时的用户名
//        String user = "root";
//
//        // MySQL配置时的密码
//        String password = "anywhere";

        connectBtn.addActionListener( e ->{

            String driver = driverField.getText();
            String url = urlField.getText();
            String user = userField.getText();
            String password = passwordField.getText();
            DBOperation op = new DBOperation();
            Connection conn = op.getConnection(driver, url, user, password);

            List<String> list = op.getAllTables(conn);
            for(String str: list){
                System.out.println(str);
                op.getTableColumn(str, conn);
                break;
            }

        });
    }

    public static void main(String[] args){
        DatabaseWindow codeGenWindow = new DatabaseWindow();
        codeGenWindow.setSize(800, 400);
        codeGenWindow.setAlwaysOnTop(false);
        codeGenWindow.setLocationRelativeTo(null);
        codeGenWindow.setVisible(true);
    }

}
