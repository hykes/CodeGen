package me.hehaiyang.codegen.windows;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.Messages;
import me.hehaiyang.codegen.model.Database;
import me.hehaiyang.codegen.model.Field;
import me.hehaiyang.codegen.model.IdeaContext;
import me.hehaiyang.codegen.config.SettingManager;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Desc:
 * Mail: hehaiyang@terminus.io
 * Date: 2017/4/23
 */
public class DatabaseWindow extends JFrame{

    public DatabaseWindow(IdeaContext ideaContext){
        setLayout(new BorderLayout());
        JFrame thisFrame = this;

        SettingManager settingManager = SettingManager.getInstance();

        JPanel configPanel = new JPanel();

        configPanel.setLayout(new BoxLayout(configPanel, BoxLayout.Y_AXIS));
        ComboBox databaseBox=new ComboBox();
        JButton connectBtn = new JButton("Connect");

        ComboBox tableBox=new ComboBox();

        JButton sureBtn = new JButton("Sure");

        List<Database> databases = settingManager.getDatabasesSetting().getDatabases();
        if(databases != null && !databases.isEmpty()){
            databaseBox.setRenderer(new ComboBoxCellRenderer());
            databases.forEach( it -> databaseBox.addItem(it));
        }else{
            databaseBox.addItem("无数据源");
            databaseBox.setEnabled(false);
            connectBtn.setEnabled(false);

            tableBox.addItem("请选择");
            tableBox.setEnabled(false);
            sureBtn.setEnabled(false);
        }

        configPanel.add(databaseBox);
        configPanel.add(connectBtn);

        configPanel.add(tableBox);
        configPanel.add(sureBtn);

        connectBtn.addActionListener( it ->{

            Database database = (Database) databaseBox.getSelectedItem();
            if(database != null){
                try {
                    Connection conn = DBOperation.getConnection(database.getDriver(), database.getUrl(), database.getUsername(), database.getPassword());

                    if (conn != null && !conn.isClosed()) {
                        List<String> list = DBOperation.getTables(conn);
                        tableBox.removeAllItems();
                        for (String str : list) {
                            tableBox.addItem(str);
                        }
                        tableBox.setEnabled(true);
                    }
                }catch (SQLException e){
                    Messages.showInfoMessage(thisFrame,"connect fail !", "Error");
                }
            }

        });

        sureBtn.addActionListener( it ->{
            Database database = (Database) databaseBox.getSelectedItem();
            if(database != null){
                try {
                    String table = tableBox.getSelectedItem().toString();
                    Connection conn = DBOperation.getConnection(database.getDriver(), database.getUrl(), database.getUsername(), database.getPassword());
                    if (conn != null && !conn.isClosed()) {
                        List<Field> fields = DBOperation.getColumns(table, conn);
                        ColumnEditorFrame frame = new ColumnEditorFrame(ideaContext, fields);
                        frame.setSize(800, 400);
                        frame.setAlwaysOnTop(false);
                        frame.setLocationRelativeTo(thisFrame);
                        frame.setVisible(true);
                        frame.setResizable(false);
                        dispose();
                    }
                }catch (SQLException e){
                    Messages.showInfoMessage(thisFrame,"get columns fail !", "Error");
                }
            }
        });

        add(configPanel, BorderLayout.CENTER);
    }

}
